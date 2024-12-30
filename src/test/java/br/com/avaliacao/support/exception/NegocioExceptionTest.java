package br.com.avaliacao.support.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class NegocioExceptionTest {

    @Test
    void dadoMensagemEHttpStatus_quandoCriarNegocioException_entaoRetornaMensagemECodigo() {
        String mensagem = "Erro de negócio";
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        NegocioException exception = new NegocioException(mensagem, httpStatus);

        assertEquals(mensagem, exception.getMessage());
        assertEquals(httpStatus, exception.getHttpStatus());
    }

    @Test
    void dadoExcecaoLancada_quandoObterMensagem_entaoMensagemCorreta() {
        String mensagem = "Erro esperado";
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        NegocioException exception = assertThrows(NegocioException.class,
                () -> { throw new NegocioException(mensagem, httpStatus); });

        assertEquals(mensagem, exception.getMessage());
        assertEquals(httpStatus, exception.getHttpStatus());
    }
}