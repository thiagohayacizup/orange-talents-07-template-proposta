package br.com.projeto.proposta.carteira;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
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
@AutoConfigureWireMock(port = 8082)
class CarteiraIntegrationTest {

    private static final String CARTEIRA_ENDPOINT = "/carteira/cartao";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Cartao associada a carteira com sucesso")
    @Sql( statements = {
            "INSERT INTO Cartao( numero ) VALUES ('9812-6534-0192-5342')"
    })
    void cartaoAssociadoAcarteiraSucesso() throws Exception{
        WireMock.stubFor(
                WireMock
                        .post("/api/cartoes/9812-6534-0192-5342/carteiras")
                        .withHeader("Content-Type", WireMock.equalTo(MediaType.APPLICATION_JSON_VALUE))
                        .willReturn(
                                WireMock
                                        .ok()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(
                                                Files.readString(
                                                        ResourceUtils
                                                                .getFile("classpath:br/com/projeto/proposta/carteira/carteira-resposta-legado.json")
                                                                .toPath()
                                                )
                                        )
                        )
        );

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post(CARTEIRA_ENDPOINT + "/9812-6534-0192-5342")
                        .contentType( MediaType.APPLICATION_JSON )
                        .content(
                                Files.readString(
                                        ResourceUtils
                                                .getFile("classpath:br/com/projeto/proposta/carteira/carteira-associada-sucesso.json")
                                                .toPath()
                                )
                        )
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isCreated() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.id").value(2L) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.carteira").value("PAYPAL") )
                .andExpect( MockMvcResultMatchers.jsonPath("$.status").value("ASSOCIADO") )
                .andExpect( MockMvcResultMatchers.header().string("Location", "http://localhost/carteira/2") );
    }

    @Test
    @DisplayName("Cartao associada a carteira com sucesso samsung pay")
    @Sql( statements = {
            "INSERT INTO Cartao( numero ) VALUES ('2222-2222-2222-2222')"
    })
    void cartaoAssociadoAcarteiraSucessoSamsungPay() throws Exception{
        WireMock.stubFor(
                WireMock
                        .post("/api/cartoes/2222-2222-2222-2222/carteiras")
                        .withHeader("Content-Type", WireMock.equalTo(MediaType.APPLICATION_JSON_VALUE))
                        .willReturn(
                                WireMock
                                        .ok()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(
                                                Files.readString(
                                                        ResourceUtils
                                                                .getFile("classpath:br/com/projeto/proposta/carteira/carteira-resposta-legado.json")
                                                                .toPath()
                                                )
                                        )
                        )
        );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(CARTEIRA_ENDPOINT + "/2222-2222-2222-2222")
                                .contentType( MediaType.APPLICATION_JSON )
                                .content(
                                        Files.readString(
                                                ResourceUtils
                                                        .getFile("classpath:br/com/projeto/proposta/carteira/carteira-associada-sucesso-samsung-pay.json")
                                                        .toPath()
                                        )
                                )
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isCreated() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.id").value(1L) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.carteira").value("SAMSUNG_PAY") )
                .andExpect( MockMvcResultMatchers.jsonPath("$.status").value("ASSOCIADO") )
                .andExpect( MockMvcResultMatchers.header().string("Location", "http://localhost/carteira/1") );
    }

    @Test
    @DisplayName("Cartao nao encontrado")
    void cartaoNaoEncontrado() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(CARTEIRA_ENDPOINT + "/9812-6534-0192-1111")
                                .contentType( MediaType.APPLICATION_JSON )
                                .content(
                                        Files.readString(
                                                ResourceUtils
                                                        .getFile("classpath:br/com/projeto/proposta/carteira/carteira-associada-sucesso.json")
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
    @DisplayName("Cartao ja associado a carteira")
    void cartaoAssociadoNovamente() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(CARTEIRA_ENDPOINT + "/9812-6534-0192-5342")
                                .contentType( MediaType.APPLICATION_JSON )
                                .content(
                                        Files.readString(
                                                ResourceUtils
                                                        .getFile("classpath:br/com/projeto/proposta/carteira/carteira-associada-sucesso.json")
                                                        .toPath()
                                        )
                                )
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isUnprocessableEntity() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.codigo").value(422) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.mensagem").value("Cartao { 9812-6534-0192-5342 } ja associado a carteira { PAYPAL } com status { ASSOCIADO }") );
    }

    @Test
    @DisplayName("Falha ao associar")
    @Sql( statements = {
            "INSERT INTO Cartao( numero ) VALUES ('9812-6534-0192-1111')"
    })
    void falhaAoAssociar() throws Exception {
        WireMock.stubFor(
                WireMock
                        .post("/api/cartoes/9812-6534-0192-1111/carteiras")
                        .withHeader("Content-Type", WireMock.equalTo(MediaType.APPLICATION_JSON_VALUE))
                        .willReturn(
                                WireMock
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY.value() )
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(
                                                Files.readString(
                                                        ResourceUtils
                                                                .getFile("classpath:br/com/projeto/proposta/carteira/carteira-resposta-legado-falha.json")
                                                                .toPath()
                                                )
                                        )
                        )
        );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(CARTEIRA_ENDPOINT + "/9812-6534-0192-1111")
                                .contentType( MediaType.APPLICATION_JSON )
                                .content(
                                        Files.readString(
                                                ResourceUtils
                                                        .getFile("classpath:br/com/projeto/proposta/carteira/carteira-associada-sucesso.json")
                                                        .toPath()
                                        )
                                )
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isUnprocessableEntity() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.codigo").value(422) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.mensagem").value("Cartao { 9812-6534-0192-1111 } nao pode ser associado.") );
    }

    @Test
    @DisplayName("Falha ao associar com resposta ok")
    @Sql( statements = {
            "INSERT INTO Cartao( numero ) VALUES ('1111-6534-0192-1111')"
    })
    void falhaAoAssociarComRespostaOk() throws Exception {
        WireMock.stubFor(
                WireMock
                        .post("/api/cartoes/1111-6534-0192-1111/carteiras")
                        .withHeader("Content-Type", WireMock.equalTo(MediaType.APPLICATION_JSON_VALUE))
                        .willReturn(
                                WireMock
                                        .ok()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(
                                                Files.readString(
                                                        ResourceUtils
                                                                .getFile("classpath:br/com/projeto/proposta/carteira/carteira-resposta-legado-falha.json")
                                                                .toPath()
                                                )
                                        )
                        )
        );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(CARTEIRA_ENDPOINT + "/1111-6534-0192-1111")
                                .contentType( MediaType.APPLICATION_JSON )
                                .content(
                                        Files.readString(
                                                ResourceUtils
                                                        .getFile("classpath:br/com/projeto/proposta/carteira/carteira-associada-sucesso.json")
                                                        .toPath()
                                        )
                                )
                ).andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isUnprocessableEntity() )
                .andExpect( MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.codigo").value(422) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.mensagem").value("Cartao { 1111-6534-0192-1111 } nao pode ser associado.") );
    }

}
