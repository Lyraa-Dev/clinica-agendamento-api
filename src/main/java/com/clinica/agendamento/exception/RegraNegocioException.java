package com.clinica.agendamento.exception;

// exceção para possiveis violações de regras de negocio, como por exemplo, tentar agendar um atendimento para um profissional que 
// já possui outro agendamento no mesmo horário ou CPF duplicado no cadastro de paciente.
// estende RuntimeException para ser uma exceção não verificada, que não precisa ser declarada na assinatura do método e pode ser lançada em qualquer ponto do código.

public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
