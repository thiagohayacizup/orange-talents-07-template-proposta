package br.com.projeto.proposta.carteira.excessao;

public class CartaoJaEstaAssociadoACarteiraException extends RuntimeException{

    public CartaoJaEstaAssociadoACarteiraException( final String mensagem ){
        super( mensagem );
    }

}
