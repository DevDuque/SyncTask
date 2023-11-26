package org.SyncTask.exceptions;

public class UserNotFoundException extends Exception {

    // Fazendo um IF ternário para retornar a mensagem inserida ou a mensagem padrão
    public UserNotFoundException(String message) {
        super(message);
    }
}

