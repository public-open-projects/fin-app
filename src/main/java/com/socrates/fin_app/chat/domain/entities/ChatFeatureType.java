package com.socrates.fin_app.chat.domain.entities;

public enum ChatFeatureType {
    // Available for both authenticated and unauthenticated
    GENERAL_INQUIRY,
    PRODUCT_INFO,
    FAQ_ACCESS,
    
    // Available only for authenticated users
    ACCOUNT_INQUIRY,
    TRANSACTION_EXECUTION,
    DOCUMENT_ACCESS,
    PROFILE_MANAGEMENT
}
