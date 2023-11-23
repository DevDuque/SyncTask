package org.SyncTask;

import org.SyncTask.database.TaskDAO;
import org.SyncTask.models.TaskModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MainTask {

    public static void imprimirLista(List<TaskModel> taskList) {
        // Imprimir os detalhes das tarefas na lista
        for (TaskModel task : taskList) {
            System.out.println("Task details:");
            System.out.println("TaskID: " + task.getTaskID());
            System.out.println("UserID: " + task.getUserID());
            System.out.println("Title: " + task.getTitle());
            System.out.println("Description: " + task.getDescription());
            System.out.println("DateEnd: " + task.getDateEnd());
            System.out.println("Priority: " + task.getPriority());
            System.out.println();
        }
    }

    public static void main(String[] args) {
        TaskDAO taskDAO = new TaskDAO();

        // Criar uma lista para armazenar tarefas
        List<TaskModel> taskList = new ArrayList<>();

        // Criar uma tarefa e adicioná-la à lista
        TaskModel task = new TaskModel();
        task.setTaskID();
        task.setUserID(UUID.fromString("c5864cac-7491-4c5c-851d-2cec8862a153"));
        task.setTitle("Título da Tarefa");
        task.setDescription("Descrição");
        task.setDateEnd(new Date());
        task.setPriority("Alta");

        // Adicionar a tarefa ao banco de dados usando o TaskDAO
        TaskModel taskInserted = taskDAO.insert(task);

        // Adicionar a tarefa inserida à lista
        taskList.add(taskInserted);

        imprimirLista(taskList);

        // Inicializando ID para atualização
        UUID updateID = taskInserted.getTaskID();
        TaskModel taskUpdated = null;

        // Procurando o usuário na lista de usuários
        for(TaskModel tasks : taskList) {
            if(tasks.getTaskID().equals(updateID)) {
                taskUpdated = task;

                break;
            }
        }

        try {
            if(taskUpdated != null) {
                // Atualizando dados de tarefas
                taskUpdated.setTitle("Um novo título");
                taskUpdated.setDescription("Uma nova descrição");
                taskUpdated.setDateEnd(new Date());
                taskUpdated.setPriority("Média");

                // Chamando a função de atualização no banco de dados
                boolean updateSuccessful = taskDAO.update(taskUpdated);

                // Verificar se a atualização foi bem-sucedida
                if(updateSuccessful) {
                    System.out.println("Tarefa atualizada com sucesso no banco de dados");
                    imprimirLista(taskList);
                } else {
                    throw new Exception("Erro ao atualizar usuário no banco de dados");
                }
            } else {
                throw new Exception("Tarefa não encontrada na lista.");
            }
        } catch (Exception e) {
            System.err.println("Erro durante a atualização da tarefa: " + e.getMessage());
        }


        // BUSCA POR ID

        //System.out.println("TESTE findByID");
        // Substitua o UUID a seguir pelo ID real de uma tarefa existente no banco de dados
        //UUID taskIDToFind = UUID.fromString("coloque-aqui-o-uuid-da-tarefa");

        // Obtendo uma tarefa por ID do banco de dados
        //TaskModel foundTask = taskDAO.findByID(taskIDToFind);

        // Verificando se a tarefa foi encontrada
        //if (foundTask != null) {
            //System.out.println("Tarefa encontrada por ID:");
            //System.out.println("TaskID: " + foundTask.getTaskID());
            //System.out.println("UserID: " + foundTask.getUserID());
            //System.out.println("Title: " + foundTask.getTitle());
            //System.out.println("Description: " + foundTask.getDescription());
            //System.out.println("DateEnd: " + foundTask.getDateEnd());
            //System.out.println("Priority: " + foundTask.getPriority());
        //} else {
            //System.out.println("Tarefa não encontrada por ID.");
        //}
        //System.out.println("FIM TESTE findByID");


        // ALL FIND

        //System.out.println("Teste ALLFIND");

        // Obtendo todas as tarefas do banco de dados
        //List<TaskModel> allTasks = taskDAO.findAll();

        // Imprimindo detalhes das tarefas
        //imprimirLista(allTasks);

        //System.out.println("FIM Teste ALLFIND");

    }
}
