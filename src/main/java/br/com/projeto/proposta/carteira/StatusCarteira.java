package br.com.projeto.proposta.carteira;

public enum StatusCarteira {

    ASSOCIADO,
    NAO_ASSOCIADO,
    NAO_DEFINIDO;

    public static StatusCarteira paraStatus( final String resultado ){
        System.out.println(resultado);
        if( resultado.equals("ASSOCIADA") ) return ASSOCIADO;
        return NAO_ASSOCIADO;
    }

}
