public class CarroPasseio extends Carro {
    private int numeroPassageiros;

    public CarroPasseio() {
        super();
    }

    public CarroPasseio(String marca, String modelo, int ano, String matricula, int numeroPassageiros) {
        super(marca, modelo, ano, matricula);
        setNumeroPassageiros(numeroPassageiros);
    }

    public int getNumeroPassageiros() {
        return numeroPassageiros;
    }

    public void setNumeroPassageiros(int numeroPassageiros) {
        if (numeroPassageiros > 0) {
            this.numeroPassageiros = numeroPassageiros;
        } else {
            throw new IllegalArgumentException("Número de passageiros deve ser positivo.");
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", numeroPassageiros=" + numeroPassageiros + " (Passeio)";
    }
}