package br.com.projeto.proposta.aviso.viagem;

import br.com.projeto.proposta.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;

@Entity
public class AvisoViagem {

    public static Builder construtor(){
        return new Builder();
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

    private AvisoViagem(){}

    public AvisoViagem( final Builder builder ){
        this.cartao = builder.cartao;
        this.destino = builder.destino;
        this.dataTermino = builder.dataTermino;
        this.ip = builder.ip;
        this.userAgent = builder.userAgent;
    }

    public AvisoViagem registrar( final AvisoViagemRepositorio avisoViagemRepositorio) {
        return avisoViagemRepositorio.save( this );
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

}
