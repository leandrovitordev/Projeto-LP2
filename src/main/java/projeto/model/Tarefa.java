package projeto.model;

/**
 * Esta é a classe Modelo (Model) que representa
 * a estrutura de dados de uma Tarefa.
 */
public class Tarefa {

    private Long id;
    private String titulo;
    private StatusTarefa status;
    private PrioridadeTarefa prioridade;
    private String responsavel;

    /**
     * Construtor vazio.
     * Necessário para a biblioteca Jackson (JSON) funcionar corretamente.
     */
    public Tarefa() {
    }
    
    // --- Getters e Setters ---
    // Métodos públicos necessários para que o Jackson
    // possa ler e escrever os dados desta classe.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public StatusTarefa getStatus() {
        return status;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status;
    }

    public PrioridadeTarefa getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(PrioridadeTarefa prioridade) {
        this.prioridade = prioridade;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }
}