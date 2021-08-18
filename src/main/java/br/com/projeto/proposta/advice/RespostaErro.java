package br.com.projeto.proposta.advice;

class RespostaErro {

    private final Integer codigo;

    private final String mensagem;

    RespostaErro(final Integer codigo, final String mensagem) {
        this.codigo = codigo;
        this.mensagem = mensagem;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getMensagem() {
        return mensagem;
    }

}
