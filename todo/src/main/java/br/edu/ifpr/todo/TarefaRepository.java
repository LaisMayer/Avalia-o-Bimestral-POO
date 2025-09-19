package br.edu.ifpr.todo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByStatus(TodoStatus status);

    List<Tarefa> findByImportanteTrue();

    List<Tarefa> findByStatusAndImportanteTrue(TodoStatus status);

    List<Tarefa> findByDataEntregaBefore(LocalDate limite);

    List<Tarefa> findByNomeContainingIgnoreCase(String q);
}
