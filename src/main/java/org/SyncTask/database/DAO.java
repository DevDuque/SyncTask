package org.SyncTask.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public abstract class DAO <Type> {

    // Encontrar o todos dados
    public abstract List<Type> findAll();

    // Encontrar dados pelo ID
    public abstract Type findByID(UUID ID);

    // Inserir dados
    public abstract Type insert(Type OBJ);

    // Atualizar Dados
    public abstract boolean update(Type OBJ);

    // Deletar um dado
    public abstract boolean delete(UUID ID);
}
