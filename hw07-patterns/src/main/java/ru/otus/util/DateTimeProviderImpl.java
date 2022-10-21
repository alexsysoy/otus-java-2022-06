package ru.otus.util;

import java.time.LocalDateTime;

public class DateTimeProviderImpl implements DateTimeProvider {
    @Override
    public int getCurrentSecond() {
        return LocalDateTime.now().getSecond();
    }
}
