package org.SyncTask.database;

import org.SyncTask.connection.MyConnection;
import org.SyncTask.models.UserModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Date;

public class UserDAO extends DAO<UserModel> {
    private Connection myConnection;

    // Construtor que obtém uma conexão do banco de dados ao instanciar o UserDAO
    public UserDAO() {
        this.myConnection = MyConnection.getConnection();
    }

    // Recuperar todos os usuários do banco de dados
    @Override
    public List<UserModel> findAll() {
        List<UserModel> userList = new ArrayList<>();

        // Comando MySQL para selecionar todas as tarefas
        String selectQuery = "SELECT * FROM UserTable";

        try {
            // Preparando a declaração SQL
            PreparedStatement ps = this.myConnection.prepareStatement(selectQuery);

            // Executando a consulta SQL
            ResultSet rs = ps.executeQuery();

            // Iterando sobre o resultado para criar objetos TaskModel e adicioná-los à lista
            while (rs.next()) {
                UUID userID = UUID.fromString(rs.getString("UserID"));
                String name = rs.getString("Name");
                String username = rs.getString("Username");
                String password = rs.getString("Password");
                boolean isAdmin = rs.getBoolean("IsAdmin");
                Date createdAt = rs.getDate("CreatedAt");

                UserModel user = new UserModel(userID, name, username, password, createdAt, isAdmin);

                userList.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao recuperar os usuários: " + e.getMessage());
        }

        return userList;
    }

    // Recuperar um usuário por ID do banco de dados
    @Override
    public UserModel findByID(UUID ID) {
        // Inicializar um objeto UserModel
        UserModel user = null;

        // Consulta SQL para selecionar um usuário pelo ID
        String selectQuery = "SELECT * FROM UserTable WHERE UserID = ?";

        try {
            // Criar um PreparedStatement
            PreparedStatement ps = this.myConnection.prepareStatement(selectQuery);

            // Definir o parâmetro UUID
            ps.setString(1, ID.toString());

            // Executar a consulta
            ResultSet rs = ps.executeQuery();

            // Verificar se o conjunto de resultados contém um usuário
            if (rs.next()) {
                // Recuperar detalhes do usuário do conjunto de resultados
                String name = rs.getString("Name");
                String username = rs.getString("Username");
                String password = rs.getString("Password");
                boolean isAdmin = rs.getBoolean("IsAdmin");
                Date createdAt = rs.getDate("CreatedAt");

                // Criar um objeto UserModel
                user = new UserModel(ID, name, username, password, createdAt, isAdmin);
            }
        } catch (SQLException e) {
            // Lidar com exceções SQL
            System.err.println("Erro ao recuperar usuário por ID: " + e.getMessage());
        }

        return user;
    }

    // Função para inserir um usuário no banco de dados
    @Override
    public UserModel insert(UserModel user) {
        // Inicializando variável de usuário inserido
        UserModel userInserted = null;

        // Comando MySQL para inserção
        String insertQuery = "INSERT INTO UserTable (Name, Username, Password, IsAdmin, UserID) VALUES (?, ?, ?, ?, ?)";

        // Fazendo um try/catch para lançamento de Exceptions
        try {
            // Preparando a declaração SQL com a capacidade de recuperar chaves geradas
            PreparedStatement ps = this.myConnection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);

            user.setUserID();

            // Definindo os parâmetros da declaração SQL com base no objeto UserModel fornecido
            ps.setString(1, user.getName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setBoolean(4, user.getAdmin());
            ps.setString(5, user.getUserID().toString()); // Passando o UUID diretamente para o PreparedStatement

            // Executando a declaração SQL
            int rowAffected = ps.executeUpdate();

            // Verificando se a inserção foi bem-sucedida
            if (rowAffected == 1) {
                // Atribuindo o usuário inserido à variável de retorno
                userInserted = user;
            }
        } catch (SQLException e) {
            // Lidando com exceções SQL, imprimindo uma mensagem de erro
            System.err.println("Erro ao criar usuário: " + e.getMessage());
        }

        // Retornando o usuário inserido ou null em caso de falha
        return userInserted;
    }

    @Override
    // Atualizar um usuário no banco de dados
    public boolean update(UserModel user) {
        boolean response = false;

        String updateQuery = "UPDATE UserTable SET Name = ?, Username = ? WHERE UserID = ?";

        try {
            PreparedStatement ps = this.myConnection.prepareStatement(updateQuery);
            ps.setString(1, user.getName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getUserID().toString());

            int rowsAffected = ps.executeUpdate();
            response = rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + user.getUserID() + e.getMessage());
        }

        return response;
    }

    // Excluir um usuário do banco de dados
    @Override
    public boolean delete(UUID userID) {
        // Lógica para deletar um usuário com o ID fornecido
        String deleteQuery = "DELETE FROM UserTable WHERE UserID = ?";
        try {
            PreparedStatement preparedStatement = this.myConnection.prepareStatement(deleteQuery);

            // Setar o parâmetro do ID como uma String
            preparedStatement.setString(1, userID.toString());

            // Executar a instrução DELETE
            int rowsDeleted = preparedStatement.executeUpdate();

            // Verificar se a deleção foi bem-sucedida
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro durante a deleção do usuário: " + e.getMessage());
            return false;
        }
    }

    public static void returnUser(UserModel user) {

        // Imprimir os detalhes do usuário na lista
        System.out.println("UserID: " + user.getUserID());
        System.out.println("Name: " + user.getName());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Admin: " + user.getAdmin());
        System.out.println("CreatedAt " + user.getCreatedAt());
        System.out.println();
    }

    public static void returnUserList(List<UserModel> userList) {

        // Imprimir os detalhes dos usuários na lista
        for (UserModel user : userList) {
            returnUser(user);
        }
    }
}
