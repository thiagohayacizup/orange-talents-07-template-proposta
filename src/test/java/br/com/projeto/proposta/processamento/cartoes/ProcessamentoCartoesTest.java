package br.com.projeto.proposta.processamento.cartoes;

import br.com.projeto.proposta.analise.financeira.AnaliseFinanceira;
import br.com.projeto.proposta.cartao.Cartao;
import br.com.projeto.proposta.cartao.CartaoResposta;
import br.com.projeto.proposta.proposta.Proposta;
import br.com.projeto.proposta.proposta.PropostaRepositorio;
import br.com.projeto.proposta.proposta.StatusProposta;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

public class ProcessamentoCartoesTest {

    @Test
    @DisplayName("Falha de conexao sistema externo")
    void falhaConexao(){
        final Cartao cartaoMock = Mockito.mock( Cartao.class );

        Mockito.when( cartaoMock.criarCartao( Mockito.any() ) ).thenThrow(FeignException.class);

        final PropostaRepositorio propostaRepositorioMock = Mockito.mock(PropostaRepositorio.class);

        final Proposta proposta = Proposta.mockCartao();

        Mockito.when(
                propostaRepositorioMock
                        .findFirst10ByStatusAndNumeroCartaoIsNull( StatusProposta.ELEGIVEL )
        ).thenReturn( List.of(proposta) );

        Mockito.when(propostaRepositorioMock.save(Mockito.any())).thenReturn( proposta );

        new ProcessarCartoesProposta( propostaRepositorioMock, cartaoMock ).processar();

        Assertions.assertNull(proposta.getNumeroCartao());
    }

    @Test
    @DisplayName("Cartao adicionado status elegivel")
    void cartaoAdicionado(){
        final Cartao cartaoMock = Mockito.mock( Cartao.class );

        final String numeroCartao = "1234-1234-1234-1234";

        final CartaoResposta resposta = new CartaoResposta();
        resposta.setId( numeroCartao );

        Mockito.when( cartaoMock.criarCartao( Mockito.any() ) ).thenReturn( resposta );

        final PropostaRepositorio propostaRepositorioMock = Mockito.mock(PropostaRepositorio.class);

        final Proposta proposta = Proposta.mockCartao();

        Mockito.when(
                propostaRepositorioMock
                        .findFirst10ByStatusAndNumeroCartaoIsNull( StatusProposta.ELEGIVEL )
        ).thenReturn( List.of(proposta) );

        Mockito.when(propostaRepositorioMock.save(Mockito.any())).thenReturn( proposta );

        new ProcessarCartoesProposta( propostaRepositorioMock, cartaoMock ).processar();

        Assertions.assertEquals( numeroCartao, proposta.getNumeroCartao() );

    }

}
