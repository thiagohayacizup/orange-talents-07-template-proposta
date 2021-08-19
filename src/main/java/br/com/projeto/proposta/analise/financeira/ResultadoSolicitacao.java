package br.com.projeto.proposta.analise.financeira;

import br.com.projeto.proposta.proposta.StatusProposta;

public enum ResultadoSolicitacao {

    SEM_RESTRICAO{
        @Override
        public StatusProposta statusProposta() {
            return StatusProposta.ELEGIVEL;
        }
    },
    COM_RESTRICAO{
        @Override
        public StatusProposta statusProposta() {
            return StatusProposta.NAO_ELEGIVEL;
        }
    };

    public abstract StatusProposta statusProposta();

}
