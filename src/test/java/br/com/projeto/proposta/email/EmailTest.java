package br.com.projeto.proposta.email;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailTest {

    @Test
    @DisplayName("Email com sintaxe valida")
    void emailSintaxeValido(){
        Assertions.assertTrue( Email.match( "nome.pessoa@email.com" ) );
    }

    @Test
    @DisplayName("Email com sintaxe invalida")
    void emailSintaxeInvalida(){
        Assertions.assertFalse( Email.match( "@" ) );
    }

}
