package br.com.projeto.proposta.carteira.excessao;

public class CartaoNaoPodeSerAssociadoSistemaCaiuException extends RuntimeException{

    public CartaoNaoPodeSerAssociadoSistemaCaiuException( final String mensagem ){
        super( mensagem );
    }

}
