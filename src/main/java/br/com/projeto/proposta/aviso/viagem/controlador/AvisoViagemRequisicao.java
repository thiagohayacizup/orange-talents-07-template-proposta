package br.com.projeto.proposta.aviso.viagem.controlador;

import br.com.projeto.proposta.aviso.viagem.AvisoViagem;
import br.com.projeto.proposta.aviso.viagem.AvisoViagemRepositorio;
import br.com.projeto.proposta.cartao.Cartao;
import br.com.projeto.proposta.cartao.CartaoRepositorio;

import java.time.LocalDate;

class AvisoViagemRequisicao {

    private final AvisoViagem.Builder builder;

    AvisoViagemRequisicao() {
        this.builder = AvisoViagem.construtor();
    }

    public void setDestino(final String destino) {
        builder.comDestino( destino );
    }

    public void setDataTermino(final LocalDate dataTermino) {
        builder.comDataTermino( dataTermino );
    }

    AvisoViagemRequisicao comIp( final String ip ){
        builder.comIp( ip );
        return this;
    }

    AvisoViagemRequisicao comUserAgent( final String userAgent ){
        builder.comUserAgent( userAgent );
        return this;
    }

    AvisoViagemResposta registrar(final String cartao, final CartaoRepositorio cartaoRepositorio, final AvisoViagemRepositorio avisoViagemRepositorio){
        return new AvisoViagemResposta(
                builder
                        .comCartao( Cartao.buscarPeloNumeroCartao( cartao, cartaoRepositorio ) )
                        .construir()
                        .registrar( avisoViagemRepositorio )
        );
    }

}
