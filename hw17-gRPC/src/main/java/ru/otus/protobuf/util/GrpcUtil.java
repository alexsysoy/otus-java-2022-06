package ru.otus.protobuf.util;

public class GrpcUtil {

    public static void sleep(long timeToSleep) {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
