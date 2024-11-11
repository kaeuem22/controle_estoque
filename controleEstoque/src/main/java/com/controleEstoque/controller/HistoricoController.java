package com.controleEstoque.controller;

import com.controleEstoque.db.DB;
import com.controleEstoque.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import com.controleEstoque.db.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoricoController {

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
    private Button pesquisarDia;
    @FXML
    private Button pesquisarSemana;
    @FXML
    private Button pesquisarMes;
    @FXML
    private Button pesquisarTodos;

    public void initialize() {
        controleEstoqueButton.setOnAction(event -> loadScreen("/view/Controle.fxml"));
        fornecedoresButton.setOnAction(event -> loadScreen("/view/Fornecedores.fxml"));
        clientesButton.setOnAction(event -> loadScreen("/view/Clientes.fxml"));
        atualizarEstoqueButton.setOnAction(event -> loadScreen("/view/Atualizar.fxml"));
        cadastrarProdutoButton.setOnAction(event -> loadScreen("/view/Cadastrar.fxml"));

        pesquisarDia.setOnAction(event -> executarConsulta(1));
        pesquisarSemana.setOnAction(event -> executarConsulta(2));
        pesquisarMes.setOnAction(event -> executarConsulta(3));
        pesquisarTodos.setOnAction(event -> executarConsulta(4));
    }

    private void loadScreen(String fxmlFile) {
        // Obtém o Stage atual no momento do evento
        Stage currentStage = (Stage) historicoButton.getScene().getWindow();
        // Chama o método da superclasse RouteController
        new RouteController().loadScreen(currentStage, fxmlFile);
    }

    private static void executarConsulta(int tipo) {
        PreparedStatement st = null;
        Connection conn = null;
        ResultSet rs = null;
        String query = null;

        switch (tipo) {
            case 1:
                query = "SELECT Nome, Quantidade, data_criacao FROM historico WHERE data_criacao BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 1 DAY) - INTERVAL 1 SECOND";
                break;
            case 2:
                query = "SELECT Nome, Quantidade, data_criacao FROM historico WHERE data_criacao >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) GROUP BY Produto_id";
                break;
            case 3:
                query = "SELECT Nome, Quantidade, data_criacao FROM historico WHERE MONTH(data_criacao) = MONTH(CURDATE()) AND YEAR(data_criacao) = YEAR(CURDATE()) GROUP BY Produto_id";
                break;
            case 4:
                query = "SELECT Nome, Quantidade, data_criacao FROM historico LIMIT 1000";
                break;
            default:
                System.out.println("Opção inválida.");
        }

        try{
            conn = DB.getConnection();
            st = conn.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                // Exibir resultados conforme a consulta
                System.out.print(STR."Produto: \{rs.getString("Nome")}\n"); // Devolve nome
                System.out.print(STR."Quantidade: \{rs.getInt("Quantidade")}\n"); // Devolve quantidade
                System.out.print(STR."Quantidade: \{rs.getTimestamp("data_criacao")}\n"); // Devolve data de criação
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao buscar produtos: " + e.getMessage());
            alert.showAndWait();
        } finally {
            // Fechar recursos manualmente
            DB.closeResultSet(rs);
            DB.closeStatement(st);
            DB.closeConnection();
        }
    }
}
