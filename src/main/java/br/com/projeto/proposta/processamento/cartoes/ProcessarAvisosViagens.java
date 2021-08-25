package br.com.projeto.proposta.processamento.cartoes;

import br.com.projeto.proposta.aviso.viagem.AvisoViagem;
import br.com.projeto.proposta.aviso.viagem.AvisoViagemRepositorio;
import br.com.projeto.proposta.bloqueio.cartao.BloqueioCartao;
import br.com.projeto.proposta.cartao.sistema.legado.CartaoApiExterna;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class ProcessarAvisosViagens {

    private final AvisoViagemRepositorio avisoViagemRepositorio;

    private final CartaoApiExterna cartaoApiExterna;

    ProcessarAvisosViagens(final AvisoViagemRepositorio avisoViagemRepositorio, final CartaoApiExterna cartaoApiExterna) {
        this.avisoViagemRepositorio = avisoViagemRepositorio;
        this.cartaoApiExterna = cartaoApiExterna;
    }

    private final Logger logger = LoggerFactory.getLogger(ProcessarAvisosViagens.class);

    @Scheduled( initialDelayString = "${scheduler.initial.delay}", fixedDelayString = "${scheduler.fixed.delay}" )
    void processar(){

        try{
            AvisoViagem
                    .buscarAvisosViagemStatusNaoCriadosSistemaExterno( avisoViagemRepositorio )
                    .forEach( avisoViagem -> avisoViagem.notificar( cartaoApiExterna, avisoViagemRepositorio ) );
        }catch (FeignException exception){
            logger.error(exception.getMessage());
        }

    }

}
