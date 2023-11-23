package org.SyncTask;

import org.SyncTask.database.TaskDAO;
import org.SyncTask.models.TaskModel;
import org.SyncTask.models.UserModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MainTask {

    public static void imprimirLista(List<TaskModel> taskList) {
        // Imprimir os detalhes das tarefas na lista
        for (TaskModel task : taskList) {
            System.out.println("TaskID: " + task.getTaskID());
            System.out.println("UserID: " + task.getUserID());
            System.out.println("Title: " + task.getTitle());
            System.out.println("Description: " + task.getDescription());
            System.out.println("DateEnd: " + task.getDateEnd());
            System.out.println("Priority: " + task.getPriority());
            System.out.println();
        }
    }

    public static void imprimirTarefa(TaskModel task) {

        // Imprimir os detalhes de tarefa na lista
        System.out.println("TaskID: " + task.getTaskID());
        System.out.println("UserID: " + task.getUserID());
        System.out.println("Title: " + task.getTitle());
        System.out.println("Description: " + task.getDescription());
        System.out.println("DateEnd: " + task.getDateEnd());
        System.out.println("Priority: " + task.getPriority());
        System.out.println();
    }

    public static void main(String[] args) {

        // Criando uma instância de TaskDAO
        TaskDAO taskDAO = new TaskDAO();

        // Criar uma tarefa
        TaskModel task = new TaskModel();
        task.setTaskID();
        task.setUserID(UUID.fromString("bb06b508-befd-4806-ade2-a9c68c76d7ed"));
        task.setTitle("Título da Tarefa");
        task.setDescription("Descrição");
        task.setDateEnd(new Date());
        task.setPriority("Alta");

        // Adicionar a tarefa ao banco de dados usando o TaskDAO
        taskDAO.insert(task);

        // Imprimindo a tarefa criada
        System.out.println("Tarefa criada com sucesso! \nID: " + task.getTaskID() + "\nUserID = " + task.getUserID() + "\n");

        // FIND ALL
        // Recuperar todas as tarefas do banco de dados e salvando em uma lista
        List<TaskModel> taskList = taskDAO.findAll();

        // Imprimir os detalhes das tarefas recuperadas
        System.out.println("Todas as tarefas no banco de dados: ");
        imprimirLista(taskList);

        //BUSCA POR ID

        // Escolher um ID existente (substitua 'algumID' pelo UUID real)
        UUID someID = UUID.fromString("ad65251b-74f0-4f24-b424-20d116dfe9ea");

        // Recuperar uma tarefa pelo ID
        TaskModel taskByID = taskDAO.findByID(someID);

        // Imprimir os detalhes de tarefa recuperada
        if(taskByID != null) {
            System.out.println("Tarefa encontrada por ID:");
            imprimirTarefa(taskByID);
        }

        try {
            if(taskByID != null) {
                // Atualizando dados de tarefas
                taskByID.setTitle("Um novo título");
                taskByID.setDescription("Uma nova descrição");
                taskByID.setDateEnd(new Date());
                taskByID.setPriority("Média");

                // Chamando a função de atualização no banco de dados
                boolean updateSuccessful = taskDAO.update(taskByID);

                // Verificar se a atualização foi bem-sucedida
                if(updateSuccessful) {
                    System.out.println("Tarefa atualizada com sucesso no banco de dados");
                    imprimirTarefa(taskByID);
                } else {
                    throw new Exception("Erro ao atualizar usuário no banco de dados");
                }
            } else {
                throw new Exception("Tarefa não encontrada na lista.");
            }
        } catch (Exception e) {
            System.err.println("Erro durante a atualização da tarefa: " + e.getMessage());
        }
    }
}
