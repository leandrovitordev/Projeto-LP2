package projeto.repository;

import projeto.model.Tarefa; // Import corrigido
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TarefaRepository {

    private static final String ARQUIVO_DB = "tarefas.json";
    private final Path caminhoDoArquivo = Paths.get(ARQUIVO_DB);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Tarefa> tarefas = new CopyOnWriteArrayList<>();
    private final AtomicLong contadorId = new AtomicLong(0);

    @PostConstruct
    public void carregarDoArquivo() {
        objectMapper.registerModule(new JavaTimeModule());

        try {
            if (Files.exists(caminhoDoArquivo) && Files.size(caminhoDoArquivo) > 0) {
                String json = Files.readString(caminhoDoArquivo);
                List<Tarefa> tarefasCarregadas = objectMapper.readValue(json, new TypeReference<List<Tarefa>>() {});
                this.tarefas.addAll(tarefasCarregadas);

                long maxId = tarefasCarregadas.stream()
                                            .mapToLong(Tarefa::getId)
                                            .max()
                                            .orElse(0L);
                this.contadorId.set(maxId);
                
                System.out.println("Tarefas carregadas do arquivo: " + tarefas.size());
            } else {
                System.out.println("Arquivo " + ARQUIVO_DB + " não encontrado ou vazio. Iniciando com lista vazia.");
            }
        } catch (IOException e) {
            System.err.println("Falha fatal ao carregar o arquivo de banco de dados: " + e.getMessage());
            throw new RuntimeException("Não foi possível carregar o banco de dados de tarefas.", e);
        }
    }

    private synchronized void salvarNoArquivo() {
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.tarefas);
            Files.writeString(caminhoDoArquivo, json);
        } catch (IOException e) {
            System.err.println("Falha ao salvar dados no arquivo: " + e.getMessage());
        }
    }

    public List<Tarefa> findAll() {
        return new ArrayList<>(this.tarefas);
    }

    public Optional<Tarefa> findById(Long id) {
        return this.tarefas.stream()
                        .filter(t -> t.getId() != null && t.getId().equals(id))
                        .findFirst();
    }

    public Tarefa save(Tarefa tarefa) {
        if (tarefa.getId() == null) {
            tarefa.setId(contadorId.incrementAndGet());
            this.tarefas.add(tarefa);
        } else {
            this.tarefas.removeIf(t -> t.getId() != null && t.getId().equals(tarefa.getId()));
            this.tarefas.add(tarefa);
        }
        salvarNoArquivo();
        return tarefa;
    }

    public void deleteById(Long id) {
        boolean removido = this.tarefas.removeIf(t -> t.getId() != null && t.getId().equals(id));
        if (removido) {
            salvarNoArquivo();
        }
    }
}