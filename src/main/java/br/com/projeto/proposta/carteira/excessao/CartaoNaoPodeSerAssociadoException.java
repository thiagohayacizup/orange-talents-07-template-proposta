package br.com.projeto.proposta.carteira.excessao;

public class CartaoNaoPodeSerAssociadoException extends RuntimeException{

    public CartaoNaoPodeSerAssociadoException( final String mensagem ){
        super(mensagem);
    }

}
