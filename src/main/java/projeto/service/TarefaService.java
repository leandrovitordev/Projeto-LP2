package projeto.service;

import projeto.exception.TarefaNaoEncontradaException; // Import corrigido
import projeto.model.StatusTarefa; // Import corrigido
import projeto.model.Tarefa; // Import corrigido
import projeto.repository.TarefaRepository; // Import corrigido
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto.model.PrioridadeTarefa;
import projeto.model.StatusTarefa;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    @Autowired
    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }


public List<Tarefa> listarTodas(StatusTarefa status, PrioridadeTarefa prioridade) {
    // 1. Busca todas as tarefas do arquivo JSON
    List<Tarefa> todas = tarefaRepository.findAll();

    // 2. Aplica a filtragem usando Java Streams
    return todas.stream()
            .filter(t -> status == null || t.getStatus() == status)
            .filter(t -> prioridade == null || t.getPrioridade() == prioridade)
            .collect(Collectors.toList());
}

    public Tarefa buscarPorId(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa com ID " + id + " não encontrada."));
    }

    public Tarefa criarTarefa(Tarefa novaTarefa) {
        // Regra de Negócio: Toda nova tarefa começa como PENDENTE
        novaTarefa.setStatus(StatusTarefa.PENDENTE);
        return tarefaRepository.save(novaTarefa);
    }

    public Tarefa atualizarTarefa(Long id, Tarefa tarefaAtualizada) {
        Tarefa tarefaExistente = buscarPorId(id);

        // Atualiza os campos
        tarefaExistente.setTitulo(tarefaAtualizada.getTitulo());
        tarefaExistente.setPrioridade(tarefaAtualizada.getPrioridade());
        tarefaExistente.setResponsavel(tarefaAtualizada.getResponsavel());
        tarefaExistente.setStatus(tarefaAtualizada.getStatus());

        return tarefaRepository.save(tarefaExistente);
    }

    public void deletarTarefa(Long id) {
        buscarPorId(id); // Verifica se existe antes de deletar
        tarefaRepository.deleteById(id);
    }
}