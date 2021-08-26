package br.com.projeto.proposta.carteira;

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
import org.springframework.util.ResourceUtils;

import java.nio.file.Files;

@ActiveProfiles( value = "test" )
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext( classMode = DirtiesContext.ClassMode.BEFORE_CLASS )
class CarteiraServidorExternoCaiuTest {

    private static final String CARTEIRA_ENDPOINT = "/carteira/cartao";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Servidor caiu")
    @Sql( statements = {
            "INSERT INTO Cartao( numero ) VALUES ('9812-6534-1111-1111')"
    })
    void servidorCaiu() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(CARTEIRA_ENDPOINT + "/9812-6534-1111-1111")
                                .contentType( MediaType.APPLICATION_JSON )
                                .content(
                                        Files.readString(
                                                ResourceUtils
                                                        .getFile("classpath:br/com/projeto/proposta/carteira/carteira-associada-sucesso.json")
                                                        .toPath()
                                        )
                                )
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isServiceUnavailable() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.codigo").value(503) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.mensagem").value("Cartao { 9812-6534-1111-1111 } nao pode ser associado, sistema indisponivel.") );
    }

}
