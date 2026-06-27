import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { Profissional } from '../../models/models';

@Component({
  selector: 'app-profissionais',
  imports: [CommonModule, FormsModule],
  templateUrl: './profissionais.component.html'
})
export class ProfissionaisComponent implements OnInit {

  profissionais = signal<Profissional[]>([]);
  novo: Profissional = { nome: '', especialidade: '', registro: '' };
  busca = '';
  erro = signal<string>('');
  sucesso = signal<string>('');

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.api.listarProfissionais().subscribe({
      next: (lista) => this.profissionais.set(lista),
      error: () => this.erro.set('Nao foi possivel carregar os profissionais. A API esta no ar?')
    });
  }

  // Filtra por nome, especialidade, registro ou ID.
  profissionaisFiltrados(): Profissional[] {
    const termo = this.busca.trim().toLowerCase();
    if (!termo) return this.profissionais();
    return this.profissionais().filter(p =>
      p.nome.toLowerCase().includes(termo) ||
      p.especialidade.toLowerCase().includes(termo) ||
      p.registro.toLowerCase().includes(termo) ||
      String(p.id).includes(termo)
    );
  }

  cadastrar(): void {
    this.erro.set('');
    this.sucesso.set('');
    this.api.cadastrarProfissional(this.novo).subscribe({
      next: () => {
        this.sucesso.set('Profissional cadastrado com sucesso.');
        this.novo = { nome: '', especialidade: '', registro: '' };
        this.carregar();
      },
      error: (e) => this.erro.set(e?.error?.mensagem || 'Erro ao cadastrar profissional.')
    });
  }
}