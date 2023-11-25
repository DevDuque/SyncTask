package org.SyncTask;

import org.SyncTask.connection.MyConnection;
import org.SyncTask.exceptions.UserNotFoundException;
import org.SyncTask.models.TaskModel;
import org.SyncTask.models.UserModel;
import org.SyncTask.database.TaskDAO;

import java.sql.Connection;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        // Obtenha uma instância de Connection usando o método getConnection() da classe MyConnection
        Connection connection = MyConnection.getConnection();

        // Crie um scanner para receber a entrada do usuário
        Scanner scanner = new Scanner(System.in);

        // Solicite ao usuário que insira seu nome de usuário e senha
        System.out.print("Digite o nome de usuário: ");
        String username = scanner.nextLine();

        System.out.print("Digite a senha: ");
        String password = scanner.nextLine();

        try {
            // Chame o método authenticateUser da classe MyConnection para autenticar o usuário
            UserModel authenticatedUser = MyConnection.authenticateUser(username, password);

            // Se não ocorrer uma exceção, a autenticação foi bem-sucedida
            System.out.println("Usuário autenticado com sucesso!");

            // Solicite ao usuário que insira os dados da tarefa
            System.out.println("Digite os detalhes da tarefa:");

            System.out.print("Título: ");
            String title = scanner.nextLine();

            System.out.print("Descrição: ");
            String description = scanner.nextLine();

            System.out.print("Prioridade: ");
            String priority = scanner.nextLine();

            // Criar uma nova tarefa com os dados fornecidos
            TaskModel newTask = new TaskModel();
            newTask.setTitle(title);
            newTask.setDescription(description);
            newTask.setPriority(priority);
            newTask.setDateEnd(new Date());
            newTask.setUserID(authenticatedUser.getUserID()); // Defina o UserID com o ID do usuário autenticado

            // Inserir a nova tarefa no banco de dados
            TaskDAO taskDAO = new TaskDAO();
            TaskModel insertedTask = taskDAO.insert(newTask);

            if (insertedTask != null) {
                System.out.println("Tarefa criada com sucesso!");
            } else {
                System.out.println("Falha ao criar a tarefa.");
            }

        } catch (UserNotFoundException e) {
            // Se a exceção UserNotFoundException for lançada, o usuário não foi encontrado
            System.out.println("Usuário não encontrado. Autenticação falhou.");
        } finally {
            // Certifique-se de fechar o scanner para evitar vazamento de recursos
            scanner.close();
        }
    }
}
