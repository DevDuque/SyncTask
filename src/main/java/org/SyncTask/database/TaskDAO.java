package org.SyncTask.database;

import org.SyncTask.connection.MyConnection;
import org.SyncTask.models.TaskModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class TaskDAO extends DAO<TaskModel> {
    private Connection myConnection;

    public TaskDAO() {
        this.myConnection = MyConnection.getConnection();
    }

    @Override
    public List<TaskModel> findAll() {
        // Implementação ausente: Deve ser usada para recuperar todas as tarefas do banco de dados
        return null;
    }

    @Override
    public TaskModel findByID(UUID ID) {
        // Implementação ausente: Deve ser usada para recuperar uma tarefa por ID do banco de dados
        return null;
    }

    @Override
    public TaskModel insert(TaskModel task) {
        // Inicializando variável de tarefa inserida
        TaskModel taskInserted = null;

        // Comando MySQL para inserção
        String insertQuery = "INSERT INTO TaskTable (UserID, Title, Description, DateEnd, Priority, TaskID) VALUES (?, ?, ?, ?, ?, ?)";

        // Fazendo um try/catch para lançamento de Exceptions
        try {
            // Preparando a declaração SQL com a capacidade de recuperar chaves geradas
            PreparedStatement ps = this.myConnection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);

            task.setTaskID();

            // Definindo os parâmetros da declaração SQL com base no objeto TaskModel fornecido
            ps.setObject(1, task.getUserID().toString());
            ps.setString(2, task.getTitle());
            ps.setString(3, task.getDescription());

            // Convertendo java.util.Date para java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(task.getDateEnd().getTime());
            ps.setDate(4, sqlDate);
            ps.setString(5, task.getPriority());
            ps.setString(6, task.getTaskID().toString());

            // Executando a declaração SQL
            int rowAffected = ps.executeUpdate();

            // Verificando se a inserção foi bem-sucedida
            if(rowAffected == 1) {
                // Atribuindo a tarefa inserida à variável de retorno
                taskInserted = task;
            }
        } catch (SQLException e) {
            // Lidando com exceções SQL, imprimindo uma mensagem de erro
            System.err.println("Erro ao criar tarefa: " + e.getMessage());
        }

        // Retornando a tarefa inserida ou null em caso de falha
        return taskInserted;
    }

    @Override
    public boolean update(TaskModel OBJ) {
        // Implementação ausente: Deve ser usada para atualizar uma tarefa no banco de dados
        return false;
    }

    @Override
    public boolean delete(TaskModel OBJ) {
        // Implementação ausente: Deve ser usada para excluir uma tarefa do banco de dados
        return false;
    }
}
