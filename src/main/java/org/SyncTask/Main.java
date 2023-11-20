package org.SyncTask;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        // Configurações do banco de dados
        String url = "jdbc:mysql://localhost:3306/SyncTask";
        String usuario = "2023954740";
        String senha = "2023954740";

        try {
            // Conectar ao banco de dados
            Connection conexao = DriverManager.getConnection(url, usuario, senha);

            // Criar uma instrução SQL
            Statement instrucao = conexao.createStatement();

            // Executar a consulta SQL
            String sql = "SELECT UserID, Name, Username, CreatedAt FROM UserTable";
            ResultSet resultado = instrucao.executeQuery(sql);

            // Exibir os resultados
            while (resultado.next()) {
                UUID userId = UUID.fromString(resultado.getString("userId"));
                String nome = resultado.getString("nome");
                String username = resultado.getString("username");
                String createdAt = resultado.getString("createdAt");

                System.out.println("UserID: " + userId);
                System.out.println("Nome: " + nome);
                System.out.println("Username: " + username);
                System.out.println("CreatedAt: " + createdAt);
                System.out.println();
            }

            // Fechar recursos
            resultado.close();
            instrucao.close();
            conexao.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}