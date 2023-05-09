package ru.client.controllers;

public class ChatException extends RuntimeException {
    public ChatException(String message) {
        super(message);
    }
}
