package org.SyncTask.models;

import java.time.LocalDateTime;
import java.util.UUID;

// Modelo de Usuário para criação de acordo com a tabela

public class UserModel {
    private UUID UserID;
    private String Name;
    private String Username;
    private String Password;
    private LocalDateTime createdAt;
    private Boolean isAdmin;

    public UserModel(UUID UserID, String Name, String Username, String Password, LocalDateTime createdAt, boolean isAdmin) {
        this.UserID = UserID;
        this.Name = Name;
        this.Username = Username;
        this.Password = Password;
        this.createdAt = createdAt;
        this.isAdmin = isAdmin;
    }

    // Getters & Setters para pegar os dados e deixa-los privados
    public UUID getUserID() {
        return UserID;
    }

    public void setUserID(UUID userID) {
        UserID = userID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
