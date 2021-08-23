package br.com.projeto.proposta.cartao;

import br.com.projeto.proposta.cartao.excessao.CartaoNaoEncontradoException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Cartao {

    public static Cartao of(final String numero) {
        return new Cartao( numero );
    }

    public static Cartao buscarPeloNumeroCartao(final String numero, final CartaoRepositorio cartaoRepositorio) {
        return cartaoRepositorio
                .findByNumero( numero )
                .orElseThrow(() -> new CartaoNaoEncontradoException(
                        String.format("Cartao com numero { %s } nao foi encontrado.", numero)
                ));
    }

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private @NotNull String numero;

    private Cartao(){}

    public Cartao( final String numero ){
        this.numero = numero;
    }

    public String getNumero() {
        return numero;
    }

}
