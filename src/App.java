import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;

public class App extends JFrame {
    private Oficina oficina;
    private JTabbedPane tabbedPane;
    private JTable tabelaCarros;
    private JTable tabelaServicos;
    private JTable tabelaFaturas;
    private DefaultTableModel modeloCarros;
    private DefaultTableModel modeloServicos;
    private DefaultTableModel modeloFaturas;

    public App() {
        oficina = new Oficina();
        // Carregar dados se existir
        try {
            oficina.carregarDados("dados.csv");
        } catch (IOException e) {
            // Ficheiro não existe, ignorar
        }

        // Dark mode setup
        try {
            // Definir propriedades antes de setLookAndFeel
            UIManager.put("control", Color.DARK_GRAY);
            UIManager.put("text", Color.WHITE);
            UIManager.put("nimbusBase", Color.BLACK);
            UIManager.put("nimbusBlueGrey", Color.DARK_GRAY);
            UIManager.put("nimbusLightBackground", Color.GRAY);
            UIManager.put("Table.background", Color.BLACK);
            UIManager.put("Table.foreground", Color.WHITE);
            UIManager.put("TableHeader.background", Color.DARK_GRAY);
            UIManager.put("TableHeader.foreground", Color.WHITE);
            UIManager.put("Button.background", Color.LIGHT_GRAY);
            UIManager.put("Button.foreground", Color.BLACK);
            UIManager.put("Button.font", new Font("SansSerif", Font.BOLD, 12));
            UIManager.put("ToolTip.background", Color.WHITE);
            UIManager.put("ToolTip.foreground", Color.BLACK);
            UIManager.put("ToolTip.font", new Font("SansSerif", Font.BOLD, 12));

            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("ToolTipUI", "javax.swing.plaf.basic.BasicToolTipUI");
            UIManager.put("ToolTip.background", Color.WHITE);
            UIManager.put("ToolTip.foreground", Color.BLACK);
            UIManager.put("ToolTip.font", new Font("SansSerif", Font.BOLD, 12));
        } catch (Exception e) {
        }

        setTitle("Gestão Oficina Mecânica");
        setSize(1000, 700); // Aumentado para melhor legibilidade
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.DARK_GRAY);

        // Centro: TabbedPane
        tabbedPane = new JTabbedPane();
        criarTabelaCarros();
        criarTabelaServicos();
        criarTabelaFaturas();
        add(tabbedPane, BorderLayout.CENTER);

        // Inferior: Botões
        JPanel painelBotoes = new JPanel(new GridLayout(2, 5, 5, 5)); // 2 linhas, 5 colunas, espaçamento 5px
        JButton btnCriarCarro = new JButton("Criar Carro");
        JButton btnCriarServico = new JButton("Criar Serviço");
        JButton btnAtribuirServico = new JButton("Atribuir Serviço");
        JButton btnGerarFatura = new JButton("Gerar Fatura");
        JButton btnListar = new JButton("Atualizar Listas");
        JButton btnProcurar = new JButton("Procurar");
        JButton btnRemover = new JButton("Remover");
        JButton btnRelatorios = new JButton("Relatórios");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCarregar = new JButton("Carregar");

