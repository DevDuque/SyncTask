package org.SyncTask;

import org.SyncTask.connection.MyConnection;
import org.SyncTask.exceptions.InvalidPasswordException;
import org.SyncTask.exceptions.UserNotFoundException;
import org.SyncTask.models.TaskModel;
import org.SyncTask.models.UserModel;
import org.SyncTask.database.TaskDAO;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Obtenha uma instância de Connection usando o método getConnection() da classe MyConnection
            MyConnection.getConnection();

            TaskDAO taskDAO = new TaskDAO();

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

                // Exiba um menu interativo
                int choice;
                do {
                    System.out.println("Escolha uma opção:");
                    System.out.println("1. Criar nova tarefa");
                    System.out.println("2. Listar tarefas");
                    System.out.println("0. Sair");
                    System.out.print("Opção: ");
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consumir a quebra de linha pendente

                    switch (choice) {
                        case 1:
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
                            newTask.setUserID(authenticatedUser.getUserID()); // Define o UserID com o ID do usuário autenticado

                            // Exibir informações da tarefa antes de inserir
                            System.out.println("\nConfirme os detalhes da tarefa:");
                            TaskDAO.returnTask(newTask);

                            // Dar ao usuário a opção de confirmar ou voltar ao início
                            System.out.println("Escolha uma opção:");
                            System.out.println("1. Confirmar e inserir a tarefa");
                            System.out.println("0. Voltar ao início sem inserir");
                            System.out.print("Opção: ");
                            int confirmOption = scanner.nextInt();
                            scanner.nextLine(); // Consumir a quebra de linha pendente

                            if (confirmOption == 1) {
                                // Inserir a nova tarefa no banco de dados
                                TaskModel insertedTask = taskDAO.insert(newTask);

                                if (insertedTask != null) {
                                    System.out.println("Tarefa criada com sucesso!");
                                } else {
                                    System.out.println("Falha ao criar a tarefa.");
                                }
                            } else if (confirmOption == 0) {
                                System.out.println("Retornando ao início. Nenhuma tarefa inserida.");
                            } else {
                                System.out.println("Opção inválida. Retornando ao início. Nenhuma tarefa inserida.");
                            }
                            break;

                        case 2:
                            // Recuperar e exibir todas as tarefas do usuário
                            List<TaskModel> userTasks = taskDAO.findByUserID(authenticatedUser.getUserID());
                            System.out.println("Tarefas do usuário " + authenticatedUser.getUsername());
                            TaskDAO.returnTaskList(userTasks);
                            break;

                        case 0:
                            System.out.println("Saindo do programa. Até logo!");
                            break;

                        default:
                            System.out.println("Opção inválida. Tente novamente.");
                            break;
                    }
                } while (choice != 0);

            } catch (UserNotFoundException e) {
                // Se a exceção UserNotFoundException for lançada, a autenticação falhou por causa do usuário
                System.out.println("Usuário não encontrado. Autenticação falhou.");

            } catch (InvalidPasswordException e) {
                // Se a exceção InvalidPasswordException for lançada, a autenticação falhou por causa da senha
                System.out.println("Senha inválida. Autenticação falhou.");
            }
        }
    }
}
