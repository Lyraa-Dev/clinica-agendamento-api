import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  Paciente, Profissional, Agendamento, AgendamentoRequest, StatusAgendamento
} from '../models/models';

/**
 * Service central de comunicacao com a API.
 *
 * Todos os componentes usam este service para falar com o back-end -
 * eles nunca chamam HttpClient diretamente. Isso centraliza a logica
 * de acesso a API num lugar so (mesma ideia da camada Repository no back).
 *
 * 'providedIn: root' faz o Angular criar uma unica instancia compartilhada.
 */
@Injectable({ providedIn: 'root' })
export class ApiService {

  // URL base da API. Em producao, viria de um arquivo de environment.
  private readonly baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  // ---------- Pacientes ----------
  listarPacientes(): Observable<Paciente[]> {
    return this.http.get<Paciente[]>(`${this.baseUrl}/pacientes`);
  }

  cadastrarPaciente(paciente: Paciente): Observable<Paciente> {
    return this.http.post<Paciente>(`${this.baseUrl}/pacientes`, paciente);
  }

  // ---------- Profissionais ----------
  listarProfissionais(): Observable<Profissional[]> {
    return this.http.get<Profissional[]>(`${this.baseUrl}/profissionais`);
  }

  cadastrarProfissional(profissional: Profissional): Observable<Profissional> {
    return this.http.post<Profissional>(`${this.baseUrl}/profissionais`, profissional);
  }

  // ---------- Agendamentos ----------
  // Filtros opcionais: so sao enviados quando preenchidos.
  listarAgendamentos(filtros?: {
    pacienteId?: number;
    profissionalId?: number;
    status?: StatusAgendamento;
  }): Observable<Agendamento[]> {
    let params = new HttpParams();
    if (filtros?.pacienteId) params = params.set('pacienteId', filtros.pacienteId);
    if (filtros?.profissionalId) params = params.set('profissionalId', filtros.profissionalId);
    if (filtros?.status) params = params.set('status', filtros.status);
    return this.http.get<Agendamento[]>(`${this.baseUrl}/agendamentos`, { params });
  }

  criarAgendamento(req: AgendamentoRequest): Observable<Agendamento> {
    return this.http.post<Agendamento>(`${this.baseUrl}/agendamentos`, req);
  }

  cancelarAgendamento(id: number, motivo: string): Observable<Agendamento> {
    return this.http.patch<Agendamento>(
      `${this.baseUrl}/agendamentos/${id}/cancelamento`, { motivo });
  }
}