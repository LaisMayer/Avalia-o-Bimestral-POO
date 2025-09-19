package br.edu.ifpr.todo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TarefaService {

    private final TarefaRepository repo;

    public TarefaService(TarefaRepository repo) {
        this.repo = repo;
    }

    public List<Tarefa> listar(String q, TodoStatus status, Boolean importante, LocalDate ate) {
        if (q != null && !q.isBlank()) {
            return repo.findByNomeContainingIgnoreCase(q);
        }
        if (status != null && Boolean.TRUE.equals(importante)) {
            return repo.findByStatusAndImportanteTrue(status);
        }
        if (status != null) {
            return repo.findByStatus(status);
        }
        if (Boolean.TRUE.equals(importante)) {
            return repo.findByImportanteTrue();
        }
        if (ate != null) {
            return repo.findByDataEntregaBefore(ate.plusDays(1));
        }
        return repo.findAll();
    }

    public Tarefa buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa id=" + id + " não encontrada"));
    }

    @Transactional
    public Tarefa criar(TarefaRequest dto) {
        Tarefa t = new Tarefa();
        t.setNome(dto.getNome());
        t.setDescricao(dto.getDescricao());
        t.setStatus(dto.getStatus() != null ? dto.getStatus() : TodoStatus.A_FAZER);
        t.setDataEntrega(dto.getDataEntrega());
        t.setImportante(dto.getImportante() != null ? dto.getImportante() : false);
        return repo.save(t);
    }

    @Transactional
    public Tarefa atualizar(Long id, TarefaRequest dto) {
        Tarefa t = buscarPorId(id);
        t.setNome(dto.getNome());
        t.setDescricao(dto.getDescricao());
        if (dto.getStatus() != null) {
            t.setStatus(dto.getStatus());
        }
        t.setDataEntrega(dto.getDataEntrega());
        if (dto.getImportante() != null) {
            t.setImportante(dto.getImportante());
        }
        return repo.save(t);
    }

    @Transactional
    public void remover(Long id) {
        Tarefa t = buscarPorId(id);
        repo.delete(t);
    }
}
