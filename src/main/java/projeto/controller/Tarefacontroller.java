package projeto.controller;

import projeto.exception.TarefaNaoEncontradaException; // Import corrigido
import projeto.model.Tarefa; // Import corrigido
import projeto.service.TarefaService; // Import corrigido

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projeto.model.StatusTarefa;
import projeto.model.PrioridadeTarefa;

import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tarefas") // URL base para a API
public class Tarefacontroller {

    private final TarefaService tarefaService;

    @Autowired
    public Tarefacontroller(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

@GetMapping
public List<Tarefa> listarTodasTarefas(
        @RequestParam(required = false) StatusTarefa status,
        @RequestParam(required = false) PrioridadeTarefa prioridade) {

    return tarefaService.listarTodas(status, prioridade);
}

    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarTarefaPorId(@PathVariable Long id) {
        Tarefa tarefa = tarefaService.buscarPorId(id);
        return ResponseEntity.ok(tarefa);
    }

    @PostMapping
    public ResponseEntity<Tarefa> criarTarefa(@RequestBody @Valid Tarefa novaTarefa)  {
        Tarefa tarefaSalva = tarefaService.criarTarefa(novaTarefa);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefaSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizarTarefa(@PathVariable Long id, @RequestBody @Valid Tarefa tarefaAtualizada) {
        Tarefa tarefa = tarefaService.atualizarTarefa(id, tarefaAtualizada);
        return ResponseEntity.ok(tarefa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long id) {
        tarefaService.deletarTarefa(id);
        return ResponseEntity.noContent().build();
    }

/* @ExceptionHandler(TarefaNaoEncontradaException.class)
    public ResponseEntity<String> handleTarefaNaoEncontrada(TarefaNaoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }*/ 
}
