package br.com.projeto.proposta.carteira;

import br.com.projeto.proposta.cartao.Cartao;
import br.com.projeto.proposta.cartao.sistema.legado.CartaoApiExterna;
import br.com.projeto.proposta.cartao.sistema.legado.associar.carteira.CarteiraRequisicao;
import br.com.projeto.proposta.cartao.sistema.legado.associar.carteira.CarteiraResposta;
import br.com.projeto.proposta.carteira.excessao.CartaoJaEstaAssociadoACarteiraException;
import br.com.projeto.proposta.carteira.excessao.CartaoNaoPodeSerAssociadoException;
import br.com.projeto.proposta.carteira.excessao.CartaoNaoPodeSerAssociadoSistemaCaiuException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class CarteiraDigital {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Enumerated( EnumType.STRING )
    private @NotNull Carteira carteira;

    @NotBlank
    private @NotNull String email;

    @ManyToOne( cascade = CascadeType.PERSIST )
    private @NotNull Cartao cartao;

    @Enumerated( EnumType.STRING )
    private @NotNull StatusCarteira status = StatusCarteira.NAO_DEFINIDO;

    private @NotNull String identificadorCarteira;

    private CarteiraDigital(){}

    public CarteiraDigital( final Carteira carteira, final String email, final Cartao cartao ){
        this.carteira = carteira;
        this.email = email;
        this.cartao = cartao;
    }

    public CarteiraDigital associar(final CarteiraDigitalRepositorio carteiraDigitalRepositorio, final CartaoApiExterna cartaoApiExterna) {
        carteiraDigitalRepositorio
                .findByCarteiraAndCartaoAndStatus( carteira, cartao, StatusCarteira.ASSOCIADO )
                .ifPresent(carteiraDigital -> {
                    throw new CartaoJaEstaAssociadoACarteiraException(
                            String.format("Cartao { %s } ja associado a carteira { %s } com status { %s }", cartao.getNumero(), carteira, StatusCarteira.ASSOCIADO)
                    );
                });
        solicitarAssociacao( cartaoApiExterna );
        return salvar( carteiraDigitalRepositorio );
    }

    private CarteiraDigital salvar(final CarteiraDigitalRepositorio carteiraDigitalRepositorio) {
        if( status.equals( StatusCarteira.NAO_ASSOCIADO ) ){
            throw new CartaoNaoPodeSerAssociadoException(
                    String.format("Cartao { %s } nao pode ser associado.", cartao.getNumero())
            );
        }else if( status.equals( StatusCarteira.NAO_DEFINIDO ) ){
            throw new CartaoNaoPodeSerAssociadoSistemaCaiuException(
                    String.format("Cartao { %s } nao pode ser associado, sistema indisponivel.", cartao.getNumero())
            );
        }
        return carteiraDigitalRepositorio.save(this);
    }

    private transient final Logger logger = LoggerFactory.getLogger(CarteiraDigital.class);

    private void solicitarAssociacao(final CartaoApiExterna cartaoApiExterna) {
        try {
            final CarteiraResposta associar = cartaoApiExterna
                    .associar(cartao.getNumero(), new CarteiraRequisicao(email, carteira));
            status = StatusCarteira.paraStatus(associar.getResultado());
            identificadorCarteira = associar.getId();
            logger.info(
                    "Cartao ***-{} associado a carteira {}.",
                    cartao.getNumero().split("-")[3],
                    carteira.toString()
            );
        }catch (FeignException.FeignClientException exception){
            status = StatusCarteira.NAO_ASSOCIADO;
        }catch (FeignException exception){
            logger.error( exception.getMessage());
        }
    }


    public Long getId() {
        return id;
    }

    public Carteira getCarteira() {
        return carteira;
    }

    public StatusCarteira getStatus() {
        return status;
    }

}
