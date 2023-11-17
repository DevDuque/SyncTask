package org.SyncTask.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

// Modelo de Tarefa para criação de acordo com a tabela

public class TaskModel {
    private UUID TaskID;
    private UUID UserID;
    private String Title;
    private String Description;
    private String Priority;
    private LocalDateTime DateEnd;
    private LocalDateTime createdAt;

    // Construtor
    public TaskModel(UUID taskID, UUID userID, String title, String description, String priority, LocalDateTime dateEnd, LocalDateTime createdAt) {
        this.TaskID = taskID;
        this.UserID = userID;
        this.Title = title;
        this.Description = description;
        this.Priority = priority;
        this.DateEnd = dateEnd;
        this.createdAt = createdAt;
    }

    // Getters & Setters para pegar os dados e deixa-los privados
    public UUID getTaskID() {
        return TaskID;
    }
    public void setTaskID(UUID taskID) {
        TaskID = taskID;
    }

    public UUID getUserID() {
        return UserID;
    }
    public void setUserID(UUID userID) {
        UserID = userID;
    }

    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }

    public String getPriority() {
        return Priority;
    }
    public void setPriority(String priority) {
        Priority = priority;
    }

    public LocalDateTime getDateEnd() {
        return DateEnd;
    }

    public void setDateEnd(LocalDateTime dateEnd) {
        DateEnd = dateEnd;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}


