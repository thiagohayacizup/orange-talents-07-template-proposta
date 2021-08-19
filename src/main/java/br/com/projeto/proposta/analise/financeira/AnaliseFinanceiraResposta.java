package br.com.projeto.proposta.analise.financeira;

import br.com.projeto.proposta.proposta.StatusProposta;

public class AnaliseFinanceiraResposta {

    private ResultadoSolicitacao resultadoSolicitacao;

    public void setResultadoSolicitacao(final ResultadoSolicitacao resultadoSolicitacao) {
        this.resultadoSolicitacao = resultadoSolicitacao;
    }

    public StatusProposta paraStatusProposta() {
        return resultadoSolicitacao.statusProposta();
    }

}
