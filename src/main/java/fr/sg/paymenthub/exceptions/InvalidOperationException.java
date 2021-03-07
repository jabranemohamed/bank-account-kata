package fr.sg.paymenthub.exceptions;

public class InvalidOperationException extends Exception {

    public InvalidOperationException(String message) {
        super(String.format(message));
    }
}
