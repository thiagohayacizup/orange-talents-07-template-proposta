package br.com.projeto.proposta.biometria;

import br.com.projeto.proposta.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
public class Biometria {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @NotBlank
    private @NotNull String fingerprint;

    @ManyToOne( cascade = CascadeType.PERSIST )
    private @NotNull Cartao cartao;

    private final Instant dataCadastro = Instant.now();

    private Biometria(){}

    public Biometria( final String fingerprint, final Cartao cartao ){
        this.fingerprint = fingerprint;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }

    public Biometria cadastrar(final BiometriaRepositorio biometriaRepositorio) {
        return biometriaRepositorio.save( this );
    }

}
