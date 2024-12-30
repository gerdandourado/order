package br.com.avaliacao.support.util;

public class CpfUtil {

    public static boolean isValidCPF(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false; // Checks if CPF is null, has a different length than 11, or is a repeated sequence.
        }

        try {
            int[] digits = new int[11];
            for (int i = 0; i < 11; i++) {
                digits[i] = Integer.parseInt(cpf.substring(i, i + 1));
            }

            // First verification digit validation
            int sum1 = 0;
            for (int i = 0; i < 9; i++) {
                sum1 += digits[i] * (10 - i);
            }
            int digit1 = 11 - (sum1 % 11);
            if (digit1 == 10 || digit1 == 11) {
                digit1 = 0;
            }
            if (digit1 != digits[9]) {
                return false;
            }

            // Second verification digit validation
            int sum2 = 0;
            for (int i = 0; i < 10; i++) {
                sum2 += digits[i] * (11 - i);
            }
            int digit2 = 11 - (sum2 % 11);
            if (digit2 == 10 || digit2 == 11) {
                digit2 = 0;
            }
            if (digit2 != digits[10]) {
                return false;
            }

            return true;

        } catch (NumberFormatException e) {
            return false; // CPF contains non-numeric characters.
        }
    }
}
