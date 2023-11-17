package org.SyncTask.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {

        public static Connection getConnection() {

            //String de conexão
            String uri = "jdbc:mysql://localhost:3306/todolist";

            // usuário e senha de autenticação ao banco
            String usuario = "root";
            String senha = "";

            // variável responsável por realizar a conexão ao banco
            Connection conn = null;

            try {
                //Carregando o driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                //abrindo a conexão
                conn = DriverManager.getConnection(uri, usuario, senha);
            } catch (ClassNotFoundException e) {
                System.out.println("Driver não carregado");
            } catch (SQLException e) {
                System.out.println("Conexão não realizada");
            }
            return conn;
        }
    }
