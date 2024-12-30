package br.com.avaliacao.support.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CpfUtilTest {

    @Test
    void dadoCpfValido_quandoIsValidCPF_entaoRetornaTrue() {
        String cpfValido = "12345678909";
        boolean resultado = CpfUtil.isValidCPF(cpfValido);
        assertTrue(resultado);
    }

    @Test
    void dadoCpfInvalidoPorDigitosVerificadores_quandoIsValidCPF_entaoRetornaFalse() {
        String cpfInvalido = "12345678901";
        boolean resultado = CpfUtil.isValidCPF(cpfInvalido);
        assertFalse(resultado);
    }

    @Test
    void dadoCpfComCaracteresInvalidos_quandoIsValidCPF_entaoRetornaFalse() {
        String cpfInvalido = "12345678abc";
        boolean resultado = CpfUtil.isValidCPF(cpfInvalido);
        assertFalse(resultado);
    }

    @Test
    void dadoCpfNulo_quandoIsValidCPF_entaoRetornaFalse() {
        String cpfNulo = null;
        boolean resultado = CpfUtil.isValidCPF(cpfNulo);
        assertFalse(resultado);
    }

    @Test
    void dadoCpfComTamanhoInvalido_quandoIsValidCPF_entaoRetornaFalse() {
        String cpfInvalidoTamanho = "12345678";
        boolean resultado = CpfUtil.isValidCPF(cpfInvalidoTamanho);
        assertFalse(resultado);
    }

    @Test
    void dadoCpfComNumerosRepetidos_quandoIsValidCPF_entaoRetornaFalse() {
        String cpfInvalidoRepetidos = "11111111111";
        boolean resultado = CpfUtil.isValidCPF(cpfInvalidoRepetidos);
        assertFalse(resultado);
    }
}