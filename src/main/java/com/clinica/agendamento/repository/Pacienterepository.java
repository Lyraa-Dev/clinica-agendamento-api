package com.clinica.agendamento.repository;
 
import com.clinica.agendamento.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Repository do Paciente, ao estender JpaRepository<Paciente, Long>, esta interface já herda os metodos
// Como estamos usando Spring Data JPA, não precisamos implementar os metodos, o Spring Data JPA faz isso automaticamente
// o primeiro parametro é paciente, o segundo é o tipo da chave primaria da entidade, que no caso é Long.

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // metodo para buscar paciente por cpf, retornando um Optional<Paciente> para tratar o caso de não encontrar o paciente
    Optional<Paciente> findByCpf(String cpf);
    
    // util para validar a duplicidade de cpf no cadastro do paciente
    boolean existsByCpf(String cpf);
    
}
