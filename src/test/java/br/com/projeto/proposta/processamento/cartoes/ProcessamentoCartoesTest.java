package br.com.projeto.proposta.processamento.cartoes;

import br.com.projeto.proposta.cartao.sistema.legado.CartaoCriacao;
import br.com.projeto.proposta.cartao.sistema.legado.CartaoResposta;
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
        final CartaoCriacao cartaoCriacaoMock = Mockito.mock( CartaoCriacao.class );

        Mockito.when( cartaoCriacaoMock.criarCartao( Mockito.any() ) ).thenThrow(FeignException.class);

        final PropostaRepositorio propostaRepositorioMock = Mockito.mock(PropostaRepositorio.class);

        final Proposta proposta = Proposta.mockCartao();

        Mockito.when(
                propostaRepositorioMock
                        .findFirst10ByStatusAndCartaoIsNull( StatusProposta.ELEGIVEL )
        ).thenReturn( List.of(proposta) );

        Mockito.when(propostaRepositorioMock.save(Mockito.any())).thenReturn( proposta );

        new ProcessarCartoesProposta( propostaRepositorioMock, cartaoCriacaoMock).processar();

        Assertions.assertEquals("", proposta.getNumeroCartao());
    }

    @Test
    @DisplayName("Cartao adicionado status elegivel")
    void cartaoAdicionado(){
        final CartaoCriacao cartaoCriacaoMock = Mockito.mock( CartaoCriacao.class );

        final String numeroCartao = "1234-1234-1234-1234";

        final CartaoResposta resposta = new CartaoResposta();
        resposta.setId( numeroCartao );

        Mockito.when( cartaoCriacaoMock.criarCartao( Mockito.any() ) ).thenReturn( resposta );

        final PropostaRepositorio propostaRepositorioMock = Mockito.mock(PropostaRepositorio.class);

        final Proposta proposta = Proposta.mockCartao();

        Mockito.when(
                propostaRepositorioMock
                        .findFirst10ByStatusAndCartaoIsNull( StatusProposta.ELEGIVEL )
        ).thenReturn( List.of(proposta) );

        Mockito.when(propostaRepositorioMock.save(Mockito.any())).thenReturn( proposta );

        new ProcessarCartoesProposta( propostaRepositorioMock, cartaoCriacaoMock).processar();

        Assertions.assertEquals( numeroCartao, proposta.getNumeroCartao() );

    }

}
