package org.SyncTask.database;

import org.SyncTask.connection.MyConnection;
import org.SyncTask.models.UserModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Date;

public class UserDAO implements DAO<UserModel> {
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

    // Verifica se o usúario existe
    public boolean isUserExists(String username) {
        Connection connection = MyConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Consulta SQL para verificar se o usuário já existe
            String sql = "SELECT COUNT(*) FROM usertable WHERE username = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fechar recursos
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
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

        // Comando MySQL para atualização
        String updateQuery = "UPDATE UserTable SET Name = ?, Username = ? WHERE UserID = ?";

        try {
            // Preparando a declaração SQL
            PreparedStatement ps = this.myConnection.prepareStatement(updateQuery);

            // Adicionando dados novos no banco de dados
            ps.setString(1, user.getName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getUserID().toString());

            // Atribuindo rowsAffected para a execução do update
            int rowsAffected = ps.executeUpdate();

            // Como a função retorna um boolean, conferimos se rowsAffected é maior que 0, ou seja quantidade de linhas afetadas > 0
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

    public static String returnUser(UserModel user) {

        // Construir uma String com os detalhes da tarefa
        StringBuilder userDetails = new StringBuilder();
        userDetails.append("UserID: ").append(user.getUserID()).append("\n");
        userDetails.append("Name: ").append(user.getName()).append("\n");
        userDetails.append("Username: ").append(user.getUsername()).append("\n");
        userDetails.append("Admin: ").append(user.getAdmin()).append("\n");
        userDetails.append("CreatedAt: ").append(user.getCreatedAt()).append("\n");

        // Retornar a String com os detalhes da tarefa
        return userDetails.toString();
    }

    public static void returnUserList(List<UserModel> userList) {

        // Imprimir os detalhes dos usuários na lista
        for (UserModel user : userList) {
            returnUser(user);
        }
    }
}
