package br.com.projeto.proposta.documento.validador;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@javax.validation.constraints.Pattern.List({@Pattern(
        regexp = "([0-9]{3}[.]?[0-9]{3}[.]?[0-9]{3}-[0-9]{2})|([0-9]{11})"
), @Pattern(
        regexp = "^(?:(?!000\\.?000\\.?000-?00).)*$"
), @Pattern(
        regexp = "^(?:(?!111\\.?111\\.?111-?11).)*$"
), @Pattern(
        regexp = "^(?:(?!222\\.?222\\.?222-?22).)*$"
), @Pattern(
        regexp = "^(?:(?!333\\.?333\\.?333-?33).)*$"
), @Pattern(
        regexp = "^(?:(?!444\\.?444\\.?444-?44).)*$"
), @Pattern(
        regexp = "^(?:(?!555\\.?555\\.?555-?55).)*$"
), @Pattern(
        regexp = "^(?:(?!666\\.?666\\.?666-?66).)*$"
), @Pattern(
        regexp = "^(?:(?!777\\.?777\\.?777-?77).)*$"
), @Pattern(
        regexp = "^(?:(?!888\\.?888\\.?888-?88).)*$"
), @Pattern(
        regexp = "^(?:(?!999\\.?999\\.?999-?99).)*$"
)})
@Documented
@Constraint(validatedBy = { CPFValidator.class })
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CPF {

    String message() default "Cpf invalido.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
