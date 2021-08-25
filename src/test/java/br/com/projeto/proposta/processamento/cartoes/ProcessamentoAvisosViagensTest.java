package br.com.projeto.proposta.processamento.cartoes;

import br.com.projeto.proposta.aviso.viagem.AvisoViagem;
import br.com.projeto.proposta.aviso.viagem.AvisoViagemRepositorio;
import br.com.projeto.proposta.aviso.viagem.StatusAvisoViagem;
import br.com.projeto.proposta.bloqueio.cartao.BloqueioCartao;
import br.com.projeto.proposta.bloqueio.cartao.BloqueioCartaoRepositorio;
import br.com.projeto.proposta.bloqueio.cartao.StatusBloqueio;
import br.com.projeto.proposta.cartao.sistema.legado.CartaoApiExterna;
import br.com.projeto.proposta.cartao.sistema.legado.aviso.viagem.AvisoViagemResposta;
import br.com.projeto.proposta.cartao.sistema.legado.bloquear.cartao.BloqueioResposta;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

public class ProcessamentoAvisosViagensTest {

    @Test
    @DisplayName("Falha de conexao sistema externo")
    void falhaConexao(){
        final CartaoApiExterna cartaoApiExternaMock = Mockito.mock( CartaoApiExterna.class );

        Mockito.when( cartaoApiExternaMock.criarAviso( Mockito.any(), Mockito.any() ) ).thenThrow(FeignException.class);

        final AvisoViagemRepositorio avisoViagemRepositorioMock = Mockito.mock(AvisoViagemRepositorio.class);

        final AvisoViagem avisoViagem = AvisoViagem.mock();

        Mockito.when(
                avisoViagemRepositorioMock
                        .findFirst10ByStatus( StatusAvisoViagem.NAO_CRIADO )
        ).thenReturn( List.of(avisoViagem) );

        Mockito.when(avisoViagemRepositorioMock.save(Mockito.any())).thenReturn( avisoViagem );

        new ProcessarAvisosViagens( avisoViagemRepositorioMock, cartaoApiExternaMock ).processar();

        Assertions.assertEquals( StatusAvisoViagem.NAO_CRIADO, avisoViagem.getStatus() );

    }

    @Test
    @DisplayName("Aviso viagem processado pelo sistema externo")
    void avisoViagemProcessadoSistemaExterno(){
        final CartaoApiExterna cartaoApiExternaMock = Mockito.mock( CartaoApiExterna.class );

        final AvisoViagemResposta avisoViagemResposta = new AvisoViagemResposta();
        avisoViagemResposta.setResultado( "CRIADO" );

        Mockito.when( cartaoApiExternaMock.criarAviso( Mockito.any(), Mockito.any() ) ).thenReturn( avisoViagemResposta );

        final AvisoViagemRepositorio avisoViagemRepositorioMock = Mockito.mock(AvisoViagemRepositorio.class);

        final AvisoViagem avisoViagem = AvisoViagem.mock();

        Mockito.when(
                avisoViagemRepositorioMock
                        .findFirst10ByStatus( StatusAvisoViagem.NAO_CRIADO )
        ).thenReturn( List.of(avisoViagem) );

        Mockito.when(avisoViagemRepositorioMock.save(Mockito.any())).thenReturn( avisoViagem );

        new ProcessarAvisosViagens( avisoViagemRepositorioMock, cartaoApiExternaMock ).processar();

        Assertions.assertEquals( StatusAvisoViagem.CRIADO, avisoViagem.getStatus() );
    }

    @Test
    @DisplayName("Aviso viagem nao processado pelo sistema externo")
    void avisoViagemNaoProcessadoSistemaExterno(){
        final CartaoApiExterna cartaoApiExternaMock = Mockito.mock( CartaoApiExterna.class );

        final AvisoViagemResposta avisoViagemResposta = new AvisoViagemResposta();
        avisoViagemResposta.setResultado( "FALHA" );

        Mockito.when( cartaoApiExternaMock.criarAviso( Mockito.any(), Mockito.any() ) ).thenReturn( avisoViagemResposta );

        final AvisoViagemRepositorio avisoViagemRepositorioMock = Mockito.mock(AvisoViagemRepositorio.class);

        final AvisoViagem avisoViagem = AvisoViagem.mock();

        Mockito.when(
                avisoViagemRepositorioMock
                        .findFirst10ByStatus( StatusAvisoViagem.NAO_CRIADO )
        ).thenReturn( List.of(avisoViagem) );

        Mockito.when(avisoViagemRepositorioMock.save(Mockito.any())).thenReturn( avisoViagem );

        new ProcessarAvisosViagens( avisoViagemRepositorioMock, cartaoApiExternaMock ).processar();

        Assertions.assertEquals( StatusAvisoViagem.NAO_CRIADO, avisoViagem.getStatus() );
    }

}
