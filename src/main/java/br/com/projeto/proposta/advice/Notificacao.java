package br.com.projeto.proposta.advice;

import br.com.projeto.proposta.proposta.exception.EmailInvalidoException;
import br.com.projeto.proposta.proposta.exception.PropostaComDocumentoJaCriadaException;
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
        return new RespostaErro( 400, first.get().getMessage() );
    }

    @ExceptionHandler({EmailInvalidoException.class})
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    RespostaErro emailInvalido( final EmailInvalidoException exception ){
        return new RespostaErro(400, exception.getMessage() );
    }

    @ExceptionHandler({PropostaComDocumentoJaCriadaException.class})
    @ResponseStatus( HttpStatus.UNPROCESSABLE_ENTITY )
    RespostaErro propostaComDocumentoJaCadastrada( final PropostaComDocumentoJaCriadaException exception ){
        return new RespostaErro(422, exception.getMessage() );
    }

}
