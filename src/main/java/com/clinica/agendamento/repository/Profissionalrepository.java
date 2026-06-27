package com.clinica.agendamento.repository;

import com.clinica.agendamento.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

// repository do profissional, ao estender JpaRepository<Profissional, Long>, esta interface herda os metodos 

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    
    // metodo para validar a duplicidade de registro no cadastro do profissional através do registro (CRM etc)
    boolean existsByRegistro(String registro);
}
