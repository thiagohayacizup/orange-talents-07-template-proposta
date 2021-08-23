package br.com.projeto.proposta.biometria.controlador;

import br.com.projeto.proposta.biometria.Biometria;

class BiometriaResposta {

    private final Biometria biometria;

    BiometriaResposta(final Biometria biometria) {
        this.biometria = biometria;
    }

    public Long getId(){
        return biometria.getId();
    }

}
