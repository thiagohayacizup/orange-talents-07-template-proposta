package br.com.projeto.proposta.documento.validador;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@Pattern(regexp = "([0-9]{2}[.]?[0-9]{3}[.]?[0-9]{3}[/]?[0-9]{4}[-]?[0-9]{2})")
@Documented
@Constraint(validatedBy = { CNPJValidator.class })
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CNPJ {

    String message() default "Cnpj invalido.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
