package br.com.projeto.proposta.proposta.exception;

public class EmailInvalidoException extends RuntimeException {

    public EmailInvalidoException( final String mensagem ){
        super( mensagem );
    }

}
