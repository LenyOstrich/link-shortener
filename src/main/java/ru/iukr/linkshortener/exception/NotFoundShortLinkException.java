package ru.iukr.linkshortener.exception;

public class NotFoundShortLinkException extends RuntimeException {
    public NotFoundShortLinkException(String message) {
        super(message);
    }
}
