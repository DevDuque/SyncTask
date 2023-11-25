package org.SyncTask.connection;

import org.SyncTask.exceptions.UserNotFoundException;
import org.SyncTask.models.UserModel;

import java.sql.*;
import java.util.UUID;

public class MyConnection {

    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            // If the connection has not been created yet, create it
            String uri = "jdbc:mysql://localhost:3306/SyncTask";
            String usuario = "root";
            String senha = "";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(uri, usuario, senha);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace(); // Handle exceptions more robustly in production
            }
        }

        return conn;
    }

    // Método para autenticar um usuário
    public static UserModel authenticateUser(String username, String password) throws UserNotFoundException {
        // Comando MySQL para verificar a autenticação do usuário
        String authenticationQuery = "SELECT * FROM UserTable WHERE Username = ? AND Password = ?";

        try {
            // Preparando a declaração SQL
            PreparedStatement ps = conn.prepareStatement(authenticationQuery);

            // Definindo os parâmetros da declaração SQL com base no nome de usuário e senha fornecidos
            ps.setString(1, username);
            ps.setString(2, password);

            // Preparando a declaração SQL
            ResultSet rs = ps.executeQuery();

            // Verificando se o conjunto de resultados contém um usuário autenticado
            if (!rs.next()) {
                // Lançar a exceção se o usuário não for encontrado
                throw new UserNotFoundException("Nenhum usuário encontrado no banco de dados");
            }

            // Criar um objeto UserModel com base nos dados recuperados do banco de dados
            UUID UserID = UUID.fromString(rs.getString("UserID"));
            String Name = rs.getString("Name");
            boolean isAdmin = rs.getBoolean("IsAdmin");

            return new UserModel(UserID, Name, username, password, new Date(2023, 11, 26), isAdmin);

            // Se chegou aqui, o usuário está autenticado
        } catch (SQLException e) {
            e.printStackTrace(); // Tratar exceções de uma forma mais robusta em produção
        }
        return null;
    }
}
