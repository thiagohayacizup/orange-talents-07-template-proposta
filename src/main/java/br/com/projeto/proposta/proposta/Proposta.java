package br.com.projeto.proposta.proposta;

import br.com.projeto.proposta.analise.financeira.AnaliseFinanceira;
import br.com.projeto.proposta.analise.financeira.AnaliseFinanceiraRequisicao;
import br.com.projeto.proposta.cartao.Cartao;
import br.com.projeto.proposta.cartao.CartaoRequisicao;
import br.com.projeto.proposta.email.Email;
import br.com.projeto.proposta.proposta.exception.EmailInvalidoException;
import br.com.projeto.proposta.proposta.exception.PropostaComDocumentoJaCriadaException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Proposta {

    public static Builder construtor(){
        return new Builder();
    }

    public static List<Proposta> buscarPropostarSemCartoes( final PropostaRepositorio propostaRepositorio ){
        return propostaRepositorio.findFirst10ByStatusAndNumeroCartaoIsNull( StatusProposta.ELEGIVEL );
    }

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(columnDefinition = "CHAR(18)")
    @NotBlank(message = "Documento nao pode ser branco.")
    private @NotNull String documento;

    @NotBlank(message = "Email nao pode ser branco.")
    private @NotNull String email;

    @NotBlank(message = "Nome nao pode ser branco.")
    private @NotNull String nome;

    @NotBlank(message = "Endereco nao pode ser branco.")
    private @NotNull String endereco;

    @DecimalMin( value = "0.0", message = "Salario nao pode ser negativo.")
    private @NotNull BigDecimal salario;

    @Enumerated( EnumType.STRING )
    private StatusProposta status;

    private String numeroCartao;

    private Proposta(){}

    Proposta( final Builder builder ){
        documento = builder.documento;
        nome = builder.nome;
        endereco = builder.endereco;
        salario = builder.salario;
        if( !Email.match(builder.email) )
            throw new EmailInvalidoException(
                    String.format("Email { %s } com formato invalido.", builder.email )
            );
        email = builder.email;
    }

    public Proposta criar( final PropostaRepositorio propostaRepositorio, final AnaliseFinanceira analiseFinanceira ){
        naoPodeExistirPropostaParaDocumentoExistente( propostaRepositorio );
        propostaRepositorio.save( this );
        definirStatusProposta( analiseFinanceira );
        return propostaRepositorio.save( this );
    }

    private transient final Logger logger = LoggerFactory.getLogger(Proposta.class);

    public void associarCartao( final Cartao cartao, final PropostaRepositorio propostaRepositorio ) throws FeignException {
        numeroCartao = cartao
                .criarCartao(new CartaoRequisicao(documento, nome, id.toString()))
                .getNumeroCartao();
        propostaRepositorio.save(this);
        logger.info(
                "Cartao com numero ***-{} associado a proposta com documento {}.***-{}.",
                numeroCartao.split("-")[3],
                documento.split("\\.")[0],
                documento.split("-")[1]
        );
    }

    private void definirStatusProposta( final AnaliseFinanceira analiseFinanceira ){
        try {
            status = analiseFinanceira
                    .solicitar( new AnaliseFinanceiraRequisicao(documento, nome, id.toString()) )
                    .paraStatusProposta();
            logger.info(
                    "Analise financeira do solicitante {}.***-{} processado.",
                    documento.split("\\.")[0],
                    documento.split("-")[1]
            );
        }catch (FeignException exception){
            logger.error( exception.getMessage());
        }
    }

    private void naoPodeExistirPropostaParaDocumentoExistente( final PropostaRepositorio propostaRepositorio ){
        propostaRepositorio
                .findByDocumento( documento )
                .ifPresent( proposta -> {
                    throw new PropostaComDocumentoJaCriadaException(
                            String.format("Ja existe uma proposta com o documento { %s }.", documento)
                    );
                });
    }

    public static class Builder {

        private String documento;
        private String email;
        private String nome;
        private String endereco;
        private BigDecimal salario;

        public Builder comDocumento(final String documento) {
            this.documento = documento;
            return this;
        }

        public Builder comEmail(final String email) {
            this.email = email;
            return this;
        }

        public Builder comNome(final String nome) {
            this.nome = nome;
            return this;
        }

        public Builder comEndereco(final String endereco) {
            this.endereco = endereco;
            return this;
        }

        public Builder comSalario(final BigDecimal salario) {
            this.salario = salario;
            return this;
        }

        public Proposta construir(){
            return new Proposta( this );
        }

    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    StatusProposta getStatus() {
        return status;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public static Proposta mock(){
        final Proposta proposta = Proposta
                .construtor()
                .comDocumento("297.036.590-12")
                .comSalario(BigDecimal.TEN)
                .comEndereco("Rua x")
                .comNome("Proposta")
                .comEmail("email@dominio.com")
                .construir();
        proposta.id = 1L;
        return proposta;
    }

    public static Proposta mockCartao(){
        final Proposta proposta = Proposta
                .construtor()
                .comDocumento("297.036.590-12")
                .comSalario(BigDecimal.TEN)
                .comEndereco("Rua x")
                .comNome("Proposta")
                .comEmail("email@dominio.com")
                .construir();
        proposta.id = 1L;
        proposta.status = StatusProposta.ELEGIVEL;
        return proposta;
    }

}
