package com.controleEstoque.controller;

import com.controleEstoque.db.DB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.*;

public class LoginController {

    @FXML
    private TextField nomeField;
    @FXML
    private PasswordField senhaField;
    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> {
            String nome = nomeField.getText();
            String senha = senhaField.getText();

            loginTrue(event, nome, senha);
        });
    }

    private void loginTrue(ActionEvent event, String nome, String senha) {
        boolean autenticado = autenticacao(nome, senha);
        if (autenticado) {
            try {
                // Carrega o arquivo FXML da tela principal
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Controle.fxml"));
                Parent root = loader.load();

                // Define a nova cena
                Scene scene = new Scene(root);

                // Obtem o Stage atual (janela) a partir do evento
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Configura a nova cena
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Usuario ou senha incorretas.");
            alert.showAndWait();
        }
    }

    public static boolean autenticacao(String nome, String senha) {
        PreparedStatement st = null;
        java.sql.Connection conn = null;

        try {
            conn = DB.getConnection();
            String sql = "SELECT * FROM usuario WHERE nome = ? AND senha = ?";
            st = conn.prepareStatement(sql);

            st.setString(1, nome);
            st.setString(2, senha);

            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao autenticar o usuário: " + e.getMessage());
            alert.showAndWait();
            return false;
        } finally {
            DB.closeStatement(st);
            DB.closeConnection();
        }
    }



    public void mostrarTelaLogin(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Tela de Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
