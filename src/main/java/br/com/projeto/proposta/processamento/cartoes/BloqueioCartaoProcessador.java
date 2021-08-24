package br.com.projeto.proposta.processamento.cartoes;

import br.com.projeto.proposta.bloqueio.cartao.BloqueioCartao;
import br.com.projeto.proposta.bloqueio.cartao.BloqueioCartaoRepositorio;
import br.com.projeto.proposta.cartao.sistema.legado.CartaoApiExterna;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BloqueioCartaoProcessador {

    private final BloqueioCartaoRepositorio bloqueioCartaoRepositorio;

    private final CartaoApiExterna cartaoApiExterna;

    public BloqueioCartaoProcessador(final BloqueioCartaoRepositorio bloqueioCartaoRepositorio, final CartaoApiExterna cartaoApiExterna) {
        this.bloqueioCartaoRepositorio = bloqueioCartaoRepositorio;
        this.cartaoApiExterna = cartaoApiExterna;
    }

    private final Logger logger = LoggerFactory.getLogger(ProcessarCartoesProposta.class);

    @Scheduled( initialDelayString = "${scheduler.initial.delay}", fixedDelayString = "${scheduler.fixed.delay}" )
    void processar(){

        try{
            BloqueioCartao
                    .buscarCartoesNaoBloqueados( bloqueioCartaoRepositorio )
                    .forEach( bloqueioCartao -> bloqueioCartao.bloquear( cartaoApiExterna, bloqueioCartaoRepositorio ) );
        }catch (FeignException exception){
            logger.error(exception.getMessage());
        }

    }

}
