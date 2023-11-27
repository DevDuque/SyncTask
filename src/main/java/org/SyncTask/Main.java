package org.SyncTask;

// DAO's
import org.SyncTask.database.UserDAO;
import org.SyncTask.database.TaskDAO;

// Conexão com banco
import org.SyncTask.connection.MyConnection;

// Exceptions
import org.SyncTask.exceptions.UserNotFoundException;
import org.SyncTask.exceptions.InvalidPasswordException;

// Models
import org.SyncTask.models.TaskModel;
import org.SyncTask.models.UserModel;

// Java
import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Obtenha uma instância de Connection usando o método getConnection() da classe MyConnection
        MyConnection.getConnection();

        TaskDAO taskDAO = new TaskDAO();
        UserDAO userDAO = new UserDAO();

        boolean exitProgram = false;

        while (!exitProgram) {
            // Exibir um menu inicial
            String[] loginOptions = {"Cadastrar Usuario", "Logar Usuario", "Sair"};
            int loginChoice = JOptionPane.showOptionDialog(null, "Escolha uma opcao:", "Menu Inicial",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, loginOptions, loginOptions[0]);

            switch (loginChoice) {
                case 0:
                    // Cadastrando um novo usuário
                    String newName = JOptionPane.showInputDialog("Digite o seu nome:");
                    String newUsername = JOptionPane.showInputDialog("Digite o nome de usuario:");
                    String newPassword = JOptionPane.showInputDialog("Digite a sua senha:");

                    // Adicionando escolha de admin
                    int adminChoice = JOptionPane.showConfirmDialog(null, "Voce e um administrador?" , "Escolha", JOptionPane.YES_NO_OPTION);

                    boolean isAdmin = (adminChoice == JOptionPane.YES_OPTION);

                    // Criando um novo usuário
                    UserModel newUser = new UserModel();
                    newUser.setName(newName);
                    newUser.setUsername(newUsername);
                    newUser.setPassword(newPassword);
                    newUser.setAdmin(isAdmin);

                    // Salvando no banco usando UserDAO
                    UserModel userCreated = userDAO.insert(newUser);

                    if(userCreated != null) {
                        JOptionPane.showMessageDialog(null, "Usuario criado com sucesso! \nNome: " + newUser.getName() + "\nUserID = " + newUser.getUserID() + "\n");
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao criar um usuario!");
                    }
                    break;

                case 1:
                    // Logando usuário
                    String username = JOptionPane.showInputDialog("Digite o nome de usuario:");
                    String password = JOptionPane.showInputDialog("Digite sua senha:");

                    try {
                        UserModel authenticatedUser = MyConnection.authenticateUser(username, password);
                        JOptionPane.showMessageDialog(null, "Usuario autenticado com sucesso!");

                        // Exibir um menu interativo
                        int choice;
                        do {
                            String[] options = {"Criar nova tarefa", "Listar tarefas", "Sair da sua conta"};
                            choice = JOptionPane.showOptionDialog(null, "Escolha uma opcao:", "Menu",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                                    null, options, options[0]);

                        switch (choice) {
                            case 0:
                                // Solicitar ao usuário que insira os dados da tarefa usando JOptionPane
                                String title = JOptionPane.showInputDialog("Titulo:");
                                String description = JOptionPane.showInputDialog("Descricao:");
                                String priority = JOptionPane.showInputDialog("Prioridade:");
                                String dateEndStr = JOptionPane.showInputDialog("Data de Termino (formato dd/mm/yyyy):");

                                // Convertendo a string da data de término para um objeto Date
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                Date dateEnd = null;
                                try {
                                    dateEnd = dateFormat.parse(dateEndStr);
                                } catch (ParseException e) {
                                    JOptionPane.showMessageDialog(null, "Formato de data inválido. Certifique-se de usar o formato dd/mm/yyyy.");
                                    return;
                                }

                                    // Criar uma nova tarefa com os dados fornecidos
                                    TaskModel newTask = new TaskModel();
                                    newTask.setTitle(title);
                                    newTask.setDescription(description);
                                    newTask.setPriority(priority);
                                    newTask.setDateEnd(dateEnd);
                                    newTask.setUserID(authenticatedUser.getUserID()); // Define o UserID com o ID do usuário autenticado

                                    String taskDetailsString = TaskDAO.returnTask(newTask);

                                    // Dar ao usuário a opção de confirmar ou voltar ao início
                                Object[] confirmOptions = {"Confirmar e inserir a tarefa", "Voltar ao inicio sem inserir"};
                                int confirmOption = JOptionPane.showOptionDialog(null,
                                                    "Tarefa a ser inserida \n" + taskDetailsString, "Confirmacao", JOptionPane.DEFAULT_OPTION,
                                                    JOptionPane.PLAIN_MESSAGE, null, confirmOptions, confirmOptions[0]);

                                if (confirmOption == 0) {
                                    // Inserir a nova tarefa no banco de dados
                                    TaskModel insertedTask = taskDAO.insert(newTask);

                                    if (insertedTask != null) {
                                        JOptionPane.showMessageDialog(null, "Tarefa criada com sucesso!");
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Falha ao criar a tarefa.");
                                                }
                                } else if (confirmOption == 1) {
                                    JOptionPane.showMessageDialog(null, "Retornando ao inicio. Nenhuma tarefa inserida.");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Opcao invalida. Retornando ao inicio. Nenhuma tarefa inserida.");
                                }
                                break;

                                case 1:
                                    // Recuperar e exibir todas as tarefas do usuário
                                    List<TaskModel> userTasks = taskDAO.findByUserID(authenticatedUser.getUserID());
                                    StringBuilder taskList = new StringBuilder("Tarefas do usuario " + authenticatedUser.getUsername() + ":\n");
                                    for (TaskModel task : userTasks) {
                                        taskList.append(TaskDAO.toString(task)).append("\n");
                                    }
                                    JOptionPane.showMessageDialog(null, taskList.toString());
                                    break;

                                case 2:
                                    JOptionPane.showMessageDialog(null, "Saindo do programa. Ate logo!");
                                    break;

                                default:
                                    JOptionPane.showMessageDialog(null, "Opcao invalida. Tente novamente.");
                                    break;
                            }
                        } while (choice != 2);

                    } catch (UserNotFoundException | InvalidPasswordException e) {
                        JOptionPane.showMessageDialog(null, "Falha na autenticacao: " + e.getMessage());
                    }
                    break;

                case 2:
                    // Sair do programa
                    exitProgram = true;
                    break;
            }
        }
    }
}
