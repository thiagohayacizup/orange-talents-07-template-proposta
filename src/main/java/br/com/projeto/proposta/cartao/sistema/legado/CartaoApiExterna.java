package br.com.projeto.proposta.cartao.sistema.legado;

import br.com.projeto.proposta.cartao.sistema.legado.associar.carteira.CarteiraRequisicao;
import br.com.projeto.proposta.cartao.sistema.legado.associar.carteira.CarteiraResposta;
import br.com.projeto.proposta.cartao.sistema.legado.aviso.viagem.AvisoViagemRequisicao;
import br.com.projeto.proposta.cartao.sistema.legado.aviso.viagem.AvisoViagemResposta;
import br.com.projeto.proposta.cartao.sistema.legado.bloquear.cartao.BloqueioRequisicao;
import br.com.projeto.proposta.cartao.sistema.legado.bloquear.cartao.BloqueioResposta;
import br.com.projeto.proposta.cartao.sistema.legado.criar.cartao.CartaoRequisicao;
import br.com.projeto.proposta.cartao.sistema.legado.criar.cartao.CartaoResposta;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(url = "${feign.cartoes}", name = "cartao")
public interface CartaoApiExterna {

    @PostMapping("/api/cartoes")
    @ResponseBody CartaoResposta criarCartao(@RequestBody final CartaoRequisicao cartaoRequisicao );

    @PostMapping("/api/cartoes/{id}/bloqueios")
    @ResponseBody BloqueioResposta bloquear(@PathVariable("id") final String id, @RequestBody final BloqueioRequisicao bloqueioRequisicao );

    @PostMapping("/api/cartoes/{id}/avisos")
    @ResponseBody AvisoViagemResposta criarAviso(@PathVariable("id") final String id, @RequestBody final AvisoViagemRequisicao avisoViagemRequisicao );

    @PostMapping("/api/cartoes/{id}/carteiras")
    @ResponseBody
    CarteiraResposta associar(@PathVariable("id") final String id, @RequestBody final CarteiraRequisicao carteiraRequisicao);


}