package org.SyncTask;

import org.SyncTask.database.TaskDAO;
import org.SyncTask.models.TaskModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MainTask {

    public static void main(String[] args) {
        // Criar uma instância de TaskDAO
        TaskDAO taskDAO = new TaskDAO();

        // Criar uma lista para armazenar tarefas
        List<TaskModel> taskList = new ArrayList<>();

        // Criar uma tarefa e adicioná-la à lista
        TaskModel task1 = new TaskModel();
        task1.setUserID(UUID.fromString("c5106f49-8815-11ee-86b7-40b076d8628f"));
        task1.setTitle("Task Title 1");
        task1.setDescription("Task Description 1");
        task1.setDateEnd(new Date());
        task1.setPriority("High");

        // Adicionar a tarefa ao banco de dados usando o TaskDAO
        TaskModel taskInserted1 = taskDAO.insert(task1);

        // Adicionar a tarefa inserida à lista
        taskList.add(taskInserted1);

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
}
