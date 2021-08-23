package br.com.projeto.proposta.biometria;

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
public class BiometriaIntegrationTest {

    private static final String BIOMETRIA_ENDPOINT = "/biometria/cartao";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Biometria cadastrada sucesso")
    @Sql( statements = {
            "INSERT INTO Cartao( numero ) VALUES ('9812-6534-0192-5342')"
    })
    void biometriaCadastradaSucesso() throws Exception{
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(BIOMETRIA_ENDPOINT + "/9812-6534-0192-5342")
                                .contentType( MediaType.APPLICATION_JSON )
                                .content("{\"fingerprint\":\"RmluZ2VycHJpbnQgYmlvbWV0cmlhIHBhcmEgY2FydGFv\"}")
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isCreated() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.id").value(1L) );
    }

    @Test
    @DisplayName("Fingerprint não pode ser branco")
    @Sql( statements = {
            "INSERT INTO Cartao( numero ) VALUES ('3456-3456-3465-3456')"
    })
    void fingerprintNaoPodeSerBranco() throws Exception{
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(BIOMETRIA_ENDPOINT + "/3456-3456-3465-3456")
                                .contentType( MediaType.APPLICATION_JSON )
                                .content("{\"fingerprint\":\"\"}")
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isBadRequest() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.codigo").value(400) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.mensagem").value("Fingerprint nao pode ser branco ou nulo.") );
    }

    @Test
    @DisplayName("Fingerprint não pode ser null")
    @Sql( statements = {
            "INSERT INTO Cartao( numero ) VALUES ('7890-7890-7890-7890')"
    })
    void fingerprintNaoPodeSerNull() throws Exception{
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(BIOMETRIA_ENDPOINT + "/7890-7890-7890-7890")
                                .contentType( MediaType.APPLICATION_JSON )
                                .content("{\"fingerprint\":null}")
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isBadRequest() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.codigo").value(400) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.mensagem").value("Fingerprint nao pode ser branco ou nulo.") );
    }

    @Test
    @DisplayName("Cartao nao encontrado")
    void cartaoNaoEncontrado() throws Exception{
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(BIOMETRIA_ENDPOINT + "/1231-1412-1412-1438")
                                .contentType( MediaType.APPLICATION_JSON )
                                .content("{\"fingerprint\":\"RmluZ2VycHJpbnQgYmlvbWV0cmlhIHBhcmEgY2FydGFv\"}")
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isNotFound() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.codigo").value(404) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.mensagem").value("Cartao com numero { 1231-1412-1412-1438 } nao foi encontrado.") );
    }

}
