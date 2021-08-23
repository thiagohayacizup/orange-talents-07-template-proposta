package br.com.projeto.proposta.biometria.controlador;

import br.com.projeto.proposta.biometria.BiometriaRepositorio;
import br.com.projeto.proposta.cartao.CartaoRepositorio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
class BiometriaControlador {

    private final BiometriaRepositorio biometriaRepositorio;

    private final CartaoRepositorio cartaoRepositorio;

    BiometriaControlador(final BiometriaRepositorio biometriaRepositorio, final CartaoRepositorio cartaoRepositorio) {
        this.biometriaRepositorio = biometriaRepositorio;
        this.cartaoRepositorio = cartaoRepositorio;
    }

    @PostMapping("/biometria/cartao/{cartao}")
    public @ResponseBody ResponseEntity<BiometriaResposta> cadastrar(@PathVariable("cartao") final String cartao, @RequestBody @Valid final BiometriaRequisicao biometriaRequisicao, final UriComponentsBuilder uriComponentsBuilder){
        final BiometriaResposta resposta = biometriaRequisicao.cadastrar(cartao, cartaoRepositorio, biometriaRepositorio);
        return ResponseEntity
                .created(
                        uriComponentsBuilder
                                .path("/biometria/{id}")
                                .build( resposta.getId() )
                )
                .body( resposta );
    }

}
