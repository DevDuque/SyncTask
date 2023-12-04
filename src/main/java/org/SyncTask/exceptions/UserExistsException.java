package org.SyncTask.exceptions;

public class UserExistsException extends Exception {
    public UserExistsException(String mensagem) {
        super(mensagem);
    }
}
