package br.com.projeto.proposta.aviso.viagem.controlador;

import br.com.projeto.proposta.aviso.viagem.AvisoViagem;

class AvisoViagemResposta {

    private final AvisoViagem avisoViagem;

    AvisoViagemResposta(final AvisoViagem avisoViagem) {
        this.avisoViagem = avisoViagem;
    }

    public Long getId(){
        return avisoViagem.getId();
    }

}
