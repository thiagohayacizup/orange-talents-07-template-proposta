package br.com.projeto.proposta.carteira.controlador;

import br.com.projeto.proposta.cartao.Cartao;
import br.com.projeto.proposta.cartao.CartaoRepositorio;
import br.com.projeto.proposta.cartao.sistema.legado.CartaoApiExterna;
import br.com.projeto.proposta.carteira.Carteira;
import br.com.projeto.proposta.carteira.CarteiraDigital;
import br.com.projeto.proposta.carteira.CarteiraDigitalRepositorio;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

class CarteiraRequisicao {

    @NotNull(message = "Carteira nao pode se nulo.")
    private Carteira carteira;

    @NotBlank(message = "Email nao pode ser branco ou nulo.")
    private String email;

    public void setCarteira(final Carteira carteira) {
        this.carteira = carteira;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    private String cartao;

    CarteiraRequisicao comCartao( final String cartao ){
        this.cartao = cartao;
        return this;
    }

    CarteiraResposta associar(final CartaoRepositorio cartaoRepositorio, final CarteiraDigitalRepositorio carteiraDigitalRepositorio, final CartaoApiExterna cartaoApiExterna ){
        return new CarteiraResposta(
                new CarteiraDigital( carteira, email, Cartao.buscarPeloNumeroCartao( cartao, cartaoRepositorio ) )
                        .associar( carteiraDigitalRepositorio, cartaoApiExterna )
        );
    }

}
