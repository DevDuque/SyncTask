CREATE DATABASE SyncTask;

use SyncTask;

-- UserTable
CREATE TABLE UserTable (
    UserID CHAR(36) NOT NULL DEFAULT (UUID()),
    Name VARCHAR(25) NOT NULL,
    Username VARCHAR(30) NOT NULL,
    Password VARCHAR(30) NOT NULL,
    CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    IsAdmin BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (UserID)
);

-- TaskTable
CREATE TABLE TaskTable (
    TaskID CHAR(36) NOT NULL DEFAULT (UUID()),
    UserID CHAR(36),
    Title VARCHAR(50) NOT NULL,
    Description VARCHAR(105) NOT NULL,
    DateEnd DATETIME DEFAULT CURRENT_TIMESTAMP,
    Priority VARCHAR(45) NOT NULL,
    CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (TaskID),
    FOREIGN KEY (UserID) REFERENCES UserTable(UserID)
);

-- Adicionando dados em UserTable
INSERT INTO UserTable (Name, Username, Password, IsAdmin)
VALUES ('Duque', 'DevDuque', 'senha_segura', 0);

-- Adicionando dados em TaskTable
INSERT INTO TaskTable (UserID, Title, Description, Priority)
VALUES ('b4b48615-858c-11ee-9d48-0a0027000009', 'Título da Tarefa', 'Descrição da Tarefa', 'Alta');
