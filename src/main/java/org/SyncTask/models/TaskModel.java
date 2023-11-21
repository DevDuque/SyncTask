package org.SyncTask.models;

import java.util.Date;
import java.util.UUID;

// Modelo de Tarefa para criação de acordo com a tabela

public class TaskModel {
    private UUID TaskID;
    private UUID UserID;
    private String Title;
    private String Description;
    private String Priority;
    private Date DateEnd;
    private Date createdAt;

    // Construtor
    public TaskModel(UUID taskID, UUID userID, String title, String description, String priority, Date dateEnd, Date createdAt) {
        this.TaskID = taskID;
        this.UserID = userID;
        this.Title = title;
        this.Description = description;
        this.Priority = priority;
        this.DateEnd = dateEnd;
        this.createdAt = createdAt;
    }

    public TaskModel() {
        this.createdAt = new Date();
    }

    // Getters & Setters para pegar os dados e deixa-los privados
    public UUID getTaskID() {
        return TaskID;
    }
    public void setTaskID() {
        this.TaskID = UUID.randomUUID();
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

    public Date getDateEnd() {
        return DateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        DateEnd = dateEnd;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}


