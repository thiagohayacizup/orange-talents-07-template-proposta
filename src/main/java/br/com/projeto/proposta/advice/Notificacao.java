package br.com.projeto.proposta.advice;

import br.com.projeto.proposta.proposta.exception.EmailInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.Set;

@RestControllerAdvice
public class Notificacao {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    RespostaErro dadosInvalidos( final MethodArgumentNotValidException exception ){
        return new RespostaErro(400, exception.getFieldErrors().get(0).getDefaultMessage() );
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    RespostaErro dadosInvalidos( final ConstraintViolationException exception ){
        final Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        final Optional<ConstraintViolation<?>> first = violations.stream().findFirst();
        if( first.isEmpty() )
            return new RespostaErro( 400, exception.getMessage());
        return new RespostaErro( 400, first.get().getMessage() );
    }

    @ExceptionHandler({EmailInvalidoException.class})
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    RespostaErro emailInvalido( final EmailInvalidoException exception ){
        return new RespostaErro(400, exception.getMessage() );
    }

}
