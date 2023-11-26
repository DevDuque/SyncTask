package org.SyncTask;

// Importações do próprio projeto
import org.SyncTask.database.TaskDAO;
import org.SyncTask.models.TaskModel;

// Importações do Java
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.SyncTask.database.TaskDAO.*;

public class MainTask {

    public static void main(String[] args) {

        // Criando uma instância de TaskDAO
        TaskDAO taskDAO = new TaskDAO();

//        // Criar uma tarefa
//        TaskModel task = new TaskModel();
//        task.setTaskID();
//        task.setUserID(UUID.fromString("12beef2f-1b52-4b23-ba3c-addf69c28a45"));
//        task.setTitle("Título da Tarefa");
//        task.setDescription("Descrição");
//        task.setDateEnd(new Date());
//        task.setPriority("Alta");
//
//        // Adicionar a tarefa ao banco de dados usando o TaskDAO
//        taskDAO.insert(task);
//
//        // Imprimindo a tarefa criada
//        System.out.println("Tarefa criada com sucesso! \nTaskID: " + task.getTaskID() + "\nUserID = " + task.getUserID() + "\n");

        // DELETE USER

        // Escolher um ID existente para deletar (substitua 'algumID' pelo UUID real)
        UUID taskIDToDelete = UUID.fromString("eba920c8-1afa-49a9-b186-4194d8728438");

        // Tentar deletar a tarefa com o ID especificado
        boolean deletionSuccessful = taskDAO.delete(taskIDToDelete);

        // Verificar se a deleção foi bem-sucedida
        if (deletionSuccessful) {
            System.out.println("Tarefa deletada com sucesso do banco de dados.");
        } else {
            System.out.println("Falha ao deletar a tarefa do banco de dados.");
        }

        // FIND ALL
        // Recuperar todas as tarefas do banco de dados e salvando em uma lista
        List<TaskModel> taskList = taskDAO.findAll();

        // Imprimir os detalhes das tarefas recuperadas
        System.out.println("Todas as tarefas no banco de dados: ");
        returnTaskList(taskList);

        //BUSCA POR ID

        // Escolher um ID existente (substitua 'algumID' pelo UUID real)
        UUID someID = UUID.fromString("410e890d-9b60-4a25-88c1-63d0c7dd40eb");

        // Recuperar uma tarefa pelo ID
        TaskModel taskByID = taskDAO.findByID(someID);

        // Imprimir os detalhes de tarefa recuperada
        if(taskByID != null) {
            System.out.println("Tarefa encontrada por ID:");
            returnTask(taskByID);
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
                    returnTask(taskByID);
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