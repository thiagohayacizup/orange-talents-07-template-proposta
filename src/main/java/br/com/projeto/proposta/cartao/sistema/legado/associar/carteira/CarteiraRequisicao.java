package br.com.projeto.proposta.cartao.sistema.legado.associar.carteira;

import br.com.projeto.proposta.carteira.Carteira;

public class CarteiraRequisicao {

    private final String email;

    private final Carteira carteira;

    public CarteiraRequisicao(final String email, final Carteira carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public String getEmail() {
        return email;
    }

    public Carteira getCarteira() {
        return carteira;
    }

}
