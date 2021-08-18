package br.com.projeto.proposta.proposta.controlador;

import br.com.projeto.proposta.proposta.Proposta;

class PropostaResposta {

    private final Proposta proposta;

    PropostaResposta(final Proposta proposta) {
        this.proposta = proposta;
    }

    public Long getId(){
        return proposta.getId();
    }

    public String getNome(){
        return proposta.getNome();
    }

}
