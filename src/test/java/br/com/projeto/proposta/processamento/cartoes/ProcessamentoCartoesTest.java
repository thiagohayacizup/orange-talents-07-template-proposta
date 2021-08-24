package br.com.projeto.proposta.processamento.cartoes;

import br.com.projeto.proposta.cartao.sistema.legado.CartaoApiExterna;
import br.com.projeto.proposta.cartao.sistema.legado.criar.cartao.CartaoResposta;
import br.com.projeto.proposta.proposta.Proposta;
import br.com.projeto.proposta.proposta.PropostaRepositorio;
import br.com.projeto.proposta.proposta.StatusProposta;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class ProcessamentoCartoesTest {

    @Test
    @DisplayName("Falha de conexao sistema externo")
    void falhaConexao(){
        final CartaoApiExterna cartaoApiExternaMock = Mockito.mock( CartaoApiExterna.class );

        Mockito.when( cartaoApiExternaMock.criarCartao( Mockito.any() ) ).thenThrow(FeignException.class);

        final PropostaRepositorio propostaRepositorioMock = Mockito.mock(PropostaRepositorio.class);

        final Proposta proposta = Proposta.mockCartao();

        Mockito.when(
                propostaRepositorioMock
                        .findFirst10ByStatusAndCartaoIsNull( StatusProposta.ELEGIVEL )
        ).thenReturn( List.of(proposta) );

        Mockito.when(propostaRepositorioMock.save(Mockito.any())).thenReturn( proposta );

        new ProcessarCartoesProposta( propostaRepositorioMock, cartaoApiExternaMock).processar();

        Assertions.assertEquals("", proposta.getNumeroCartao());
    }

    @Test
    @DisplayName("Cartao adicionado status elegivel")
    void cartaoAdicionado(){
        final CartaoApiExterna cartaoApiExternaMock = Mockito.mock( CartaoApiExterna.class );

        final String numeroCartao = "1234-1234-1234-1234";

        final CartaoResposta resposta = new CartaoResposta();
        resposta.setId( numeroCartao );

        Mockito.when( cartaoApiExternaMock.criarCartao( Mockito.any() ) ).thenReturn( resposta );

        final PropostaRepositorio propostaRepositorioMock = Mockito.mock(PropostaRepositorio.class);

        final Proposta proposta = Proposta.mockCartao();

        Mockito.when(
                propostaRepositorioMock
                        .findFirst10ByStatusAndCartaoIsNull( StatusProposta.ELEGIVEL )
        ).thenReturn( List.of(proposta) );

        Mockito.when(propostaRepositorioMock.save(Mockito.any())).thenReturn( proposta );

        new ProcessarCartoesProposta( propostaRepositorioMock, cartaoApiExternaMock).processar();

        Assertions.assertEquals( numeroCartao, proposta.getNumeroCartao() );

    }

}
