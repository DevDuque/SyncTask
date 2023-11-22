package org.SyncTask;


import org.SyncTask.database.UserDAO;
import org.SyncTask.models.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainUser {

    public static void imprimirLista(List<UserModel> userList) {

        // Imprimir os detalhes dos usuários na lista
        for (UserModel user : userList) {
            System.out.println("User details:");
            System.out.println("UserID: " + user.getUserID());
            System.out.println("Name: " + user.getName());
            System.out.println("Username: " + user.getUsername());
            System.out.println("Admin: " + user.getAdmin());
            System.out.println("CreatedAt " + user.getCreatedAt());
            System.out.println();
        }
    }

    public static void main(String[] args) {

        // Criar uma instância de UserDAO
        UserDAO userDAO = new UserDAO();

        // Criar uma lista para armazenar usuários
        List<UserModel> userList = new ArrayList<>();

        // Criar um usuário e adicioná-lo à lista
        UserModel user = new UserModel();
        user.setName("Davih Duque");
        user.setUsername("DevDuque");
        user.setPassword("password123");
        user.setAdmin(true);

        // Adicionar o usuário ao banco de dados usando o UserDAO
        UserModel userInserted = userDAO.insert(user);

        // Adicionar o usuário inserido à lista
        userList.add(userInserted);

        imprimirLista(userList);

        // Inicializando ID para atualização
        UUID updateID = userInserted.getUserID();
        UserModel userUpdated = null;

        // Procurando o usuário na lista de usuários
        for (UserModel users : userList) {
            if (users.getUserID().equals(updateID)) {
                userUpdated = users;
                break;
            }
        }

        try {
            if (userUpdated != null) {
                // Atualizar os dados do usuário
                userUpdated.setName("Atualizando de novo");
                userUpdated.setUsername("BAAAH");

                // Chamar a função de atualização no banco de dados
                boolean updateSuccessful = userDAO.update(userUpdated);

                // Verificar se a atualização foi bem-sucedida
                if (updateSuccessful) {
                    System.out.println("Usuário atualizado com sucesso no banco de dados.");
                    imprimirLista(userList);
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