package br.com.projeto.proposta.bloqueio.cartao;

public enum StatusBloqueio {

    BLOQUEADO,

    NAO_BLOQUEADO;

    public static StatusBloqueio paraStatus( final String resultado ){
        if( resultado.equals("BLOQUEADO") ) return BLOQUEADO;
        return NAO_BLOQUEADO;
    }

}
