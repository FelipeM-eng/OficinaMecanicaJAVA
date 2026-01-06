public class CarroComercial extends Carro {
    private double capacidadeCarga; // em kg

    public CarroComercial() {
        super();
    }

    public CarroComercial(String marca, String modelo, int ano, String matricula, double capacidadeCarga) {
        super(marca, modelo, ano, matricula);
        setCapacidadeCarga(capacidadeCarga);
    }

    public double getCapacidadeCarga() {
        return capacidadeCarga;
    }

    public void setCapacidadeCarga(double capacidadeCarga) {
        if (capacidadeCarga > 0) {
            this.capacidadeCarga = capacidadeCarga;
        } else {
            throw new IllegalArgumentException("Capacidade de carga deve ser positiva.");
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", capacidadeCarga=" + capacidadeCarga + "kg (Comercial)";
    }
}