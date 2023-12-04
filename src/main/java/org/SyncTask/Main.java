package org.SyncTask;

// DAO's
import org.SyncTask.database.UserDAO;
import org.SyncTask.database.TaskDAO;

// Conexão com banco
import org.SyncTask.connection.MyConnection;

// Exceptions
import org.SyncTask.exceptions.UserExistsException;
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

    // Função para escolher uma tarefa de uma lista
    // Criando uma lista de strings com os dados da tarefa para facilitar na hora de inserir pelo ID, visto que o TaskID é algo auto incrementavel com 36 espaços.
// No método chooseTaskFromList da classe Main
    private static TaskModel chooseTaskFromList(List<TaskModel> taskList) {
        if (taskList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Sem tarefas registradas, comece a criar tarefas, aqui!");
            return null;
        }

        StringBuilder taskOptions = new StringBuilder("Escolha uma tarefa:\n");
        for (int i = 0; i < taskList.size(); i++) {
            // Exibir informações formatadas da tarefa
            taskOptions.append(i + 1).append(". ").append(TaskDAO.returnTask(taskList.get(i))).append("\n");
        }

        int taskChoice;
        try {
            taskChoice = Integer.parseInt(JOptionPane.showInputDialog(taskOptions.toString())) - 1;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Escolha invalida.");
            return null;
        }

        if (taskChoice >= 0 && taskChoice < taskList.size()) {
            return taskList.get(taskChoice);
        } else {
            JOptionPane.showMessageDialog(null, "Escolha invalida.");
            return null;
        }
    }

    // Método para conferir se o usuário autenticado é Admin, fazendo com que coisas sejam limitadas
    private static boolean isAdminUserAuthenticated(UserModel user) {
        return user != null && user.getAdmin();
    }

    // Função para criar usuário
    private static void createUser(UserDAO userDAO) {
        String newName = JOptionPane.showInputDialog("Digite o seu nome:");
        String newUsername = JOptionPane.showInputDialog("Digite o nome de usuario:");
        String newPassword = JOptionPane.showInputDialog("Digite a sua senha:");

        int adminChoice = JOptionPane.showConfirmDialog(null, "Voce e um administrador?" , "Escolha", JOptionPane.YES_NO_OPTION);
        boolean isAdmin = (adminChoice == JOptionPane.YES_OPTION);

        // Criar uma nova usuário com os dados fornecidos
        UserModel newUser = new UserModel();
        newUser.setName(newName);
        newUser.setUsername(newUsername);
        newUser.setPassword(newPassword);
        newUser.setAdmin(isAdmin);

        try {
            // Verificar se o usuário já existe antes de inserir
            if (userDAO.isUserExists(newUsername)) {
                throw new UserExistsException("Usuario ja existe no banco de dados.");
            }

            UserModel userCreated = userDAO.insert(newUser);

            if (userCreated != null) {
                JOptionPane.showMessageDialog(null, "Usuario criado com sucesso! \nNome: " + newUser.getName() + "\nUserID = " + newUser.getUserID() + "\n");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao criar um usuario!");
            }
        } catch (UserExistsException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }

    // Criando uma nova tarefa com a certeza do usuário
    private static void createTask(TaskDAO taskDAO, UserModel authenticatedUser) {
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

        // Mostrando ao usuário a tarefa a ser inserida para ele confirmar
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
        } else {
            JOptionPane.showMessageDialog(null, "Retornando ao inicio. Nenhuma tarefa inserida.");
        }
    }

    // Adicionando a lista de tarefas para o usuário autenticado
    private static void listTask(TaskDAO taskDAO, UserModel authenticatedUser) {
        // Recuperar e exibir todas as tarefas do usuário
        List<TaskModel> userTasks = taskDAO.findAll();
        StringBuilder taskList = new StringBuilder("Tarefas do usuario " + authenticatedUser.getUsername() + ":\n");

        if (!userTasks.isEmpty()) {
            for (TaskModel task : userTasks) {
                taskList.append(TaskDAO.returnTask(task)).append("\n");
            }
            JOptionPane.showMessageDialog(null, taskList.toString());
        } else {
            JOptionPane.showMessageDialog(null, "Sem tarefas registradas, comece a criar tarefas, aqui!");
        }
    }



    // Atualizando uma tarefa
    private static void updateTask(TaskDAO taskDAO, UserModel authenticatedUser) {
        // Recuperar e exibir todas as tarefas do usuário
        List<TaskModel> userTasks = taskDAO.findByUserID(authenticatedUser.getUserID());
        TaskModel taskToUpdate = chooseTaskFromList(userTasks);

        if (taskToUpdate != null) {
            // Solicitar ao usuário as informações atualizadas
            String newTitle = JOptionPane.showInputDialog("Novo Titulo:");
            String newDescription = JOptionPane.showInputDialog("Nova Descricao:");
            String newPriority = JOptionPane.showInputDialog("Nova Prioridade:");
            String newDateEndStr = JOptionPane.showInputDialog("Nova Data de Termino (formato dd/mm/yyyy):");

            // Convertendo a string da nova data de término para um objeto Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date newDateEnd = null;
            try {
                newDateEnd = dateFormat.parse(newDateEndStr);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Formato de data invalido. Certifique-se de usar o formato dd/mm/yyyy.");
                return;
            }

            // Atualizar os dados da tarefa
            taskToUpdate.setTitle(newTitle);
            taskToUpdate.setDescription(newDescription);
            taskToUpdate.setPriority(newPriority);
            taskToUpdate.setDateEnd(newDateEnd);

            // Atualizar no banco de dados usando o método update da classe TaskDAO
            boolean updatedTask = taskDAO.update(taskToUpdate);

            if (updatedTask) {
                JOptionPane.showMessageDialog(null, "Tarefa atualizada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao atualizar a tarefa.");
            }
        }
    }

    // Atualizando usuário
    private static void updateUser(UserDAO userDAO, UserModel authenticatedUser) {
        // Exibir os dados atuais do usuário
        StringBuilder currentUserDetails = new StringBuilder("Dados atuais do usuario:\n");
        currentUserDetails.append("Nome: ").append(authenticatedUser.getName()).append("\n");
        currentUserDetails.append("Nome de Usuario: ").append(authenticatedUser.getUsername()).append("\n");

        JOptionPane.showMessageDialog(null, currentUserDetails.toString(), "Dados Atuais", JOptionPane.PLAIN_MESSAGE);

        // Solicitar ao usuário as informações atualizadas
        String newName = JOptionPane.showInputDialog("Novo Nome:");
        String newUsername = JOptionPane.showInputDialog("Novo Nome de Usuario:");

        // Verificar se o usuário pressionou Cancelar
        if (newName == null || newUsername == null) {
            JOptionPane.showMessageDialog(null, "Edicao cancelada. Nenhum dado do usuario foi alterado.");
            return;
        }

        // Atualizar os dados do usuário autenticado
        authenticatedUser.setName(newName);
        authenticatedUser.setUsername(newUsername);

        // Atualizar no banco de dados
        boolean updatedUser = userDAO.update(authenticatedUser);

        if (updatedUser) {
            // Exibir os dados atualizados do usuário
            StringBuilder updatedUserDetails = new StringBuilder("Dados atualizados do usuario:\n");
            updatedUserDetails.append("Nome: ").append(authenticatedUser.getName()).append("\n");
            updatedUserDetails.append("Nome de Usuario: ").append(authenticatedUser.getUsername()).append("\n");

            JOptionPane.showMessageDialog(null, updatedUserDetails.toString(), "Dados Atualizados", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Falha ao atualizar os dados do usuario.");
        }
    }

    // Deletando tarefa
    private static void deleteTask(TaskDAO taskDAO, UserModel authenticatedUser) {
        // Deletar tarefa
        // Recuperar e exibir todas as tarefas do usuário
        List<TaskModel> userTasksToDelete = taskDAO.findByUserID(authenticatedUser.getUserID());
        TaskModel taskToDelete = chooseTaskFromList(userTasksToDelete);

        if (taskToDelete != null) {
            int confirmDeleteTask = JOptionPane.showConfirmDialog(null, "Tem certeza de que deseja deletar esta tarefa?", "Confirmação", JOptionPane.YES_NO_OPTION);

            if (confirmDeleteTask == JOptionPane.YES_OPTION) {
                boolean deletedTask = taskDAO.delete(taskToDelete.getTaskID());

                if (deletedTask) {
                    JOptionPane.showMessageDialog(null, "Tarefa deletada com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Falha ao deletar a tarefa.");
                }
            }
        }
    }

    // Deletar Conta
    private static void deleteUser(UserDAO userDAO, TaskDAO taskDAO, UserModel authenticatedUser) {

        // Deletar conta
        int confirmDelete = JOptionPane.showConfirmDialog(null, "Tem certeza de que deseja deletar sua conta?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmDelete == JOptionPane.YES_OPTION) {
            // Deletar o próprio usuário autenticado
            boolean deletedUser = userDAO.delete(authenticatedUser.getUserID());

            // Deletar tarefas do usuário
            boolean deletedTasks = taskDAO.deleteTasksByUserId(authenticatedUser.getUserID().toString());

            if (deletedUser) {
                JOptionPane.showMessageDialog(null, "Sua conta foi deletada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao deletar sua conta.");
            }
        }
    }

    // Entrar no programa com login
    private static void loginUser(TaskDAO taskDAO, UserDAO userDAO) {
        boolean loggedIn = false;

        while (!loggedIn) {
            String username = JOptionPane.showInputDialog("Digite o nome de usuario:");
            String password = JOptionPane.showInputDialog("Digite sua senha:");

            try {
                // Fazendo a autenticação do usuário
                UserModel authenticatedUser = MyConnection.authenticateUser(username, password);
                JOptionPane.showMessageDialog(null, "Usuario autenticado com sucesso!");

                int choice;
                do {
                    // Mostrando um novo menu, para o usuário autenticado
                    String[] options = {"Criar nova tarefa", "Listar tarefas", "Editar tarefa", "Editar conta", "Deletar tarefa", "Deletar conta", "Sair da sua conta"};
                    choice = JOptionPane.showOptionDialog(null, "Escolha uma opcao:", "Menu",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                            null, options, options[0]);

                    switch (choice) {
                        case 0:
                            // Criar tarefa, usando o usuario logado e DAO de tarefa para manipulações em banco
                            createTask(taskDAO, authenticatedUser);
                            break;

                        case 1:
                            // Puxando a lista de tarefas para o usuário logado!
                            listTask(taskDAO, authenticatedUser);
                            break;

                        case 2: // Restringindo funcionalidade de não admin
                            if (isAdminUserAuthenticated(authenticatedUser)) {
                                updateTask(taskDAO, authenticatedUser);
                            } else {
                                JOptionPane.showMessageDialog(null, "Apenas administradores podem editar tarefas.");
                            }
                            break;

                        case 3:
                            // Atualizando dados de usuário
                            updateUser(userDAO, authenticatedUser);
                            break;

                        case 4: // Restringindo funcionalidade de não admin
                            if (isAdminUserAuthenticated(authenticatedUser)) {
                                deleteTask(taskDAO, authenticatedUser);
                            } else {
                                JOptionPane.showMessageDialog(null, "Apenas administradores podem deletar tarefas.");
                            }
                            break;

                        case 5:
                            // Deletando usuário logado
                            deleteUser(userDAO, taskDAO, authenticatedUser);
                            loggedIn = true;  // sair do loop interno e voltar para o login/cadastro
                            break;

                        case 6:
                            JOptionPane.showMessageDialog(null, "Saindo da conta..." + "Ate logo " + authenticatedUser.getUsername() + "!");
                            loggedIn = true;  // sair do loop interno e voltar para o login/cadastro
                            break;

                        default:
                            JOptionPane.showMessageDialog(null, "Opcao invalida. Tente novamente.");
                            break;
                    }
                } while (choice != 6 && choice != 5);

            } catch (UserNotFoundException | InvalidPasswordException e) {
                JOptionPane.showMessageDialog(null, "Falha na autenticacao: " + e.getMessage());
            }
        }
    }


    public static void main(String[] args) {
        // Pegando uma instância de Connection usando o método getConnection() da classe MyConnection
        MyConnection.getConnection();

        // Pegando uma instância dos DAO's para manipulações em banco
        TaskDAO taskDAO = new TaskDAO();
        UserDAO userDAO = new UserDAO();

        // Boolean para controlar o usuário manter logado
        boolean exitProgram = false;

        while (!exitProgram) {
            // Exibir um menu inicial
            String[] loginOptions = {"Cadastrar Usuario", "Logar Usuario", "Sair"};
            int loginChoice = JOptionPane.showOptionDialog(null, "Escolha uma opcao:", "Menu Inicial",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, loginOptions, loginOptions[0]);

            switch (loginChoice) {
                case 0:
                    // Cria o usuário
                    createUser(userDAO);
                    break;

                case 1:
                    // Abre o programa
                    loginUser(taskDAO, userDAO);
                    break;

                case 2:
                    exitProgram = true;
                    break;
            }
        }
    }
}