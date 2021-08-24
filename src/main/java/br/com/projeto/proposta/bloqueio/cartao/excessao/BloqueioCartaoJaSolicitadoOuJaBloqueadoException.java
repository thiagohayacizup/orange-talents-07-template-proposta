package br.com.projeto.proposta.bloqueio.cartao.excessao;

public class BloqueioCartaoJaSolicitadoOuJaBloqueadoException extends RuntimeException {

    public BloqueioCartaoJaSolicitadoOuJaBloqueadoException( final String mensagem ){
        super(mensagem);
    }

}