        painelBotoes.add(btnCriarCarro);
        painelBotoes.add(btnCriarServico);
        painelBotoes.add(btnAtribuirServico);
        painelBotoes.add(btnGerarFatura);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnProcurar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnRelatorios);
        painelBotoes.add(btnGuardar);
        painelBotoes.add(btnCarregar);

        add(painelBotoes, BorderLayout.SOUTH);

        btnCriarCarro.setToolTipText("Adicionar um novo carro à oficina");
        btnCriarServico.setToolTipText("Adicionar um novo serviço");
        btnAtribuirServico.setToolTipText("Atribuir um serviço a um carro");
        btnGerarFatura.setToolTipText("Gerar fatura para um carro");
        btnListar.setToolTipText("Atualizar as listas de dados");
        btnProcurar.setToolTipText("Procurar por ID");
        btnRemover.setToolTipText("Remover por ID");
        btnRelatorios.setToolTipText("Ver estatísticas gerais");
        btnGuardar.setToolTipText("Salvar dados manualmente");
        btnCarregar.setToolTipText("Carregar dados do arquivo");
        btnCriarCarro.addActionListener(e -> criarCarro());
        btnCriarServico.addActionListener(e -> criarServico());
        btnAtribuirServico.addActionListener(e -> atribuirServico());
        btnGerarFatura.addActionListener(e -> gerarFatura());
        btnListar.addActionListener(e -> atualizarListas());
        btnProcurar.addActionListener(e -> procurar());
        btnRemover.addActionListener(e -> remover());
        btnRelatorios.addActionListener(e -> relatorios());
        btnGuardar.addActionListener(e -> guardar());
        btnCarregar.addActionListener(e -> carregar());

        atualizarListas();

        // Salvar dados ao fechar
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                try {
                    oficina.salvarDados("dados.csv");
                } catch (IOException ex) {
                }
            }
        });
    }

    // Cria tabela carros
    private void criarTabelaCarros() {
        modeloCarros = new DefaultTableModel(new String[]{"ID", "Marca", "Modelo", "Ano", "Matrícula", "Tipo"}, 0);
        tabelaCarros = new JTable(modeloCarros);
        tabelaCarros.setRowHeight(25); // Altura maior para legibilidade
        tabelaCarros.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabbedPane.addTab("Carros", new JScrollPane(tabelaCarros));
    }

    // Cria tabela serviços
    private void criarTabelaServicos() {
        modeloServicos = new DefaultTableModel(new String[]{"ID", "Descrição", "Custo"}, 0);
        tabelaServicos = new JTable(modeloServicos);
        tabelaServicos.setRowHeight(25);
        tabelaServicos.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabbedPane.addTab("Serviços", new JScrollPane(tabelaServicos));
    }

    // Cria tabela faturas
    private void criarTabelaFaturas() {
        modeloFaturas = new DefaultTableModel(new String[]{"ID", "Carro", "Total"}, 0);
        tabelaFaturas = new JTable(modeloFaturas);
        tabelaFaturas.setRowHeight(25);
        tabelaFaturas.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabbedPane.addTab("Faturas", new JScrollPane(tabelaFaturas));
    }

    private void atualizarListas() {
        // Carros
        modeloCarros.setRowCount(0);
        for (Carro c : oficina.listarCarros()) {
            String tipo = c instanceof CarroPasseio ? "Passeio" : "Comercial";
            modeloCarros.addRow(new Object[]{c.getId(), c.getMarca(), c.getModelo(), c.getAno(), c.getMatricula(), tipo});
        }
        // Serviços
        modeloServicos.setRowCount(0);
        for (Servico s : oficina.listarServicos()) {
            modeloServicos.addRow(new Object[]{s.getId(), s.getDescricao(), s.getCusto()});
        }
        // Faturas
        modeloFaturas.setRowCount(0);
        for (Fatura f : oficina.listarFaturas()) {
            modeloFaturas.addRow(new Object[]{f.getId(), f.getCarro().getMarca() + " " + f.getCarro().getModelo(), f.getTotal()});
        }
    }

    // Cria carro
    private void criarCarro() {
        JDialog dialog = new JDialog(this, "Criar Carro", true);
        dialog.setLayout(new GridLayout(6, 2));
        JTextField txtMarca = new JTextField();
        JTextField txtModelo = new JTextField();
        JTextField txtAno = new JTextField();
        JTextField txtMatricula = new JTextField();
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Passeio", "Comercial"});
        JButton btnOk = new JButton("OK");

        // Adicionar asteriscos para campos obrigatórios
        dialog.add(new JLabel("Marca*:"));
        dialog.add(txtMarca);
        dialog.add(new JLabel("Modelo*:"));
        dialog.add(txtModelo);
        dialog.add(new JLabel("Ano*:"));
        dialog.add(txtAno);
        dialog.add(new JLabel("Matrícula* (AA-00-00):"));
        dialog.add(txtMatricula);
        dialog.add(new JLabel("Tipo*:"));
        dialog.add(comboTipo);
        dialog.add(btnOk);

        // Adicionar tooltips para melhor UX
        txtMarca.setToolTipText("Marca do carro");
        txtModelo.setToolTipText("Modelo do carro");
        txtAno.setToolTipText("Ano de fabricação (ex.: 2020)");
        txtMatricula.setToolTipText("Matrícula no formato AA-00-00");
        comboTipo.setToolTipText("Tipo de carro");
        btnOk.setToolTipText("Criar carro");

        btnOk.addActionListener(e -> {
            try {
                String marca = txtMarca.getText().trim();
                String modelo = txtModelo.getText().trim();
                String anoStr = txtAno.getText().trim();
                String matricula = txtMatricula.getText().trim();
                String tipo = (String) comboTipo.getSelectedItem();

                // Validações básicas de campos vazios
                if (marca.isEmpty() || modelo.isEmpty() || anoStr.isEmpty() || matricula.isEmpty() || tipo == null) {
                    JOptionPane.showMessageDialog(dialog, "Todos os campos obrigatórios (*) devem ser preenchidos!");
                    return;
                }

                int ano = Integer.parseInt(anoStr);

                // Verificar matrícula duplicada
                boolean matriculaExiste = oficina.listarCarros().stream().anyMatch(c -> c.getMatricula().equals(matricula));
                if (matriculaExiste) {
                    JOptionPane.showMessageDialog(dialog, "Matrícula já existe! Escolha uma matrícula diferente.");
                    return;
                }

                if (tipo.equals("Passeio")) {
                    oficina.adicionarCarro(new CarroPasseio(marca, modelo, ano, matricula, 4)); // Valor padrão 4 passageiros
                } else {
                    oficina.adicionarCarro(new CarroComercial(marca, modelo, ano, matricula, 1000.0)); // Valor padrão 1000kg
                }
                atualizarListas();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Ano, matrícula ou extra inválidos! Verifique os formatos.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, "Erro de validação: " + ex.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro inesperado: " + ex.getMessage());
            }
        });

        dialog.setSize(350, 250);
        dialog.setVisible(true);
    }

    // Cria serviço
    private void criarServico() {
        JDialog dialog = new JDialog(this, "Criar Serviço", true);
        dialog.setLayout(new GridLayout(4, 2));
        JTextField txtDescricao = new JTextField();
        JTextField txtCusto = new JTextField();
        JButton btnOk = new JButton("OK");

        dialog.add(new JLabel("Descrição*:"));
        dialog.add(txtDescricao);
        dialog.add(new JLabel("Custo*:"));
        dialog.add(txtCusto);
        dialog.add(btnOk);

        // Adicionar tooltips para melhor UX
        txtDescricao.setToolTipText("Descrição do serviço");
        txtCusto.setToolTipText("Custo em euros (ex.: 50.0)");
        btnOk.setToolTipText("Criar serviço");

        btnOk.addActionListener(e -> {
            try {
                String desc = txtDescricao.getText().trim();
                String custoStr = txtCusto.getText().trim();

                // Validações básicas
                if (desc.isEmpty() || custoStr.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Todos os campos obrigatórios (*) devem ser preenchidos!");
                    return;
                }

                // Verificar descrição duplicada
                boolean descExiste = oficina.listarServicos().stream().anyMatch(s -> s.getDescricao().equals(desc));
                if (descExiste) {
                    JOptionPane.showMessageDialog(dialog, "Serviço com essa descrição já existe! Escolha uma descrição diferente.");
                    return;
                }

                double custo = Double.parseDouble(custoStr);
                oficina.adicionarServico(new Servico(desc, custo));
                atualizarListas();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Custo inválido! Digite um número decimal.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, "Erro de validação: " + ex.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro inesperado: " + ex.getMessage());
            }
        });

        dialog.setSize(350, 150);
        dialog.setVisible(true);
    }

    private void atribuirServico() {
        String idCarroStr = JOptionPane.showInputDialog(this, "ID do Carro (número inteiro, verifique na aba Carros):");
        String idServicoStr = JOptionPane.showInputDialog(this, "ID do Serviço (número inteiro, verifique na aba Serviços):");
        try {
            int idCarro = Integer.parseInt(idCarroStr);
            int idServico = Integer.parseInt(idServicoStr);
            oficina.atribuirServicoACarro(idCarro, idServico);
            JOptionPane.showMessageDialog(this, "Serviço atribuído com sucesso!");
            atualizarListas();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "IDs inválidos! Digite números inteiros válidos.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void gerarFatura() {
        String idCarroStr = JOptionPane.showInputDialog(this, "ID do Carro (número inteiro, verifique na aba Carros):");
        try {
            int idCarro = Integer.parseInt(idCarroStr);
            Fatura f = oficina.gerarFatura(idCarro);
            if (f != null) {
                String relatorio = "Fatura ID: " + f.getId() + "\nCarro: " + f.getCarro() + "\nServiços:\n";
                for (Servico s : f.getServicos()) {
                    relatorio += s + "\n";
                }
                relatorio += "Total: " + f.getTotal();
                JOptionPane.showMessageDialog(this, relatorio);
                // Guardar relatório
                try (FileWriter writer = new FileWriter("relatorio_fatura_" + f.getId() + ".txt")) {
                    writer.write(relatorio);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao guardar relatório: " + ex.getMessage());
                }
                atualizarListas();
            } else {
                JOptionPane.showMessageDialog(this, "Carro não encontrado ou sem serviços atribuídos.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID inválido! Digite um número inteiro.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void procurar() {
        String tipo = JOptionPane.showInputDialog(this, "Procurar: 'Carro' ou 'Servico' (digite exatamente):");
        if (tipo == null || (!tipo.equalsIgnoreCase("Carro") && !tipo.equalsIgnoreCase("Servico"))) {
            JOptionPane.showMessageDialog(this, "Tipo inválido. Digite 'Carro' ou 'Servico'.");
            return;
        }
        String idStr = JOptionPane.showInputDialog(this, "ID (número inteiro, verifique na aba correspondente):");
        try {
            int id = Integer.parseInt(idStr);
            if (tipo.equalsIgnoreCase("Carro")) {
                Carro c = oficina.procurarCarro(id);
                if (c != null) {
                    mostrarDetalhesCarro(c);
                } else {
                    JOptionPane.showMessageDialog(this, "Carro não encontrado.");
                }
            } else if (tipo.equalsIgnoreCase("Servico")) {
                Servico s = oficina.procurarServico(id);
                if (s != null) {
                    mostrarDetalhesServico(s);
                } else {
                    JOptionPane.showMessageDialog(this, "Serviço não encontrado.");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID inválido! Digite um número inteiro.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void remover() {
        String tipo = JOptionPane.showInputDialog(this, "Remover: 'Carro' ou 'Servico' (digite exatamente):");
        if (tipo == null || (!tipo.equalsIgnoreCase("Carro") && !tipo.equalsIgnoreCase("Servico"))) {
            JOptionPane.showMessageDialog(this, "Tipo inválido. Digite 'Carro' ou 'Servico'.");
            return;
        }
        String idStr = JOptionPane.showInputDialog(this, "ID (número inteiro, verifique na aba correspondente):");
        try {
            int id = Integer.parseInt(idStr);
            if (tipo.equalsIgnoreCase("Carro")) {
                oficina.removerCarro(id);
                JOptionPane.showMessageDialog(this, "Carro removido com sucesso!");
            } else if (tipo.equalsIgnoreCase("Servico")) {
                oficina.removerServico(id);
                JOptionPane.showMessageDialog(this, "Serviço removido com sucesso!");
            }
            atualizarListas();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID inválido! Digite um número inteiro.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void mostrarDetalhesCarro(Carro c) {
        JDialog dialog = new JDialog(this, "Detalhes do Carro", true);
        dialog.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(new String[]{"Propriedade", "Valor"}, 0);
        model.addRow(new Object[]{"ID", c.getId()});
        model.addRow(new Object[]{"Marca", c.getMarca()});
        model.addRow(new Object[]{"Modelo", c.getModelo()});
        model.addRow(new Object[]{"Ano", c.getAno()});
        model.addRow(new Object[]{"Matrícula", c.getMatricula()});
        model.addRow(new Object[]{"Tipo", c instanceof CarroPasseio ? "Passeio" : "Comercial"});

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

        dialog.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dialog.dispose());
        dialog.add(btnFechar, BorderLayout.SOUTH);

        dialog.setSize(400, 250);
        dialog.setVisible(true);
    }

    private void mostrarDetalhesServico(Servico s) {
        JDialog dialog = new JDialog(this, "Detalhes do Serviço", true);
        dialog.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(new String[]{"Propriedade", "Valor"}, 0);
        model.addRow(new Object[]{"ID", s.getId()});
        model.addRow(new Object[]{"Descrição", s.getDescricao()});
        model.addRow(new Object[]{"Custo", s.getCusto() + "€"});

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

        dialog.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dialog.dispose());
        dialog.add(btnFechar, BorderLayout.SOUTH);

        dialog.setSize(400, 150);
        dialog.setVisible(true);
    }

    private void guardar() {
        try {
            oficina.salvarDados("dados.csv");
            JOptionPane.showMessageDialog(this, "Dados guardados!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void relatorios() {
        double totalFaturado = oficina.listarFaturas().stream().mapToDouble(Fatura::getTotal).sum();
        long numCarros = oficina.listarCarros().size();
        long numServicos = oficina.listarServicos().size();
        long numFaturas = oficina.listarFaturas().size();

        String relatorio = "Relatório Geral:\n" +
                "Total Faturado: " + totalFaturado + "€\n" +
                "Número de Carros: " + numCarros + "\n" +
                "Número de Serviços: " + numServicos + "\n" +
                "Número de Faturas: " + numFaturas + "\n";

        JOptionPane.showMessageDialog(this, relatorio);
    }

    private void carregar() {
        try {
            oficina.carregarDados("dados.csv");
            atualizarListas();
            JOptionPane.showMessageDialog(this, "Dados carregados!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new App().setVisible(true);
        });
    }
}
