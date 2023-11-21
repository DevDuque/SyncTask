package org.SyncTask.database;

import org.SyncTask.connection.MyConnection;
import org.SyncTask.models.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class UserDAO extends DAO<UserModel> {
    private Connection myConnection;

    // Construtor que obtém uma conexão do banco de dados ao instanciar o UserDAO
    public UserDAO() {
        this.myConnection = MyConnection.getConnection();
    }

    // Recuperar todos os usuários do banco de dados
    @Override
    public List<UserModel> findAll() {
        return null;
    }

    // Recuperar um usuário por ID do banco de dados
    @Override
    public UserModel findByID(UUID ID) {
        return null;
    }

    // Função para inserir um usuário no banco de dados
    @Override
    public UserModel insert(UserModel user) {
        // Inicializando variável de usuário inserido
        UserModel userInserted = null;

        // Comando MySQL para inserção
        String insertQuery = "INSERT INTO UserTable (Name, Username, Password, IsAdmin) VALUES (?, ?, ?, ?)";

        // Fazendo um try/catch para lançamento de Exceptions
        try {
            // Preparando a declaração SQL com a capacidade de recuperar chaves geradas
            PreparedStatement ps = this.myConnection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);

            // Definindo os parâmetros da declaração SQL com base no objeto UserModel fornecido
            ps.setString(1, user.getName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setBoolean(4, user.getAdmin());

            // Executando a declaração SQL
            int rowAffected = ps.executeUpdate();

            // Verificando se a inserção foi bem-sucedida
            if(rowAffected == 1) {
                // Gerando um ID único para o usuário inserido
                user.generateUserID();

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

    // Atualizar um usuário no banco de dados
    @Override
    public boolean update(UserModel OBJ) {
        return false;
    }

    // Excluir um usuário do banco de dados
    @Override
    public boolean delete(UserModel OBJ) {
        return false;
    }
}
