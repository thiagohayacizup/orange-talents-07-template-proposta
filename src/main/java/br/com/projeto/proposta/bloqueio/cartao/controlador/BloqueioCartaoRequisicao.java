package br.com.projeto.proposta.bloqueio.cartao.controlador;

import br.com.projeto.proposta.bloqueio.cartao.BloqueioCartao;
import br.com.projeto.proposta.bloqueio.cartao.BloqueioCartaoRepositorio;
import br.com.projeto.proposta.cartao.Cartao;
import br.com.projeto.proposta.cartao.CartaoRepositorio;

public class BloqueioCartaoRequisicao {

    private final String ip;

    private final String userAgent;

    public BloqueioCartaoRequisicao(final String ip, final String userAgent) {
        this.ip = ip;
        this.userAgent = userAgent;
    }

    BloqueioCartaoResposta bloquear(final String cartao, final BloqueioCartaoRepositorio bloqueioCartaoRepositorio, final CartaoRepositorio cartaoRepositorio){
        return new BloqueioCartaoResposta(
                new BloqueioCartao( ip, userAgent, Cartao.buscarPeloNumeroCartao(cartao, cartaoRepositorio) )
                        .salvarSolicitacao( bloqueioCartaoRepositorio )
        );
    }

}
