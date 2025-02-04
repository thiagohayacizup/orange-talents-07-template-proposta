package br.com.projeto.proposta.analise.financeira;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(url = "${feign.analise.financeira.solicitacao}", name = "analise-financeira")
public interface AnaliseFinanceira {

    @PostMapping("/api/solicitacao")
    @ResponseBody AnaliseFinanceiraResposta solicitar(@RequestBody final AnaliseFinanceiraRequisicao analiseFinanceiraRequisicao );

}
