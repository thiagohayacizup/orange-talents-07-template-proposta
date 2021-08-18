package br.com.projeto.proposta.documento;

public class Documento {

    static final Integer[][] PESOS_CPF = {{10,9,8,7,6,5,4,3,2}, {11,10,9,8,7,6,5,4,3,2}};
    static final Integer[][] PESOS_CNPJ = {{5,4,3,2,9,8,7,6,5,4,3,2}, {6,5,4,3,2,9,8,7,6,5,4,3,2}};

    static final Integer[] DIGITOS_VERIFICADORES_CPF = {9, 10};
    static final Integer[] DIGITOS_VERIFICADORES_CNPJ = {12,13};

    static final Integer TAMANHO_CPF = 11;
    static final Integer TAMANHO_CNPJ = 14;

    public static boolean tamanhoValido( final String documento, final TipoDocumento tipoDocumento ){
        return documento.length() == tipoDocumento.tamanho();
    }

    private final String documento;

    private boolean valido;

    private TipoDocumento tipoDocumento;

    Documento(final String documento) {
        this.documento = documento;
        valido = true;
    }

    public static Documento de( final String cpf ){
        return new Documento( cpf );
    }

    public Documento tipo( final TipoDocumento tipoDocumento ){
        this.tipoDocumento = tipoDocumento;
        return this;
    }

    public Documento validarPrimeiroDigito(){
        int somatoria = 0;
        for( int contador = 0; contador < tipoDocumento.pesos()[0].length; contador++ ){
            somatoria += Character.getNumericValue(documento.charAt( contador )) * tipoDocumento.pesos()[0][ contador ];
        }
        int modulo = ( somatoria * 10 ) % 11;
        if( modulo == 10 ) modulo = 0;
        if( Character.getNumericValue( documento.charAt( tipoDocumento.digitosVerificadores()[0] ) ) != modulo ) valido = false;
        return this;
    }

    public Documento validarSegundoDigito(){
        int somatoria = 0;
        for( int contador = 0; contador < tipoDocumento.pesos()[1].length; contador++ ){
            somatoria += Character.getNumericValue(documento.charAt( contador )) * tipoDocumento.pesos()[1][ contador ];
        }
        int modulo = ( somatoria * 10 ) % 11;
        if( modulo ==10 ) modulo = 0;
        if( Character.getNumericValue( documento.charAt( tipoDocumento.digitosVerificadores()[1] ) ) != modulo ) valido = false;
        return this;
    }

    public boolean eValido(){
        return valido;
    }

}
