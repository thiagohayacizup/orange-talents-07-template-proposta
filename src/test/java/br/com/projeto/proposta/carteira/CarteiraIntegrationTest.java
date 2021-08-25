package br.com.projeto.proposta.carteira;

import br.com.projeto.proposta.cartao.sistema.legado.CartaoApiExterna;
import br.com.projeto.proposta.cartao.sistema.legado.associar.carteira.CarteiraResposta;
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
class CarteiraIntegrationTest {

    private static final String CARTEIRA_ENDPOINT = "/carteira/cartao";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Carteira associada com sucesso")
    @Sql( statements = {
            "INSERT INTO Cartao( numero ) VALUES ('9812-6534-0192-5342')"
    })
    void carteiraAssociadaSucesso() throws Exception{
        final CartaoApiExterna cartaoApiExternaMock = Mockito.mock(CartaoApiExterna.class);

        final CarteiraResposta carteiraResposta = new CarteiraResposta();
        carteiraResposta.setId("123456-fgdhfr4t3342-3435tgfur-356rygfs");
        carteiraResposta.setResultado("ASSOCIADO");

        Mockito
                .when( cartaoApiExternaMock.associar( Mockito.anyString(), Mockito.any() ) )
                .thenReturn( carteiraResposta );

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
                .andExpect( MockMvcResultMatchers.jsonPath("$.id").value(1L) )
                .andExpect( MockMvcResultMatchers.jsonPath("$.carteira").value("PAYPAL") )
                .andExpect( MockMvcResultMatchers.jsonPath("$.status").value("ASSOCIADO") );
    }


}
