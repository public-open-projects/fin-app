package com.socrates.fin_app.identity.infrastructure.providers;

public interface IdpProvider {
    void createClientAccount(String email, String password);
}
