package br.com.projeto.proposta.documento.validador;

import br.com.projeto.proposta.documento.Documento;
import br.com.projeto.proposta.documento.TipoDocumento;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CNPJValidator implements ConstraintValidator<CNPJ, String> {

    @Override
    public boolean isValid(final String cnpj_input, final ConstraintValidatorContext constraintValidatorContext) {
        final String cnpj = cnpj_input.replaceAll("\\.", "").replaceAll("-", "").replaceAll("/","");
        if( !Documento.tamanhoValido( cnpj, TipoDocumento.CNPJ ) ) return false;
        return Documento
                        .de( cnpj )
                        .tipo( TipoDocumento.CNPJ )
                        .validarPrimeiroDigito()
                        .validarSegundoDigito()
                        .eValido();
    }

}
