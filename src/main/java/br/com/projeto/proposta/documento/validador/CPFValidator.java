package br.com.projeto.proposta.documento.validador;

import br.com.projeto.proposta.documento.Documento;
import br.com.projeto.proposta.documento.TipoDocumento;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class CPFValidator implements ConstraintValidator<CPF, String> {

    @Override
    public boolean isValid(final String cpf_input, final ConstraintValidatorContext constraintValidatorContext) {
        final String cpf = cpf_input.replaceAll("\\.", "").replaceAll("-", "");
        if( !Documento.tamanhoValido( cpf, TipoDocumento.CPF ) ) return false;
        return Documento
                        .de( cpf )
                        .tipo( TipoDocumento.CPF )
                        .validarPrimeiroDigito()
                        .validarSegundoDigito()
                        .eValido();
    }

}
