package ru.iukr.linkshortener.exception;

public class LinkShortenerException extends RuntimeException{

    public LinkShortenerException(String message) {
        super(message);
    }

    public LinkShortenerException(String message, Exception exception) {
        super(message, exception);
    }

    public LinkShortenerException() {
    }
}
