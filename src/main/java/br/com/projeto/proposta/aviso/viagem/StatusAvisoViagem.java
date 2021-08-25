package br.com.projeto.proposta.aviso.viagem;

public enum StatusAvisoViagem {

    CRIADO,
    NAO_CRIADO;

    static StatusAvisoViagem paraStatus( final String resultado){
        if( resultado.equals("CRIADO") ) return CRIADO;
        return NAO_CRIADO;
    }

}
