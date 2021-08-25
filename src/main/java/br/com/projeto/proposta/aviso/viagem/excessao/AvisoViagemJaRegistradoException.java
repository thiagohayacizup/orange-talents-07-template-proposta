package br.com.projeto.proposta.aviso.viagem.excessao;

public class AvisoViagemJaRegistradoException extends RuntimeException{

    public AvisoViagemJaRegistradoException( final String mensagem ){
        super(mensagem);
    }

}
