package com.socrates.fin_app.common.usecases;

public interface UseCaseCommand<T, R> {
    R execute(T request);
}
