package org.skypro.skyshop.service;

public class NoSuchProductException extends RuntimeException {
    public NoSuchProductException(String message) {
        super("Товар не найден");
    }
}
