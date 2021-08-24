package br.com.projeto.proposta.bloqueio.cartao.controlador;

import br.com.projeto.proposta.bloqueio.cartao.BloqueioCartaoRepositorio;
import br.com.projeto.proposta.cartao.CartaoRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
class BloqueioCartaoControlador {

    private final BloqueioCartaoRepositorio bloqueioCartaoRepositorio;

    private final CartaoRepositorio cartaoRepositorio;

    BloqueioCartaoControlador(final BloqueioCartaoRepositorio bloqueioCartaoRepositorio, final CartaoRepositorio cartaoRepositorio) {
        this.bloqueioCartaoRepositorio = bloqueioCartaoRepositorio;
        this.cartaoRepositorio = cartaoRepositorio;
    }

    @PostMapping("/bloqueio/cartao/{cartao}")
    @ResponseStatus( HttpStatus.OK )
    public @ResponseBody BloqueioCartaoResposta bloquear(@PathVariable("cartao") final String cartao, @RequestBody @Valid final BloqueioCartaoRequisicao bloqueioCartaoRequisicao){
        return bloqueioCartaoRequisicao.bloquear( cartao, bloqueioCartaoRepositorio, cartaoRepositorio);
    }

}
