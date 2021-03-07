package fr.sg.paymenthub.exceptions;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message) {
        super(String.format(message));
    }
}
