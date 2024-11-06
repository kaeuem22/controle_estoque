package com.controleEstoque.funcoes;

import com.controleEstoque.db.DB;

import java.sql.*;
import java.util.Locale;
import java.util.Scanner;


public class Program {
    static Scanner sc = new Scanner(System.in).useLocale(new Locale("pt","BR"));
    static Connection conn = null;

    public static void main(String[] args) {

        int nav = 0;

        while(nav !=9){

            System.out.println("\n---- O QUE GOSTARIA DE FAZER? ----");
            System.out.println("1-Cadastrar produto");
            System.out.println("2-Ver toda a tabela");
            System.out.println("3-ver apenas um id");
            System.out.println("9-sair");
            System.out.print("escolha: ");
            nav = sc.nextInt();
        switch (nav) {
            case 1:
                createItem("a",1,1);
                break;
            case 2:
                readItem();
                break;
            case 3:
                readSpecificItem();
                break;
            default:
            break;
        }
    }

        sc.close();
        DB.closeConnection();
    }

    public static void readSpecificItem() {


        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DB.getConnection();

            st = conn.createStatement();

            System.out.print("Insira o Id: ");
            int id_sc = sc.nextInt();

            rs = st.executeQuery("select * from produto where id =" + id_sc + "");
            while (rs.next()) {
                System.out.printf("Id: %d - Nome: %s - Preco: %.2f - Quantidade: %d\n", rs.getInt("Id"), rs.getString("Nome"), rs.getDouble("Preco"), rs.getInt("Quantidade"));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    private static void readItem() {

        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DB.getConnection();

            st = conn.createStatement();

            rs = st.executeQuery("select * from produto");
            while (rs.next()) {
                System.out.printf("Id: %d - Nome: %s - Preco: %.2f - Qantidade: %d\n", rs.getInt("Id"), rs.getString("Nome"), rs.getDouble("Preco"), rs.getInt("Quantidade"));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    public static void createItem(String nome,double preco,int quantidade) {
        //sc.nextLine();
        PreparedStatement st = null;
        try {
            conn = DB.getConnection();

            st = conn.prepareStatement(
                    "INSERT INTO produto"
                            + "(Nome, Preco, Quantidade)"
                            + "VALUES"
                            + "(?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);


            st.setString(1, nome);
            st.setDouble(2, preco);
            st.setInt(3, quantidade);

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                while (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("Done! id = " + id);
                    rs = st.executeQuery("select * from produto where id =" + id + "");
                    while (rs.next()) {
                        System.out.printf("Id: %d - Nome: %s - Preco: %.2f - Qantidade: %d\n", rs.getInt("Id"), rs.getString("Nome"), rs.getDouble("Preco"), rs.getInt("Quantidade"));
                    }
                }
            } else {
                System.out.println("No rows affected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*
        catch (ParseException e){
            e.printStackTrace();
        }*/
        finally {
            DB.closeStatement(st);
        }
    }
}
