import java.util.List;

public class Fatura {
    private static int contadorId = 1;
    private int id;
    private Carro carro;
    private List<Servico> servicos;
    private double total;

    public Fatura(Carro carro, List<Servico> servicos) {
        this.id = contadorId++;
        this.carro = carro;
        this.servicos = servicos;
        calcularTotal();
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public Carro getCarro() {
        return carro;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public double getTotal() {
        return total;
    }

    private void calcularTotal() {
        this.total = servicos.stream().mapToDouble(Servico::getCusto).sum();
    }

    @Override
    public String toString() {
        return "Fatura{" +
                "id=" + id +
                ", carro=" + carro.getMarca() + " " + carro.getModelo() +
                ", total=" + total +
                '}';
    }
}