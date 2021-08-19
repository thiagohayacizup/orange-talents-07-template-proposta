package br.com.projeto.proposta.proposta.controlador;

import br.com.projeto.proposta.analise.financeira.AnaliseFinanceira;
import br.com.projeto.proposta.proposta.Proposta;
import br.com.projeto.proposta.proposta.PropostaRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
class PropostaControlador {

    private final PropostaRepositorio propostaRepositorio;

    private final AnaliseFinanceira analiseFinanceira;

    PropostaControlador(final PropostaRepositorio propostaRepositorio, final AnaliseFinanceira analiseFinanceira) {
        this.propostaRepositorio = propostaRepositorio;
        this.analiseFinanceira = analiseFinanceira;
    }

    private final Logger logger = LoggerFactory.getLogger(PropostaControlador.class);

    @PostMapping("/proposta")
    @ResponseBody ResponseEntity<PropostaResposta> criar(@RequestBody @Valid final PropostaRequisicao propostaRequisicao, final UriComponentsBuilder uriComponentsBuilder ){
        final PropostaResposta propostaResposta = propostaRequisicao.criar( propostaRepositorio, analiseFinanceira );
        logger.info("Proposta criada com sucesso.");
        return ResponseEntity
                .created(
                        uriComponentsBuilder
                                .path("/proposta/{id}")
                                .build( propostaResposta.getId() )
                )
                .body( propostaResposta );
    }

}
