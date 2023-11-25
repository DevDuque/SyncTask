package org.SyncTask;

// Importações do próprio projeto
import org.SyncTask.database.UserDAO;
import org.SyncTask.models.UserModel;

// Importações do Java
import java.util.List;
import java.util.UUID;

public class MainUser {

    public static void imprimirUsuario(UserModel user) {

        // Imprimir os detalhes do usuário na lista
        System.out.println("UserID: " + user.getUserID());
        System.out.println("Name: " + user.getName());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Admin: " + user.getAdmin());
        System.out.println("CreatedAt " + user.getCreatedAt());
        System.out.println();
    }

    public static void imprimirLista(List<UserModel> userList) {

        // Imprimir os detalhes dos usuários na lista
        for (UserModel user : userList) {
            imprimirUsuario(user);
        }
    }

    public static void main(String[] args) {

        // Criar uma instância de UserDAO
        UserDAO userDAO = new UserDAO();

//        // Criar um usuário
//        UserModel user = new UserModel();
//        user.setName("Davih Duque");
//        user.setUsername("DevDuque");
//        user.setPassword("password123");
//        user.setAdmin(true);
//
//        // Adicionar o usuário ao banco de dados usando o UserDAO
//        userDAO.insert(user);
//
//        // Imprimindo o usuário criado
//        System.out.println("Usuário criado com sucesso! \nNome: " + user.getName() + "\nUserID = " + user.getUserID() + "\n");

        // FIND ALL
        // Recuperar todos os usuários do banco de dados e salvando em uma lista
        List<UserModel> userList = userDAO.findAll();

        // Imprimir os detalhes dos usuários recuperados
        System.out.println("Todos os usuários no banco de dados:");
        imprimirLista(userList);

        //BUSCA POR ID

        // Escolher um ID existente (substitua 'algumID' pelo UUID real)
        UUID someID = UUID.fromString("d430804b-4343-4d80-b6ce-70f414272f6f");

        // Recuperar um usuário pelo ID
        UserModel userByID = userDAO.findByID(someID);

        // Imprimir os detalhes do usuário recuperado
        if (userByID != null) {
            System.out.println("Usuário encontrado por ID:");
            imprimirUsuario(userByID);
        } else {
            System.out.println("Nenhum usuário encontrado para o ID fornecido.");
        }

        // Atualizando o usuário usando findByID
        try {
            if (userByID != null) {
                // Atualizar os dados do usuário
                userByID.setName("Davih Duque");
                userByID.setUsername("Testando com a findByID");

                // Chamar a função de atualização no banco de dados
                boolean updateSuccessful = userDAO.update(userByID);

                // Verificar se a atualização foi bem-sucedida
                if (updateSuccessful) {
                    System.out.println("Usuário atualizado com sucesso no banco de dados.");
                    imprimirUsuario(userByID);
                } else {
                    throw new Exception("Erro ao atualizar usuário no banco de dados.");
                }
            } else {
                throw new Exception("Usuário não encontrado na lista.");
            }
        } catch (Exception e) {
            System.err.println("Erro durante a atualização do usuário: " + e.getMessage());
        }
    }
}