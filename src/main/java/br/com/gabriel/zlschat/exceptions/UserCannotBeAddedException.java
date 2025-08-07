package br.com.gabriel.zlschat.exceptions;

public class UserCannotBeAddedException extends RuntimeException {
    public UserCannotBeAddedException(String message) {
        super(message);
    }
}
