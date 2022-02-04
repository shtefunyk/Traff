package com.pampam.lib.interfaces;

public interface IValueListener<T> {
    void value(T result);
    void failed();
}
