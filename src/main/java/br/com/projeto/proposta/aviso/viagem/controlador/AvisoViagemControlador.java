package br.com.projeto.proposta.aviso.viagem.controlador;

import br.com.projeto.proposta.aviso.viagem.AvisoViagemRepositorio;
import br.com.projeto.proposta.cartao.CartaoRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
class AvisoViagemControlador {

    private final CartaoRepositorio cartaoRepositorio;

    private final AvisoViagemRepositorio avisoViagemRepositorio;

    AvisoViagemControlador(final CartaoRepositorio cartaoRepositorio, final AvisoViagemRepositorio avisoViagemRepositorio) {
        this.cartaoRepositorio = cartaoRepositorio;
        this.avisoViagemRepositorio = avisoViagemRepositorio;
    }

    @PostMapping("/aviso-viagem/cartao/{cartao}")
    @ResponseStatus( HttpStatus.OK )
    public @ResponseBody AvisoViagemResposta registrar(final HttpServletRequest request, @PathVariable("cartao") final String cartao, @RequestBody @Valid final AvisoViagemRequisicao avisoViagemRequisicao){
        return avisoViagemRequisicao
                .comIp( request.getHeader("ip") )
                .comUserAgent( request.getHeader("User-Agent") )
                .registrar( cartao, cartaoRepositorio, avisoViagemRepositorio );
    }

}
