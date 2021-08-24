package br.com.projeto.proposta.cartao.sistema.legado.bloquear.cartao;

public class BloqueioRequisicao {

    private final String sistemaResponsavel;

    public BloqueioRequisicao(final String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }

}
