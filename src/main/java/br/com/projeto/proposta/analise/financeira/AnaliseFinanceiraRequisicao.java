package br.com.projeto.proposta.analise.financeira;

public class AnaliseFinanceiraRequisicao {

    public AnaliseFinanceiraRequisicao(final String documento, final String nome, final String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
    }

    public String getDocumento() {
        return documento;
    }

    private final String documento;

    public String getNome() {
        return nome;
    }

    private final String nome;

    public String getIdProposta() {
        return idProposta;
    }

    private final String idProposta;

}
