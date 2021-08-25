package br.com.projeto.proposta.bloqueio.cartao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

@ActiveProfiles( value = "test" )
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext( classMode = DirtiesContext.ClassMode.BEFORE_CLASS )
class BloqueioCartaoIntegrationTest {

    private static final String BLOQUEIO_ENDPOINT = "/bloqueio/cartao";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Bloqueio cartao solicitado sucesso")
    @Sql( statements = {
            "INSERT INTO Cartao( numero ) VALUES ('9812-6534-0192-5342')"
    })
    void bloqueioCartaoSolicitadoSucesso() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(BLOQUEIO_ENDPOINT + "/9812-6534-0192-5342")
                                .contentType( MediaType.APPLICATION_JSON )
                                .header("User-Agent", "Mozilla Firefox")
                                .header("ip", "127.0.0.1")
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.id").value(1L) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.status").value("NAO_BLOQUEADO") )
                .andExpect( MockMvcResultMatchers.jsonPath("$.mensagem").value("Aguarde enquanto processamos o bloqueio do seu cartao") );
    }

    @Test
    @DisplayName("Bloqueio cartao solicitado ja bloqueado")
    @Sql( statements = {
            "INSERT INTO Cartao( numero ) VALUES ('3333-4444-5555-6666')",
            "INSERT INTO Bloqueio_Cartao ( status, cartao_id, ip, user_agent ) VALUES ( 'BLOQUEADO', 2, '10.10.10.10', 'Firefox' )"
    })
    void bloqueioCartaoSolicitadoSucessoJaBloqueado() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(BLOQUEIO_ENDPOINT + "/3333-4444-5555-6666")
                                .contentType( MediaType.APPLICATION_JSON )
                                .header("User-Agent", "Mozilla Firefox")
                                .header("ip", "127.0.0.1")
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isUnprocessableEntity() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.codigo").value("422") )
                .andExpect( MockMvcResultMatchers.jsonPath("$.mensagem").value("Bloqueio do cartao { 3333-4444-5555-6666 } status { BLOQUEADO }.") );
    }

    @Test
    @DisplayName("Cartao nao encontrado")
    void cartaoNaoEncontrado() throws Exception{
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(BLOQUEIO_ENDPOINT + "/1231-1412-1412-1438")
                                .contentType( MediaType.APPLICATION_JSON )
                                .header("User-Agent", "Mozilla Firefox")
                                .header("ip", "127.0.0.1")
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isNotFound() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.codigo").value(404) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.mensagem").value("Cartao com numero { 1231-1412-1412-1438 } nao foi encontrado.") );
    }

}
