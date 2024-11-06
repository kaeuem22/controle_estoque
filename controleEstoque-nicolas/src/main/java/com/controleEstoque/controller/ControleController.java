package com.controleEstoque.controller;

import com.controleEstoque.db.DB;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ControleController {

    @FXML
    private Label totalEstoqueLabel;
    @FXML
    private Label disponivelVendaLabel;
    @FXML
    private Label reservadosLabel;

    // Botões do menu lateral
    @FXML
    private Button controleEstoqueButton;
    @FXML
    private Button fornecedoresButton;
    @FXML
    private Button clientesButton;
    @FXML
    private Button historicoButton;
    @FXML
    private Button cadastrarProdutoButton;
    @FXML
    private Button atualizarEstoqueButton;
    @FXML
    private TextField nomeProdutoField;
    @FXML
    private TableView tabelaProdutos;
    @FXML
    private Button pesquisarButton;


    public void initialize() {
        atualizarEstoqueButton.setOnAction(event -> loadScreen("/view/Atualizar.fxml"));
        fornecedoresButton.setOnAction(event -> loadScreen("/view/Fornecedores.fxml"));
        clientesButton.setOnAction(event -> loadScreen("/view/Clientes.fxml"));
        historicoButton.setOnAction(event -> loadScreen("/view/Historico.fxml"));
        cadastrarProdutoButton.setOnAction(event -> loadScreen("/view/Cadastrar.fxml"));
        pesquisarButton.setOnAction(event -> acaoPesquisaNome()); // Move o setOnAction para initialize
    }

    private void loadScreen(String fxmlFile) {
        Stage currentStage = (Stage) controleEstoqueButton.getScene().getWindow();
        new RouteController().loadScreen(currentStage, fxmlFile);
    }

    public void acaoPesquisaNome() { //passa campo de texto pra função de pesquisa
        String getNomeProdutoField = nomeProdutoField.getText();

        try {
            PesquisaNome(getNomeProdutoField);
        } catch (NumberFormatException e) {
            // Exibir um alerta em caso de entrada inválida
            Alert alert = new Alert(Alert.AlertType.ERROR, "Entrada inválida: verifique o campo de nome.");
            alert.showAndWait();
        }
    }
    public static void PesquisaNome(String nome) {
        java.sql.Connection conn = null; // Declare a conexão aqui


        try {
            PreparedStatement st = null;
            ResultSet rs = null;
            try {

                // gera tabla
                TableView tab = new TableView();

                TableColumn idColumn = new TableColumn("ID");
                idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));

                TableColumn nomeColumn = new TableColumn("Surname");
                nomeColumn.setCellValueFactory(new PropertyValueFactory<>("Nome"));

                TableColumn preocColumn = new TableColumn("Preço");
                preocColumn.setCellValueFactory(new PropertyValueFactory<>("Preco"));

                TableColumn quantidadeColumn = new TableColumn("Quantidade");
                quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("Quantidade"));

                tab.getColumns().addAll(idColumn, nomeColumn,preocColumn,quantidadeColumn);

                class Produto {
                    private int Id;
                    private String Nome;
                    private double Preco;
                    private int Quantidade;

                    public Produto(int id, String nome, double preco, int quantidade) {
                        this.Id = id;
                        this.Nome = nome;
                        this.Preco = preco;
                        this.Quantidade = quantidade;
                    }

                    public int getId() {
                        return Id;
                    }

                    public String getSurname() {
                        return Nome;
                    }

                    public double getPreco() {
                        return Preco;
                    }

                    public int getQuantidade() {
                        return Quantidade;
                    }
                }
                conn = DB.getConnection();

                String sql = STR."SELECT * FROM produto WHERE Nome LIKE '%\{nome}%'";

                st = conn.prepareStatement(sql);

                rs = st.executeQuery();
                while (rs.next()) {
                    System.out.printf(STR."ID \{rs.getInt("Id")}\n");//devolve o id do produto
                    System.out.printf(STR."Nome \{rs.getString("Nome")}\n");//devolve o nome do produto
                    System.out.printf(STR."Preço \{rs.getDouble("Preco")}\n");//devolve o preço do produto
                    System.out.printf(STR."Quantidade \{rs.getInt("Quantidade")}\n");//devolve o quantidade do produto

                    Produto produto = new Produto(rs.getInt("Id"), rs.getString("Nome"),rs.getDouble("Preco"),rs.getInt("Quantidade"));
                    tab.getItems().add(produto);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao buscar produtos: " + e.getMessage());
                alert.showAndWait();
            } finally {
                DB.closeStatement(st);
                DB.closeConnection();
            }
        } finally {

        }
    }

    public TextField getNomeProdutoField() {
        return nomeProdutoField;
    }

    public void setNomeProdutoField(TextField nomeProdutoField) {
        this.nomeProdutoField = nomeProdutoField;
    }
}
