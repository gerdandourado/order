package br.com.avaliacao.config;

import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ValidationConfigTest {

    @InjectMocks
    private ValidationConfig validationConfig;

    @Test
    void dadoBeanValidator_quandoCriarValidator_entaoRetornaInstanciaDeLocalValidatorFactoryBean() {
        Validator validator = validationConfig.validator();
        assertNotNull(validator);
        assertNotNull((LocalValidatorFactoryBean) validator);
    }
}