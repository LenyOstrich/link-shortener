package ru.iukr.linkshortener.exception;

public class NotFoundException extends LinkShortenerException {
    public NotFoundException(String message) {
        super(message);
    }
}
