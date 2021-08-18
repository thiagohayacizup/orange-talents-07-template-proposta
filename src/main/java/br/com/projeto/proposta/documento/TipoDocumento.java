package br.com.projeto.proposta.documento;

public enum TipoDocumento {

    CPF{
        @Override
        public Integer[][] pesos() {
            return Documento.PESOS_CPF;
        }

        @Override
        public Integer[] digitosVerificadores() {
            return Documento.DIGITOS_VERIFICADORES_CPF;
        }

        @Override
        public Integer tamanho() {
            return Documento.TAMANHO_CPF;
        }
    },
    CNPJ{
        @Override
        public Integer[][] pesos() {
            return Documento.PESOS_CNPJ;
        }

        @Override
        public Integer[] digitosVerificadores() {
            return Documento.DIGITOS_VERIFICADORES_CNPJ;
        }

        @Override
        public Integer tamanho() {
            return Documento.TAMANHO_CNPJ;
        }
    };

    public abstract Integer[][] pesos();

    public abstract Integer[] digitosVerificadores();

    public abstract Integer tamanho();

}
