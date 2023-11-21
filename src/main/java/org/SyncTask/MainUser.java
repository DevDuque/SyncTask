package org.SyncTask;


import org.SyncTask.database.UserDAO;
import org.SyncTask.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class MainUser {

    public static void main(String[] args) {

        // Criar uma instância de UserDAO
        UserDAO userDAO = new UserDAO();

        // Criar uma lista para armazenar usuários
        List<UserModel> userList = new ArrayList<>();

        // Criar um usuário e adicioná-lo à lista
        UserModel user1 = new UserModel();
        user1.setName("Davih Duque");
        user1.setUsername("DevDuque");
        user1.setPassword("password123");
        user1.setAdmin(true);

        // Adicionar o usuário ao banco de dados usando o UserDAO
        UserModel userInserted = userDAO.insert(user1);

        // Adicionar o usuário inserido à lista
        userList.add(userInserted);

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
}