package com.socrates.fin_app.common.usecases;

public interface UseCaseCommand<T> {
    T execute();
}
