package br.com.projeto.proposta.cartao.excessao;

public class CartaoNaoEncontradoException extends RuntimeException {

    public CartaoNaoEncontradoException( final String mensagem ){
        super( mensagem );
    }

}
