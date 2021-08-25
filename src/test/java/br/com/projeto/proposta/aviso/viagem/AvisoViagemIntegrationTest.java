package br.com.projeto.proposta.aviso.viagem;

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
class AvisoViagemIntegrationTest {

    private static final String AVISO_VIAGEM_ENDPOINT = "/aviso-viagem/cartao";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Aviso viagem registrado sucesso")
    @Sql( statements = {
            "INSERT INTO Cartao( numero ) VALUES ('9812-6534-0192-5342')"
    })
    void avisoViagemRegistradoSucesso() throws Exception{
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(AVISO_VIAGEM_ENDPOINT + "/9812-6534-0192-5342")
                                .contentType( MediaType.APPLICATION_JSON )
                                .header("User-Agent", "Mozilla Firefox")
                                .header("ip", "127.0.0.1")
                                .content(
                                        Files.readString(
                                                ResourceUtils
                                                        .getFile("classpath:br/com/projeto/proposta/aviso/viagem/aviso-viagem-registrado-sucesso.json")
                                                        .toPath()
                                        )
                                )
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.id").value(1L) );
    }

    @Test
    @DisplayName("Cartao nao encontrado")
    void cartaoNaoEncontrado() throws Exception{
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(AVISO_VIAGEM_ENDPOINT + "/9812-6534-0192-1111")
                                .contentType( MediaType.APPLICATION_JSON )
                                .header("User-Agent", "Mozilla Firefox")
                                .header("ip", "127.0.0.1")
                                .content(
                                        Files.readString(
                                                ResourceUtils
                                                        .getFile("classpath:br/com/projeto/proposta/aviso/viagem/aviso-viagem-registrado-sucesso.json")
                                                        .toPath()
                                        )
                                )
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isNotFound() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.codigo").value(404) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.mensagem").value("Cartao com numero { 9812-6534-0192-1111 } nao foi encontrado.") );
    }

    @Test
    @DisplayName("Destino não pode ser vazio")
    @Sql( statements = {
            "INSERT INTO Cartao( numero ) VALUES ('1111-1111-0192-5342')"
    })
    void destinoNaoPodeSerVazio() throws Exception{
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(AVISO_VIAGEM_ENDPOINT + "/1111-1111-0192-5342")
                                .contentType( MediaType.APPLICATION_JSON )
                                .header("User-Agent", "Mozilla Firefox")
                                .header("ip", "127.0.0.1")
                                .content(
                                        Files.readString(
                                                ResourceUtils
                                                        .getFile("classpath:br/com/projeto/proposta/aviso/viagem/aviso-viagem-destino-vazio.json")
                                                        .toPath()
                                        )
                                )
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isBadRequest() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.codigo").value(400) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.mensagem").value("Destino nao pode ser branco ou nulo.") );
    }

    @Test
    @DisplayName("Data de termino nao pode estar no passado")
    @Sql( statements = {
            "INSERT INTO Cartao( numero ) VALUES ('1111-1111-1111-5342')"
    })
    void dataTerminoNaoPodeEstarNoPassado() throws Exception{
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(AVISO_VIAGEM_ENDPOINT + "/1111-1111-1111-5342")
                                .contentType( MediaType.APPLICATION_JSON )
                                .header("User-Agent", "Mozilla Firefox")
                                .header("ip", "127.0.0.1")
                                .content(
                                        Files.readString(
                                                ResourceUtils
                                                        .getFile("classpath:br/com/projeto/proposta/aviso/viagem/aviso-viagem-data-termino-passado.json")
                                                        .toPath()
                                        )
                                )
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isBadRequest() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.codigo").value(400) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.mensagem").value("Data de termino nao pode estar no passado.") );
    }

}
