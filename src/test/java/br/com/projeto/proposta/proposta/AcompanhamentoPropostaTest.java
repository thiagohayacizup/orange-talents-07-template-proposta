package br.com.projeto.proposta.proposta;

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
class AcompanhamentoPropostaTest {

    private static final String PROPOSTA_ENDPOINT = "/proposta";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Proposta detalhada com sucesso")
    @Sql(statements = {
            "INSERT INTO proposta (documento, email, endereco, nome, salario, status) " +
            "VALUES ('594.556.720-57', 'email@dominio.com', 'Rua x', 'Proposta', 400.00, 'ELEGIVEL');"
    })
    void propostaDetalhada() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(PROPOSTA_ENDPOINT + "/1")
                                .contentType( MediaType.APPLICATION_JSON )
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.documento").value("594.556.720-57") )
                .andExpect( MockMvcResultMatchers.jsonPath("$.email").value("email@dominio.com") )
                .andExpect( MockMvcResultMatchers.jsonPath("$.endereco").value("Rua x") )
                .andExpect( MockMvcResultMatchers.jsonPath("$.nome").value("Proposta") )
                .andExpect( MockMvcResultMatchers.jsonPath("$.salario").value(400.00 ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.status").value("ELEGIVEL") );
    }

    @Test
    @DisplayName("Proposta nao encontrada")
    void propostaNaoEncontrada() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(PROPOSTA_ENDPOINT + "/50")
                                .contentType( MediaType.APPLICATION_JSON )
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isNotFound() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.codigo").value(404) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.mensagem").value("Proposta com id { 50 } nao encontrada.") );
    }

}
