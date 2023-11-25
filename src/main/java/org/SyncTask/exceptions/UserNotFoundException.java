package org.SyncTask.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String s) {
        super("Nenhum usuário criado no banco de dados");
    }
}
