package br.com.projeto.proposta.cartao;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(url = "${feign.cartoes}", name = "cartao")
public interface Cartao {

    @PostMapping("/api/cartoes")
    @ResponseBody CartaoResposta criarCartao( @RequestBody final CartaoRequisicao cartaoRequisicao );

}