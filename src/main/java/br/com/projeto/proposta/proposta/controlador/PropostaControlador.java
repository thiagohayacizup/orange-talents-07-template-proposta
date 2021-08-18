package br.com.projeto.proposta.proposta.controlador;

import br.com.projeto.proposta.proposta.PropostaRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
class PropostaControlador {

    private final PropostaRepositorio propostaRepositorio;

    PropostaControlador(final PropostaRepositorio propostaRepositorio) {
        this.propostaRepositorio = propostaRepositorio;
    }

    @PostMapping("/proposta")
    @ResponseBody ResponseEntity<PropostaResposta> criar(@RequestBody @Valid final PropostaRequisicao propostaRequisicao, final UriComponentsBuilder uriComponentsBuilder ){
        final PropostaResposta propostaResposta = propostaRequisicao.criar( propostaRepositorio );
        return ResponseEntity
                .created(
                        uriComponentsBuilder
                                .path("/proposta/{id}")
                                .build( propostaResposta.getId() )
                )
                .body( propostaResposta );
    }

}
