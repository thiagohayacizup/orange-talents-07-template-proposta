package br.com.projeto.proposta.advice;

import br.com.projeto.proposta.bloqueio.cartao.excessao.BloqueioCartaoJaSolicitadoOuJaBloqueadoException;
import br.com.projeto.proposta.cartao.excessao.CartaoNaoEncontradoException;
import br.com.projeto.proposta.proposta.exception.EmailInvalidoException;
import br.com.projeto.proposta.proposta.exception.PropostaComDocumentoJaCriadaException;
import br.com.projeto.proposta.proposta.exception.PropostaNaoEncontradaException;
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

    @ExceptionHandler({PropostaNaoEncontradaException.class})
    @ResponseStatus( HttpStatus.NOT_FOUND )
    RespostaErro propostaNaoEncontrada( final PropostaNaoEncontradaException exception ){
        return new RespostaErro(404, exception.getMessage() );
    }

    @ExceptionHandler({CartaoNaoEncontradoException.class})
    @ResponseStatus( HttpStatus.NOT_FOUND )
    RespostaErro cartaoNaoEncontrado( final CartaoNaoEncontradoException exception ){
        return new RespostaErro(404, exception.getMessage() );
    }

    @ExceptionHandler({BloqueioCartaoJaSolicitadoOuJaBloqueadoException.class})
    @ResponseStatus( HttpStatus.UNPROCESSABLE_ENTITY )
    RespostaErro bloqueioJaOperadoOuSolicitado( final BloqueioCartaoJaSolicitadoOuJaBloqueadoException exception ){
        return new RespostaErro(422, exception.getMessage() );
    }

}
