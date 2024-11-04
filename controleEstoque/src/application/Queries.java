package application;

import db.DB;

import java.sql.*;
import java.util.Locale;
import java.util.Scanner;


public class Queries {
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
                createItem();
                break;
            case 2:
                queryItem();
                break;
            case 3:
                querySpecificItem(1);
                break;
            default:
            break;
        }
    }

        sc.close();
        DB.closeConenction();
    }

    public static Object querySpecificItem(int id) {


        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DB.getConnection();

            st = conn.createStatement();

            //System.out.print("Insira o Id: ");
            //int id_sc = sc.nextInt();

            rs = st.executeQuery("select * from produto where id =" + id);
            while (rs.next()) {
                return String.format("Id: %d\n Nome: %s\n Preco: R$ %.2f\n Quantidade: %d\n", rs.getInt("Id"), rs.getString("Nome"), rs.getDouble("Preco"), rs.getInt("Quantidade"));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
        return null;
    }

    public static String queryItem() {

        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DB.getConnection();

            st = conn.createStatement();

            rs = st.executeQuery("select * from produto");
            while (rs.next()) {
                return String.format("Id: %d - Nome: %s - Preco: %.2f - Qantidade: %d\n", rs.getInt("Id"), rs.getString("Nome"), rs.getDouble("Preco"), rs.getInt("Quantidade"));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
        return null;
    }

    private static void createItem() {
        sc.nextLine();
        PreparedStatement st = null;
        try {
            conn = DB.getConnection();

            st = conn.prepareStatement(
                    "INSERT INTO produto"
                            + "(Nome, Preco, Quantidade)"
                            + "VALUES"
                            + "(?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);


            System.out.print("Insira o nome do produto: ");
            String nome_sc = sc.nextLine();


            System.out.print("Insira o valor do produto (sem 'R$'): ");
            double preco_sc = sc.nextDouble();

            System.out.print("Insira a quantidade inicial: ");
            int quantidade_sc = sc.nextInt();


            st.setString(1, nome_sc);
            st.setDouble(2, preco_sc);
            st.setInt(3, quantidade_sc);

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
