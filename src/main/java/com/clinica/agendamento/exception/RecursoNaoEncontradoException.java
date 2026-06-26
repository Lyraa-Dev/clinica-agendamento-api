package com.clinica.agendamento.exception;

//Exceção para quando um recurso não é encontrado, como por exemplo, tentar buscar um paciente ou profissional que não existe no banco de dados
// será traduzida em uma resposta HTTP 404 Not Found, informando ao cliente que o recurso solicitado não foi encontrado.
public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
 