public class Servico {
    private static int contadorId = 0;
    private int id;
    private String descricao;
    private double custo;

    public Servico() {
    }

    public Servico(String descricao, double custo) {
        this();
        setDescricao(descricao);
        setCusto(custo);
        this.id = ++contadorId;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao != null && !descricao.trim().isEmpty()) {
            this.descricao = descricao;
        } else {
            throw new IllegalArgumentException("Descrição não pode ser vazia.");
        }
    }

    public double getCusto() {
        return custo;
    }

    public void setCusto(double custo) {
        if (custo >= 0) {
            this.custo = custo;
        } else {
            throw new IllegalArgumentException("Custo deve ser não negativo.");
        }
    }

    @Override
    public String toString() {
        return "Servico{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", custo=" + custo +
                '}';
    }
}