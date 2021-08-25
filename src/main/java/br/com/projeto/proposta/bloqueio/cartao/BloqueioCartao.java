package br.com.projeto.proposta.bloqueio.cartao;

import br.com.projeto.proposta.bloqueio.cartao.excessao.BloqueioCartaoJaSolicitadoOuJaBloqueadoException;
import br.com.projeto.proposta.cartao.Cartao;
import br.com.projeto.proposta.cartao.sistema.legado.CartaoApiExterna;
import br.com.projeto.proposta.cartao.sistema.legado.bloquear.cartao.BloqueioRequisicao;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Entity
public class BloqueioCartao {

    public static List<BloqueioCartao> buscarCartoesNaoBloqueados( final BloqueioCartaoRepositorio bloqueioCartaoRepositorio ){
        return bloqueioCartaoRepositorio.findFirst10ByStatus( StatusBloqueio.NAO_BLOQUEADO );
    }

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @NotBlank(message = "Ip nao pode ser branco ou nulo.")
    private @NotNull String ip;

    @NotBlank(message = "User agent nao pode ser branco ou nulo.")
    private @NotNull String userAgent;

    @OneToOne( cascade = CascadeType.PERSIST )
    private @NotNull Cartao cartao;

    private Instant instanteBloqueio;

    @Enumerated( EnumType.STRING )
    private StatusBloqueio status = StatusBloqueio.NAO_BLOQUEADO;

    private static final transient String sistema = "Proposta";

    private BloqueioCartao(){}

    public BloqueioCartao( final String ip, final String userAgent, final Cartao cartao ){
        this.ip = ip;
        this.userAgent = userAgent;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }

    public StatusBloqueio getStatusBloqueio() {
        return status;
    }

    public BloqueioCartao salvarSolicitacao( final BloqueioCartaoRepositorio bloqueioCartaoRepositorio ){
        bloqueioCartaoRepositorio
                .findIdByCartao( cartao )
                .ifPresent(bloqueioCartao -> {
                    throw new BloqueioCartaoJaSolicitadoOuJaBloqueadoException(
                            String.format("Bloqueio do cartao { %s } status { %s }.", bloqueioCartao.cartao.getNumero(), bloqueioCartao.status.toString())
                    );
                });
        return bloqueioCartaoRepositorio.save( this );
    }

    private transient final Logger logger = LoggerFactory.getLogger(BloqueioCartao.class);

    public void bloquear(final CartaoApiExterna cartaoApiExterna, final BloqueioCartaoRepositorio bloqueioCartaoRepositorio ) throws FeignException {
        status = StatusBloqueio.paraStatus(
                cartaoApiExterna
                        .bloquear( cartao.getNumero(), new BloqueioRequisicao( sistema ) )
                        .getResultado()
        );
        instanteBloqueio = Instant.now();
        bloqueioCartaoRepositorio.save( this );
        logger.info(
                "Cartao com numero ***-{} bloqueado com sucesso.",
                cartao.getNumero().split("-")[3]
        );
    }

    public static BloqueioCartao mock(){
        return new BloqueioCartao("10.10.10.10", "Chrome", new Cartao("1234-1234-1234-1234"));
    }

}
