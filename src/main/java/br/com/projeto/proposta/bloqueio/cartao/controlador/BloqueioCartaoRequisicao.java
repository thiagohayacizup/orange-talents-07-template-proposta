package br.com.projeto.proposta.bloqueio.cartao.controlador;

import br.com.projeto.proposta.bloqueio.cartao.BloqueioCartao;
import br.com.projeto.proposta.bloqueio.cartao.BloqueioCartaoRepositorio;
import br.com.projeto.proposta.cartao.Cartao;
import br.com.projeto.proposta.cartao.CartaoRepositorio;

import javax.validation.constraints.NotBlank;

public class BloqueioCartaoRequisicao {

    @NotBlank(message = "Ip nao pode ser branco ou nulo")
    private String ip;

    @NotBlank(message = "User agent nao pode ser branco ou nulo")
    private String userAgent;

    public void setIp(final String ip) {
        this.ip = ip;
    }

    public void setUserAgent(final String userAgent) {
        this.userAgent = userAgent;
    }

    BloqueioCartaoResposta bloquear(final String cartao, final BloqueioCartaoRepositorio bloqueioCartaoRepositorio, final CartaoRepositorio cartaoRepositorio){
        return new BloqueioCartaoResposta(
                new BloqueioCartao( ip, userAgent, Cartao.buscarPeloNumeroCartao(cartao, cartaoRepositorio) )
                        .salvarSolicitacao( bloqueioCartaoRepositorio )
        );
    }

}
