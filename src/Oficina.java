import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class Oficina {
    private List<Carro> carros;
    private List<Servico> servicos;
    private List<Fatura> faturas;

    public Oficina() {
        this.carros = new ArrayList<>();
        this.servicos = new ArrayList<>();
        this.faturas = new ArrayList<>();
    }

    // Métodos para Carros

    // Adiciona carro
    public void adicionarCarro(Carro carro) {
        carros.add(carro);
        try {
            salvarDados("dados.csv");
        } catch (IOException e) {
        }
    }

    // Remove carro
    public void removerCarro(int id) {
        carros.removeIf(c -> c.getId() == id);
        try {
            salvarDados("dados.csv");
        } catch (IOException e) {
        }
    }

    public Carro procurarCarro(int id) {
        return carros.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public List<Carro> listarCarros() {
        return new ArrayList<>(carros);
    }

    // Métodos para Serviços

    // Adiciona serviço
    public void adicionarServico(Servico servico) {
        servicos.add(servico);
        try {
            salvarDados("dados.csv");
        } catch (IOException e) {
        }
    }

    public void removerServico(int id) {
        servicos.removeIf(s -> s.getId() == id);
        try {
            salvarDados("dados.csv");
        } catch (IOException e) {
        }
    }

    public Servico procurarServico(int id) {
        return servicos.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }

    public List<Servico> listarServicos() {
        return new ArrayList<>(servicos);
    }

    // Métodos para Faturas
    public void adicionarFatura(Fatura fatura) {
        faturas.add(fatura);
        try {
            salvarDados("dados.csv");
        } catch (IOException e) {
        }
    }

    public List<Fatura> listarFaturas() {
        return new ArrayList<>(faturas);
    }

    // Atribuir serviço a carro
    public void atribuirServicoACarro(int idCarro, int idServico) {
        Carro carro = procurarCarro(idCarro);
        Servico servico = procurarServico(idServico);
        if (carro != null && servico != null) {
            carro.adicionarServico(servico);
        }
    }

    // Gerar fatura para um carro
    public Fatura gerarFatura(int idCarro) {
        Carro carro = procurarCarro(idCarro);
        if (carro != null && !carro.getServicos().isEmpty()) {
            Fatura fatura = new Fatura(carro, carro.getServicos());
            adicionarFatura(fatura);
            return fatura;
        }
        return null;
    }

    // Salvar dados em ficheiro CSV
    public void salvarDados(String ficheiro) throws IOException {
        // Criar backup
        File original = new File(ficheiro);
        if (original.exists()) {
            File backup = new File(ficheiro.replace(".csv", "_backup.csv"));
            java.nio.file.Files.copy(original.toPath(), backup.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(ficheiro))) {
            writer.println("Carros");
            for (Carro c : carros) {
                writer.println(c.getId() + "," + c.getMarca() + "," + c.getModelo() + "," + c.getAno() + "," + c.getMatricula() + "," + (c instanceof CarroPasseio ? "Passeio," + ((CarroPasseio)c).getNumeroPassageiros() : "Comercial," + ((CarroComercial)c).getCapacidadeCarga()));
            }
            writer.println("Servicos");
            for (Servico s : servicos) {
                writer.println(s.getId() + "," + s.getDescricao() + "," + s.getCusto());
            }
            writer.println("Faturas");
            for (Fatura f : faturas) {
                writer.print(f.getId() + "," + f.getCarro().getId() + ",");
                writer.println(f.getServicos().stream().map(s -> String.valueOf(s.getId())).collect(Collectors.joining(";")));
            }
        }
    }

    // Carregar dados de ficheiro CSV
    public void carregarDados(String ficheiro) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            String secao = "";
            while ((linha = reader.readLine()) != null) {
                if (linha.equals("Carros") || linha.equals("Servicos") || linha.equals("Faturas")) {
                    secao = linha;
                    continue;
                }
                String[] partes = linha.split(",");
                if (secao.equals("Carros")) {
                    int id = Integer.parseInt(partes[0]);
                    String marca = partes[1];
                    String modelo = partes[2];
                    int ano = Integer.parseInt(partes[3]);
                    String matricula = partes[4];
                    // Evitar duplicatas por matrícula
                    boolean matriculaExiste = carros.stream().anyMatch(c -> c.getMatricula().equals(matricula));
                    if (!matriculaExiste) {
                        if (partes[5].equals("Passeio")) {
                            int numPass = Integer.parseInt(partes[6]);
                            CarroPasseio c = new CarroPasseio(marca, modelo, ano, matricula, numPass);
                            c.setId(id);
                            carros.add(c);
                        } else {
                            double cap = Double.parseDouble(partes[6]);
                            CarroComercial c = new CarroComercial(marca, modelo, ano, matricula, cap);
                            c.setId(id);
                            carros.add(c);
                        }
                    }
                } else if (secao.equals("Servicos")) {
                    int id = Integer.parseInt(partes[0]);
                    String desc = partes[1];
                    double custo = Double.parseDouble(partes[2]);
                    // Evitar duplicatas por descrição
                    boolean descExiste = servicos.stream().anyMatch(s -> s.getDescricao().equals(desc));
                    if (!descExiste) {
                        Servico s = new Servico(desc, custo);
                        s.setId(id);
                        servicos.add(s);
                    }
                } else if (secao.equals("Faturas")) {
                    int id = Integer.parseInt(partes[0]);
                    // Verificar se ID já existe
                    boolean idExiste = faturas.stream().anyMatch(f -> f.getId() == id);
                    if (idExiste) {
                        int resposta = JOptionPane.showConfirmDialog(null, "Fatura ID " + id + " já existe. Carregar mesmo assim?", "Duplicata de Fatura", JOptionPane.YES_NO_OPTION);
                        if (resposta != JOptionPane.YES_OPTION) {
                            continue;
                        }
                    }
                    int idCarro = Integer.parseInt(partes[1]);
                    String[] idsServ = partes[2].split(";");
                    Carro carro = procurarCarro(idCarro);
                    List<Servico> servs = new ArrayList<>();
                    for (String sid : idsServ) {
                        Servico s = procurarServico(Integer.parseInt(sid));
                        if (s != null) servs.add(s);
                    }
                    Fatura f = new Fatura(carro, servs);
                    f.setId(id);
                    faturas.add(f);
                }
            }
        }
    }
}