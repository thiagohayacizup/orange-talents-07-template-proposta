package br.com.projeto.proposta.processamento.cartoes;

import br.com.projeto.proposta.cartao.Cartao;
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

    private final Cartao cartao;

    ProcessarCartoesProposta(final PropostaRepositorio propostaRepositorio, final Cartao cartao) {
        this.propostaRepositorio = propostaRepositorio;
        this.cartao = cartao;
    }

    private final Logger logger = LoggerFactory.getLogger(ProcessarCartoesProposta.class);

    @Scheduled( initialDelay = 5000, fixedDelay = 5000 )
    void processar(){

        try{
            Proposta
                    .buscarPropostarSemCartoes( propostaRepositorio )
                    .forEach( proposta -> proposta.associarCartao( cartao, propostaRepositorio) );
        }catch (FeignException exception){
            logger.error(exception.getMessage());
        }

    }

}
