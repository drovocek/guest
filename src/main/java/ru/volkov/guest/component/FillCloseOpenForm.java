package ru.volkov.guest.component;

public interface FillCloseOpenForm<T> {

    void fillAndOpen(T bean);

    void clearAndClose();
}
