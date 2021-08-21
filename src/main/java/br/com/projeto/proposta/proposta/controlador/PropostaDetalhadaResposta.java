package br.com.projeto.proposta.proposta.controlador;

import br.com.projeto.proposta.proposta.Proposta;
import br.com.projeto.proposta.proposta.StatusProposta;

import java.math.BigDecimal;

class PropostaDetalhadaResposta {

    private final Proposta proposta;

    PropostaDetalhadaResposta(final Proposta proposta) {
        this.proposta = proposta;
    }

    public String getDocumento() {
        return proposta.getDocumento();
    }

    public String getEmail() {
        return proposta.getEmail();
    }

    public String getNome() {
        return proposta.getNome();
    }

    public String getEndereco() {
        return proposta.getEndereco();
    }

    public BigDecimal getSalario() {
        return proposta.getSalario();
    }

    public StatusProposta getStatus() {
        return proposta.getStatus();
    }

    public String getNumeroCartao() {
        return proposta.getNumeroCartao();
    }

}
