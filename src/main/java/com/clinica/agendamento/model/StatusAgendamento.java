package com.clinica.agendamento.model;

// possiveis status de um agendamento
// a decisão de usar enum para representar os status de um agendamento para evitar possiveis erros de digitação e dados 
// inconsistentes no banco de dados, garantindo que apenas os valores definidos no enum possam ser utilizados como status de 
// agendamento. 

public enum StatusAgendamento {
    AGENDADO,
    CANCELADO,
    REALIZADO
}
