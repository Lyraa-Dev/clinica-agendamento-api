// Interfaces que espelham os DTOs da API (tipagem forte).
// Garantem que o editor avise sobre campos errados em tempo de desenvolvimento.

export interface Paciente {
  id?: number;
  nome: string;
  cpf: string;
  telefone?: string;
}

export interface Profissional {
  id?: number;
  nome: string;
  especialidade: string;
  registro: string;
}

// Status possiveis - espelha o enum do back-end.
export type StatusAgendamento = 'AGENDADO' | 'CANCELADO' | 'REALIZADO';

// Enviado ao criar um agendamento (Abordagem A: so os IDs).
export interface AgendamentoRequest {
  pacienteId: number;
  profissionalId: number;
  dataHora: string; // formato ISO: 2026-08-15T14:30:00
  tipoAtendimento: string;
}

// Recebido da API ao listar/criar agendamentos.
export interface Agendamento {
  id: number;
  pacienteId: number;
  pacienteNome: string;
  profissionalId: number;
  profissionalNome: string;
  dataHora: string;
  tipoAtendimento: string;
  status: StatusAgendamento;
  motivoCancelamento?: string;
}