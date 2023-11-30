package org.SyncTask.database;

import org.SyncTask.connection.MyConnection;
import org.SyncTask.models.TaskModel;

import org.SyncTask.exceptions.InvalidTaskDateException;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TaskDAO implements DAO<TaskModel> {

    private Connection myConnection;

    public TaskDAO() {
        this.myConnection = MyConnection.getConnection();
    }

    @Override
    public List<TaskModel> findAll() {
        List<TaskModel> taskList = new ArrayList<>();

        // Comando MySQL para selecionar todas as tarefas
        String selectAllQuery = "SELECT * FROM tasktable";

        try {
            // Preparando a declaração SQL
            PreparedStatement ps = this.myConnection.prepareStatement(selectAllQuery);

            // Executando a consulta SQL
            ResultSet rs = ps.executeQuery();

            // Iterando sobre o resultado para criar objetos TaskModel e adicioná-los à lista
            while (rs.next()) {
                UUID taskID = UUID.fromString(rs.getString("TaskID"));
                UUID userID = UUID.fromString(rs.getString("UserID"));
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String priority = rs.getString("Priority");
                java.sql.Date sqlDate = rs.getDate("DateEnd");
                Date dateEnd = new Date(sqlDate.getTime());
                Date createdAt = rs.getTimestamp("CreatedAt");

                TaskModel task = new TaskModel(taskID, userID, title, description, priority, dateEnd, createdAt);
                taskList.add(task);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao recuperar todas as tarefas: " + e.getMessage());
        }

        return taskList;
    }

    @Override
    public TaskModel findByID(UUID ID) {
        // Comando MySQL para selecionar uma tarefa por ID
        String selectByIDQuery = "SELECT * FROM TaskTable WHERE TaskID = ?";

        try {
            // Preparando a declaração SQL
            PreparedStatement ps = this.myConnection.prepareStatement(selectByIDQuery);
            ps.setString(1, ID.toString());

            // Executando a consulta SQL
            ResultSet rs = ps.executeQuery();

            // Verificando se a consulta retornou algum resultado
            if (rs.next()) {
                UUID taskID = UUID.fromString(rs.getString("TaskID"));
                UUID userID = UUID.fromString(rs.getString("UserID"));
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String priority = rs.getString("Priority");
                java.sql.Date sqlDate = rs.getDate("DateEnd");
                Date dateEnd = new Date(sqlDate.getTime());
                Date createdAt = rs.getTimestamp("CreatedAt");

                return new TaskModel(taskID, userID, title, description, priority, dateEnd, createdAt);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao recuperar a tarefa por ID: " + e.getMessage());
        }

        // Se a tarefa não for encontrada, retorna null
        return null;
    }

    // Metodo para retornar uma lista de tarefas do usuário pedido
    public List<TaskModel> findByUserID(UUID userID) {
        List<TaskModel> taskList = new ArrayList<>();

        // Comando MySQL para selecionar todas as tarefas de um usuário específico
        String selectByUserIDQuery = "SELECT * FROM TaskTable WHERE UserID = ?";

        try {
            // Preparando a declaração SQL
            PreparedStatement ps = this.myConnection.prepareStatement(selectByUserIDQuery);
            ps.setString(1, userID.toString());

            // Executando a consulta SQL
            ResultSet rs = ps.executeQuery();

            // Iterando sobre o resultado para criar objetos TaskModel e adicioná-los à lista
            while (rs.next()) {
                UUID taskID = UUID.fromString(rs.getString("TaskID"));
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String priority = rs.getString("Priority");
                java.sql.Date sqlDate = rs.getDate("DateEnd");
                Date dateEnd = new Date(sqlDate.getTime());
                Date createdAt = rs.getTimestamp("CreatedAt");

                TaskModel task = new TaskModel(taskID, userID, title, description, priority, dateEnd, createdAt);
                taskList.add(task);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao recuperar tarefas por UserID: " + e.getMessage());
        }

        return taskList;
    }

    public boolean deleteTasksByUserId(String userId) {
        try {
            PreparedStatement preparedStatement = this.myConnection.prepareStatement("DELETE FROM TaskTable WHERE UserID = ?");

            preparedStatement.setString(1, userId);
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace(); // Trate a exceção apropriadamente em um ambiente de produção
            return false;
        }
    }

    @Override
    public TaskModel insert(TaskModel task) {
        // Inicializando variável de tarefa inserida
        TaskModel taskInserted = null;

        // Comando MySQL para inserção
        String insertQuery = "INSERT INTO TaskTable (TaskID, UserID, Title, Description, DateEnd, Priority) VALUES (?, ?, ?, ?, ?, ?)";

        // Fazendo um try/catch para lançamento de Exceptions
        try {

            // Verificar se DateEnd é maior que CreatedAt
            if (task.getDateEnd().before(task.getCreatedAt())) {
                throw new InvalidTaskDateException("Erro: A data de termino da tarefa nao pode ser anterior a data de criacao.");
            }
            // Preparando a declaração SQL com a capacidade de recuperar chaves geradas
            PreparedStatement ps = this.myConnection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);

            // Criando um TaskID
            task.setTaskID();

            // Definindo os parâmetros da declaração SQL com base no objeto TaskModel fornecido
            ps.setString(1, task.getTaskID().toString());
            ps.setString(2, task.getUserID().toString());
            ps.setString(3, task.getTitle());
            ps.setString(4, task.getDescription());

            // Convertendo java.util.Date para java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(task.getDateEnd().getTime());
            ps.setDate(5, sqlDate);
            ps.setString(6, task.getPriority());

            // Executando a declaração SQL
            int rowAffected = ps.executeUpdate();

            // Verificando se a inserção foi bem-sucedida
            if (rowAffected == 1) {
                // Atribuindo a tarefa inserida à variável de retorno
                taskInserted = task;
            } else {
                System.err.println("Nenhuma linha afetada ao inserir a tarefa. A insercao falhou.");
            }
        } catch (SQLException e) {
            // Lidando com exceções SQL, imprimindo uma mensagem de erro
            System.err.println("Erro ao criar tarefa: " + e.getMessage());
            e.printStackTrace(); // Adicionando rastreamento completo da exceção
        } catch (InvalidTaskDateException e) {
            // Lidando com exceções e retornando null
            JOptionPane.showMessageDialog(null, e.getMessage());
            return null;
        }

        // Retornando a tarefa inserida ou null em caso de falha
        return taskInserted;
    }

    @Override
    public boolean update(TaskModel task) {
        // Atualizar uma tarefa no banco de dados
        boolean response = false;

        // Comando MySQL para atualização
        String updateQuery = "UPDATE TaskTable SET Title = ?, Description = ?, Priority = ?, DateEnd = ? WHERE TaskID = ?";


        try {
            // Verificar se DateEnd é maior que CreatedAt
            if (task.getDateEnd().before(task.getCreatedAt())) {
                throw new InvalidTaskDateException("Erro: A data de termino da tarefa nao pode ser anterior a data de criacao.");
            }

            // Preparando a declaração SQL
            PreparedStatement ps = this.myConnection.prepareStatement(updateQuery);

            // Adicionando dados novos no banco de dados
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getPriority());
            ps.setString(5, task.getTaskID().toString());


            // Convertendo java.util.Date para java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(task.getDateEnd().getTime());
            ps.setDate(4, sqlDate);

            // Atribuindo rowsAffected para a execução do update
            int rowsAffected = ps.executeUpdate();

            // Como a função retorna um boolean, conferimos se rowsAffected é maior que 0, ou seja quantidade de linhas afetadas > 0
            response = rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar tarefa: " + task.getTaskID() + e.getMessage());
        } catch (InvalidTaskDateException e) {
            // Lidando com exceções e retornando null
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }

        return response;
    }

    @Override
    public boolean delete(UUID taskID) {
        // Lógica para deletar uma tarefa com o ID fornecido
        String deleteQuery = "DELETE FROM TaskTable WHERE taskID = ?";
        try {
            PreparedStatement preparedStatement = this.myConnection.prepareStatement(deleteQuery);

            // Setar o parâmetro do ID
            preparedStatement.setString(1, taskID.toString());

            // Executar a instrução DELETE
            int rowsDeleted = preparedStatement.executeUpdate();

            // Verificar se a deleção foi bem-sucedida
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.err.println("Erro durante a delecao da tarefa: " + e.getMessage());
            return false;
        }
    }

    public static void returnTaskList(List<TaskModel> taskList) {
        // Verificar se a lista de tarefas está vazia
        if (taskList.isEmpty()) {
            System.out.println("Sem tarefas registradas, comece a criar tarefas, aqui!");
        } else {
            // Imprimir os detalhes das tarefas na lista
            for (TaskModel task : taskList) {
                returnTask(task);
            }
        }
    }

    public static String returnTask(TaskModel task) {
        // Construir uma String com os detalhes da tarefa
        StringBuilder taskDetails = new StringBuilder();
        taskDetails.append("TaskID: ").append(task.getTaskID()).append("\n");
        taskDetails.append("UserID: ").append(task.getUserID()).append("\n");
        taskDetails.append("Title: ").append(task.getTitle()).append("\n");
        taskDetails.append("Description: ").append(task.getDescription()).append("\n");
        taskDetails.append("DateEnd: ").append(task.getDateEnd()).append("\n");
        taskDetails.append("Priority: ").append(task.getPriority()).append("\n\n");

        // Retornar a String com os detalhes da tarefa
        return taskDetails.toString();
    }
}
