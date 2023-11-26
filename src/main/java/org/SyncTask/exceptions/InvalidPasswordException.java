package org.SyncTask.exceptions;

public class InvalidPasswordException extends Exception {

    // Fazendo um IF ternário para retornar a mensagem inserida ou a mensagem padrão
    public InvalidPasswordException(String message) {
        super(message);
    }
}
