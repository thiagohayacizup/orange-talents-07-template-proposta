package br.com.projeto.proposta.processamento.cartoes;

import br.com.projeto.proposta.bloqueio.cartao.BloqueioCartao;
import br.com.projeto.proposta.bloqueio.cartao.BloqueioCartaoRepositorio;
import br.com.projeto.proposta.bloqueio.cartao.StatusBloqueio;
import br.com.projeto.proposta.cartao.sistema.legado.CartaoApiExterna;
import br.com.projeto.proposta.cartao.sistema.legado.bloquear.cartao.BloqueioResposta;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class ProcessamentoBloqueioCartoesTest {

    @Test
    @DisplayName("Falha de conexao sistema externo")
    void falhaConexao(){
        final CartaoApiExterna cartaoApiExternaMock = Mockito.mock( CartaoApiExterna.class );

        Mockito.when( cartaoApiExternaMock.bloquear( Mockito.any(), Mockito.any() ) ).thenThrow(FeignException.class);

        final BloqueioCartaoRepositorio bloqueioCartaoRepositorioMock = Mockito.mock(BloqueioCartaoRepositorio.class);

        final BloqueioCartao bloqueioCartao = BloqueioCartao.mock();

        Mockito.when(
                bloqueioCartaoRepositorioMock
                        .findFirst10ByStatus( StatusBloqueio.NAO_BLOQUEADO )
        ).thenReturn( List.of(bloqueioCartao) );

        Mockito.when(bloqueioCartaoRepositorioMock.save(Mockito.any())).thenReturn( bloqueioCartao );

        new BloqueioCartaoProcessador( bloqueioCartaoRepositorioMock, cartaoApiExternaMock).processar();

        Assertions.assertEquals(StatusBloqueio.NAO_BLOQUEADO, bloqueioCartao.getStatusBloqueio());
    }

    @Test
    @DisplayName("Bloqueio processado pelo sistema externo")
    void bloqueioProcessadoSistemaExterno(){
        final CartaoApiExterna cartaoApiExternaMock = Mockito.mock( CartaoApiExterna.class );

        final BloqueioResposta bloqueioResposta = new BloqueioResposta();
        bloqueioResposta.setResultado( "BLOQUEADO" );

        Mockito.when( cartaoApiExternaMock.bloquear( Mockito.any(), Mockito.any() ) ).thenReturn( bloqueioResposta );

        final BloqueioCartaoRepositorio bloqueioCartaoRepositorioMock = Mockito.mock(BloqueioCartaoRepositorio.class);

        final BloqueioCartao bloqueioCartao = BloqueioCartao.mock();

        Mockito.when(
                bloqueioCartaoRepositorioMock
                        .findFirst10ByStatus( StatusBloqueio.NAO_BLOQUEADO )
        ).thenReturn( List.of(bloqueioCartao) );

        Mockito.when(bloqueioCartaoRepositorioMock.save(Mockito.any())).thenReturn( bloqueioCartao );

        new BloqueioCartaoProcessador( bloqueioCartaoRepositorioMock, cartaoApiExternaMock).processar();

        Assertions.assertEquals(StatusBloqueio.BLOQUEADO, bloqueioCartao.getStatusBloqueio());
    }

    @Test
    @DisplayName("Bloqueio nao processado pelo sistema externo")
    void bloqueioNaoProcessadoSistemaExterno(){
        final CartaoApiExterna cartaoApiExternaMock = Mockito.mock( CartaoApiExterna.class );

        final BloqueioResposta bloqueioResposta = new BloqueioResposta();
        bloqueioResposta.setResultado( "FALHA" );

        Mockito.when( cartaoApiExternaMock.bloquear( Mockito.any(), Mockito.any() ) ).thenReturn( bloqueioResposta );

        final BloqueioCartaoRepositorio bloqueioCartaoRepositorioMock = Mockito.mock(BloqueioCartaoRepositorio.class);

        final BloqueioCartao bloqueioCartao = BloqueioCartao.mock();

        Mockito.when(
                bloqueioCartaoRepositorioMock
                        .findFirst10ByStatus( StatusBloqueio.NAO_BLOQUEADO )
        ).thenReturn( List.of(bloqueioCartao) );

        Mockito.when(bloqueioCartaoRepositorioMock.save(Mockito.any())).thenReturn( bloqueioCartao );

        new BloqueioCartaoProcessador( bloqueioCartaoRepositorioMock, cartaoApiExternaMock).processar();

        Assertions.assertEquals(StatusBloqueio.NAO_BLOQUEADO, bloqueioCartao.getStatusBloqueio());
    }

}
