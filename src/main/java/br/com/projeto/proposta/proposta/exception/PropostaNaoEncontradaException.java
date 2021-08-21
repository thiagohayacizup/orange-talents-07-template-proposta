package br.com.projeto.proposta.proposta.exception;

public class PropostaNaoEncontradaException extends RuntimeException {

    public PropostaNaoEncontradaException( final String mensagem ){
        super( mensagem );
    }

}
