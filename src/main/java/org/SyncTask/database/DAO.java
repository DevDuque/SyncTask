package org.SyncTask.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public abstract class DAO <T> {

    // Encontrar o todos dados
    public abstract List<T> findAll();

    // Encontrar dados pelo ID
    public abstract T findByID(UUID ID);

    // Inserir dados
    public abstract T insert(T OBJ);

    // Atualizar Dados
    public abstract boolean update(T OBJ);

    // Deletar um dado
    public abstract boolean delete(T OBJ);
}
