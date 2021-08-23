package br.com.projeto.proposta.biometria.controlador;

import br.com.projeto.proposta.biometria.Biometria;
import br.com.projeto.proposta.biometria.BiometriaRepositorio;
import br.com.projeto.proposta.cartao.Cartao;
import br.com.projeto.proposta.cartao.CartaoRepositorio;

import javax.validation.constraints.NotBlank;

class BiometriaRequisicao {

    @NotBlank( message = "Fingerprint nao pode ser branco ou nulo." )
    private String fingerprint;

    public void setFingerprint(final String fingerprint) {
        this.fingerprint = fingerprint;
    }

    BiometriaResposta cadastrar(final String cartao, final CartaoRepositorio cartaoRepositorio, final BiometriaRepositorio biometriaRepositorio){
        return new BiometriaResposta(
                new Biometria(
                        fingerprint,
                        Cartao.buscarPeloNumeroCartao(cartao, cartaoRepositorio)
                ).cadastrar( biometriaRepositorio )
        );
    }

}
