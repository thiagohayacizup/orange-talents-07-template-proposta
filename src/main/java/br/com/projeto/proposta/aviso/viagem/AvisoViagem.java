package br.com.projeto.proposta.aviso.viagem;

import br.com.projeto.proposta.aviso.viagem.excessao.AvisoViagemJaRegistradoException;
import br.com.projeto.proposta.bloqueio.cartao.BloqueioCartao;
import br.com.projeto.proposta.cartao.Cartao;
import br.com.projeto.proposta.cartao.sistema.legado.CartaoApiExterna;
import br.com.projeto.proposta.cartao.sistema.legado.aviso.viagem.AvisoViagemRequisicao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
public class AvisoViagem {

    public static Builder construtor(){
        return new Builder();
    }

    public static List<AvisoViagem> buscarAvisosViagemStatusNaoCriadosSistemaExterno( final AvisoViagemRepositorio avisoViagemRepositorio ){
        return avisoViagemRepositorio.findFirst10ByStatus( StatusAvisoViagem.NAO_CRIADO );
    }

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @ManyToOne( cascade = CascadeType.PERSIST )
    private @NotNull Cartao cartao;

    @NotBlank(message = "Destino nao pode ser branco ou nulo.")
    private @NotNull String destino;

    @NotNull(message = "Data de termino nao pode ser nulo.")
    @FutureOrPresent( message = "Data de termino nao pode estar no passado.")
    private LocalDate dataTermino;

    @NotNull(message = "Ip header nao deve ser branco ou nulo.")
    private String ip;

    @NotNull( message = "User agent header nao pode ser nulo ou branco.")
    private String userAgent;

    private @NotNull final Instant instanteAviso = Instant.now();

    @Enumerated( EnumType.STRING )
    private @NotNull StatusAvisoViagem status = StatusAvisoViagem.NAO_CRIADO;

    private AvisoViagem(){}

    public AvisoViagem( final Builder builder ){
        this.cartao = builder.cartao;
        this.destino = builder.destino;
        this.dataTermino = builder.dataTermino;
        this.ip = builder.ip;
        this.userAgent = builder.userAgent;
    }

    public AvisoViagem registrar( final AvisoViagemRepositorio avisoViagemRepositorio) {
        avisoViagemRepositorio
                .findIdByCartao( cartao )
                .ifPresent(avisoViagem -> {
                        throw new AvisoViagemJaRegistradoException(
                                String.format("Aviso viagem para cartao { %s } ja foi registrado.", cartao.getNumero())
                        );
                });
        return avisoViagemRepositorio.save( this );
    }

    private transient final Logger logger = LoggerFactory.getLogger(AvisoViagem.class);

    public void notificar(final CartaoApiExterna cartaoApiExterna, final AvisoViagemRepositorio avisoViagemRepositorio) {
        status = StatusAvisoViagem.paraStatus(
                cartaoApiExterna
                        .criarAviso( cartao.getNumero(), new AvisoViagemRequisicao( destino, dataTermino) )
                        .getResultado()
        );
        avisoViagemRepositorio.save( this );
        if(status.equals( StatusAvisoViagem.CRIADO ) ) {
            logger.info(
                    "Cartao com numero ***-{} aviso viagem processado com sucesso.",
                    cartao.getNumero().split("-")[3]
            );
        }else{
            logger.info(
                    "Cartao com numero ***-{} aviso viagem falhou ao processar.",
                    cartao.getNumero().split("-")[3]
            );
        }
    }

    public static class Builder{

        private Cartao cartao;
        private String destino;
        private LocalDate dataTermino;
        private String ip;
        private String userAgent;
        public Builder comCartao(final Cartao cartao) {
            this.cartao = cartao;
            return this;
        }

        public Builder comDestino(final String destino) {
            this.destino = destino;
            return this;
        }

        public Builder comDataTermino(final LocalDate dataTermino) {
            this.dataTermino = dataTermino;
            return this;
        }

        public Builder comIp(final String ip) {
            this.ip = ip;
            return this;
        }

        public Builder comUserAgent(final String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public AvisoViagem construir(){
            return new AvisoViagem( this );
        }


    }

    public Long getId() {
        return id;
    }

    public StatusAvisoViagem getStatus() {
        return status;
    }

    public static AvisoViagem mock() {
        return AvisoViagem
                .construtor()
                .comIp("127.0.0.1")
                .comUserAgent("Mozilla Firefox")
                .comDestino("Brasil")
                .comDataTermino(LocalDate.now())
                .comCartao( new Cartao("1234-1234-1234-1234") )
                .construir();
    }

}
