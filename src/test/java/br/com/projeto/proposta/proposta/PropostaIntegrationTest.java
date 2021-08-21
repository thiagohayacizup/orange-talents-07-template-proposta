package br.com.projeto.proposta.proposta;

import br.com.projeto.proposta.analise.financeira.AnaliseFinanceira;
import br.com.projeto.proposta.analise.financeira.AnaliseFinanceiraResposta;
import br.com.projeto.proposta.analise.financeira.ResultadoSolicitacao;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;

import java.nio.file.Files;

@ActiveProfiles( value = "test" )
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext( classMode = DirtiesContext.ClassMode.BEFORE_CLASS )
class PropostaIntegrationTest {

    private static final String PROPOSTA_ENDPOINT = "/proposta";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Proposta Criada Sucesso.")
    void propostaCriadaSucesso() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(PROPOSTA_ENDPOINT)
                                .contentType( MediaType.APPLICATION_JSON )
                                .content(
                                        Files.readString(
                                                ResourceUtils
                                                        .getFile("classpath:br/com/projeto/proposta/proposta/proposta-cadastrada-sucesso.json")
                                                        .toPath()
                                        )
                                )
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isCreated() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.id").value(2L) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.nome").value("proposta teste") )
                .andExpect( MockMvcResultMatchers.header().string("Location", "http://localhost/proposta/2") );
    }

    @Test
    @DisplayName("Proposta com documento invalido")
    void propostaComDocumentoInvalido() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(PROPOSTA_ENDPOINT)
                                .contentType( MediaType.APPLICATION_JSON )
                                .content(
                                        Files.readString(
                                                ResourceUtils
                                                        .getFile("classpath:br/com/projeto/proposta/proposta/proposta-documento-invalido.json")
                                                        .toPath()
                                        )
                                )
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isBadRequest() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.codigo").value(400) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.mensagem").value("Documento invalido.") );
    }

    @Test
    @DisplayName("Proposta Com Email Invalido")
    void propostaComEmailInvalido() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(PROPOSTA_ENDPOINT)
                                .contentType( MediaType.APPLICATION_JSON )
                                .content(
                                        Files.readString(
                                                ResourceUtils
                                                        .getFile("classpath:br/com/projeto/proposta/proposta/proposta-email-invalido.json")
                                                        .toPath()
                                        )
                                )
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isBadRequest() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.codigo").value(400) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.mensagem").value("Email { @ } com formato invalido.") );
    }

    @Test
    @DisplayName("Proposta nao pode ter salario negativo")
    void propostaComSalarioNegativo() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(PROPOSTA_ENDPOINT)
                                .contentType( MediaType.APPLICATION_JSON )
                                .content(
                                        Files.readString(
                                                ResourceUtils
                                                        .getFile("classpath:br/com/projeto/proposta/proposta/proposta-salario-invalido.json")
                                                        .toPath()
                                        )
                                )
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isBadRequest() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.codigo").value(400) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.mensagem").value("Salario nao pode ser negativo.") );
    }

    @Test
    @DisplayName("Proposta com documento ja cadastrado")
    @Sql(statements = {
            "INSERT INTO Proposta(documento, email, nome, endereco, salario )" +
            "VALUES('959.807.330-00', 'email@email.com', 'Proposta', 'rua abc', 100.50)"
    })
    void propostaComDocumentoJaCadastrado() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(PROPOSTA_ENDPOINT)
                                .contentType( MediaType.APPLICATION_JSON )
                                .content(
                                        Files.readString(
                                                ResourceUtils
                                                        .getFile("classpath:br/com/projeto/proposta/proposta/proposta-ja-criada.json")
                                                        .toPath()
                                        )
                                )
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isUnprocessableEntity() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.codigo").value(422) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.mensagem").value("Ja existe uma proposta com o documento { 959.807.330-00 }.") );
    }

    @Test
    @DisplayName("Analise de solicitaçao com restrição")
    void analiseSolicitacaoComRestricao(){
        final AnaliseFinanceira analiseFinanceiraMock = Mockito.mock(AnaliseFinanceira.class);

        final AnaliseFinanceiraResposta resposta = new AnaliseFinanceiraResposta();
        resposta.setResultadoSolicitacao( ResultadoSolicitacao.COM_RESTRICAO );

        Mockito.when( analiseFinanceiraMock.solicitar( Mockito.any() ) ).thenReturn( resposta );

        final PropostaRepositorio propostaRepositorioMock = Mockito.mock(PropostaRepositorio.class);

        final Proposta proposta = Proposta.mock();

        Mockito.when( propostaRepositorioMock.save( Mockito.any() ) ).thenReturn( proposta );

        proposta.criar( propostaRepositorioMock, analiseFinanceiraMock );

        Assertions.assertEquals( StatusProposta.NAO_ELEGIVEL, proposta.getStatus() );
    }

    @Test
    @DisplayName("Analise de solicitaçao sem restrição")
    void analiseSolicitacaoSemRestricao(){
        final AnaliseFinanceira analiseFinanceiraMock = Mockito.mock(AnaliseFinanceira.class);

        final AnaliseFinanceiraResposta resposta = new AnaliseFinanceiraResposta();
        resposta.setResultadoSolicitacao( ResultadoSolicitacao.SEM_RESTRICAO );

        Mockito.when( analiseFinanceiraMock.solicitar( Mockito.any() ) ).thenReturn( resposta );

        final PropostaRepositorio propostaRepositorioMock = Mockito.mock(PropostaRepositorio.class);

        final Proposta proposta = Proposta.mock();

        Mockito.when( propostaRepositorioMock.save( Mockito.any() ) ).thenReturn( proposta );

        proposta.criar( propostaRepositorioMock, analiseFinanceiraMock );

        Assertions.assertEquals( StatusProposta.ELEGIVEL, proposta.getStatus() );
    }

    @Test
    @DisplayName("Falha de Conexão Sistema externo")
    void falhaConexao(){
        final AnaliseFinanceira analiseFinanceiraMock = Mockito.mock(AnaliseFinanceira.class);

        Mockito.when( analiseFinanceiraMock.solicitar( Mockito.any() ) ).thenThrow(FeignException.class);

        final PropostaRepositorio propostaRepositorioMock = Mockito.mock(PropostaRepositorio.class);

        final Proposta proposta = Proposta.mock();

        Mockito.when( propostaRepositorioMock.save( Mockito.any() ) ).thenReturn( proposta );

        proposta.criar( propostaRepositorioMock, analiseFinanceiraMock );

        Assertions.assertNull( proposta.getStatus() );
    }

}
