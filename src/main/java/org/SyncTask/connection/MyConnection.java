package org.SyncTask.connection;

import org.SyncTask.exceptions.InvalidPasswordException;
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
    public static UserModel authenticateUser(String username, String password) throws InvalidPasswordException, UserNotFoundException {
        // Comando MySQL para verificar a autenticação do usuário
        String authenticationQuery = "SELECT * FROM UserTable WHERE Username = ?";

        try {
            // Preparando a declaração SQL
            PreparedStatement ps = conn.prepareStatement(authenticationQuery);

            // Definindo os parâmetros da declaração SQL com base no nome de usuário fornecido
            ps.setString(1, username);

            // Executando a consulta SQL
            ResultSet rs = ps.executeQuery();

            // Verificando se o conjunto de resultados contém um usuário com o nome de usuário fornecido
            if (rs.next()) {
                // Verificando a senha
                String storedPassword = rs.getString("Password");
                if (password.equals(storedPassword)) {
                    // Criar e retornar um objeto UserModel se a senha estiver correta
                    UUID userID = UUID.fromString(rs.getString("UserID"));
                    String name = rs.getString("Name");
                    boolean isAdmin = rs.getBoolean("IsAdmin");

                    return new UserModel(userID, name, username, password, new Date(2023, 11, 26), isAdmin);
                } else {
                    // Se a senha estiver incorreta, lançar a exceção InvalidPasswordException
                    throw new InvalidPasswordException("Senha incorreta");
                }
            } else {
                // Se o usuário não for encontrado, lançar a exceção UserNotFoundException
                throw new UserNotFoundException("Nenhum usuário encontrado no banco de dados");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Tratar exceções de uma forma mais robusta em produção
        }

        return null;
    }
}
