package org.SyncTask.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {

    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            // Se a conexão ainda não foi criada, então a cria
            String uri = "jdbc:mysql://localhost:3306/SyncTask";
            String usuario = "root";
            String senha = "";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(uri, usuario, senha);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace(); // Tratar exceções de uma forma mais robusta em produção
            }
        }

        return conn;
    }
}
