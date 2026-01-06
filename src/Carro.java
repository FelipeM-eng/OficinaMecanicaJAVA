import java.util.ArrayList;
import java.util.List;

public class Carro {
    private static int contadorId = 0;
    private int id;
    private String marca;
    private String modelo;
    private int ano;
    private String matricula;
    private List<Servico> servicos;

    // Construtor padrão
    public Carro() {
        this.servicos = new ArrayList<>();
    }

    // Construtor sobrecarregado
    public Carro(String marca, String modelo, int ano, String matricula) {
        this();
        setMarca(marca);
        setModelo(modelo);
        setAno(ano);
        setMatricula(matricula);
        this.id = ++contadorId;
    }

    // Getters e Setters com validação
    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        if (marca != null && !marca.trim().isEmpty()) {
            this.marca = marca;
        } else {
            throw new IllegalArgumentException("Marca não pode ser vazia.");
        }
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        if (modelo != null && !modelo.trim().isEmpty()) {
            this.modelo = modelo;
        } else {
            throw new IllegalArgumentException("Modelo não pode ser vazio.");
        }
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        if (ano > 1900 && ano <= 2025) {
            this.ano = ano;
        } else {
            throw new IllegalArgumentException("Ano inválido.");
        }
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        if (matricula != null && matricula.matches("[A-Z]{2}-[0-9]{2}-[0-9]{2}")) {
            this.matricula = matricula;
        } else {
            throw new IllegalArgumentException("Matrícula deve estar no formato AA-00-00.");
        }
    }

    public void adicionarServico(Servico servico) {
        if (servico != null) {
            this.servicos.add(servico);
        }
    }

    public List<Servico> getServicos() {
        return new ArrayList<>(servicos); // Retorna uma cópia para encapsulamento
    }

    @Override
    public String toString() {
        return "Carro{" +
                "id=" + id +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", ano=" + ano +
                ", matricula='" + matricula + '\'' +
                '}';
    }
}