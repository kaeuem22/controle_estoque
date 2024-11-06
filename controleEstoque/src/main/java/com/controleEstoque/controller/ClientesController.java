package com.controleEstoque.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ClientesController {

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

    public void initialize() {
        controleEstoqueButton.setOnAction(event -> loadScreen("/view/Controle.fxml"));
        fornecedoresButton.setOnAction(event -> loadScreen("/view/Fornecedores.fxml"));
        historicoButton.setOnAction(event -> loadScreen("/view/Historico.fxml"));
        atualizarEstoqueButton.setOnAction(event -> loadScreen("/view/Atualizar.fxml"));
        cadastrarProdutoButton.setOnAction(event -> loadScreen("/view/Cadastrar.fxml"));
    }

    private void loadScreen(String fxmlFile) {
        // Obtém o Stage atual no momento do evento
        Stage currentStage = (Stage) clientesButton.getScene().getWindow();
        // Chama o método da superclasse RouteController
        new RouteController().loadScreen(currentStage, fxmlFile);
    }
}