package br.com.avaliacao.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class NegocioException extends Exception {

    @Getter
    private final HttpStatus httpStatus;
    private String causa;

    public NegocioException(String mensagem, HttpStatus httpStatus) {
        super(mensagem);
        this.httpStatus = httpStatus;
    }

}
