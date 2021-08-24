package br.com.projeto.proposta.bloqueio.cartao.controlador;

import br.com.projeto.proposta.bloqueio.cartao.BloqueioCartao;
import br.com.projeto.proposta.bloqueio.cartao.StatusBloqueio;

class BloqueioCartaoResposta {

    private static final String MENSAGEM_EM_AGUARDO = "Aguarde enquanto processamos o bloqueio do seu cartao";

    private final BloqueioCartao bloqueioCartao;

    BloqueioCartaoResposta(final BloqueioCartao bloqueioCartao) {
        this.bloqueioCartao = bloqueioCartao;
    }

    public Long getId(){
        return bloqueioCartao.getId();
    }

    public StatusBloqueio getStatus(){
        return bloqueioCartao.getStatusBloqueio();
    }

    public String getMensagem(){
        return MENSAGEM_EM_AGUARDO;
    }

}
