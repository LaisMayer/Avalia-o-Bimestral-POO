package br.edu.ifpr.todo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private final TarefaService service;

    public TarefaController(TarefaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarefaResponse criar(@Valid @RequestBody TarefaRequest dto) {
        Tarefa salvo = service.criar(dto);
        return new TarefaResponse(
                salvo.getId(), salvo.getNome(), salvo.getDescricao(),
                salvo.getStatus(), salvo.getDataCriacao(),
                salvo.getDataEntrega(), salvo.getImportante());
    }

    @GetMapping
    public List<Tarefa> listar(@RequestParam(required = false) String q,
            @RequestParam(required = false) TodoStatus status,
            @RequestParam(required = false) Boolean importante,
            @RequestParam(required = false) String ate) {
        LocalDate limite = (ate != null && !ate.isBlank()) ? LocalDate.parse(ate) : null;
        return service.listar(q, status, importante, limite);
    }

    @GetMapping("/{id}")
    public Tarefa buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PatchMapping("/{id}")
    public Tarefa atualizar(@PathVariable Long id, @RequestBody TarefaRequest dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
