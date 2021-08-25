package br.com.projeto.proposta.carteira.controlador;

import br.com.projeto.proposta.cartao.CartaoRepositorio;
import br.com.projeto.proposta.cartao.sistema.legado.CartaoApiExterna;
import br.com.projeto.proposta.carteira.CarteiraDigitalRepositorio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
class CarteiraControlador {

    private final CartaoRepositorio cartaoRepositorio;

    private final CarteiraDigitalRepositorio carteiraDigitalRepositorio;

    private final CartaoApiExterna cartaoApiExterna;

    CarteiraControlador(final CartaoRepositorio cartaoRepositorio, final CarteiraDigitalRepositorio carteiraDigitalRepositorio, final CartaoApiExterna cartaoApiExterna) {
        this.cartaoRepositorio = cartaoRepositorio;
        this.carteiraDigitalRepositorio = carteiraDigitalRepositorio;
        this.cartaoApiExterna = cartaoApiExterna;
    }

    @PostMapping("/carteira/cartao/{cartao}")
    public @ResponseBody ResponseEntity<CarteiraResposta> associar(@PathVariable("cartao") final String cartao, @RequestBody @Valid final CarteiraRequisicao carteiraRequisicao, final UriComponentsBuilder uriComponentsBuilder){
        final CarteiraResposta resposta = carteiraRequisicao
                .comCartao(cartao)
                .associar(cartaoRepositorio, carteiraDigitalRepositorio, cartaoApiExterna);
        return ResponseEntity
                .created(
                        uriComponentsBuilder
                                .path("/carteira/{id}")
                                .build( resposta.getId() )
                )
                .body( resposta );
    }

}
