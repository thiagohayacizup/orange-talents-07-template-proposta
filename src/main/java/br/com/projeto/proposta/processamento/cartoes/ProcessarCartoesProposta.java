package br.com.projeto.proposta.processamento.cartoes;

import br.com.projeto.proposta.cartao.sistema.legado.CartaoCriacao;
import br.com.projeto.proposta.proposta.Proposta;
import br.com.projeto.proposta.proposta.PropostaRepositorio;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class ProcessarCartoesProposta {

    private final PropostaRepositorio propostaRepositorio;

    private final CartaoCriacao cartaoCriacao;

    ProcessarCartoesProposta(final PropostaRepositorio propostaRepositorio, final CartaoCriacao cartaoCriacao) {
        this.propostaRepositorio = propostaRepositorio;
        this.cartaoCriacao = cartaoCriacao;
    }

    private final Logger logger = LoggerFactory.getLogger(ProcessarCartoesProposta.class);

    @Scheduled( initialDelayString = "${scheduler.initial.delay}", fixedDelayString = "${scheduler.fixed.delay}" )
    void processar(){

        try{
            Proposta
                    .buscarPropostarSemCartoes( propostaRepositorio )
                    .forEach( proposta -> proposta.associarCartao(cartaoCriacao, propostaRepositorio) );
        }catch (FeignException exception){
            logger.error(exception.getMessage());
        }

    }

}
