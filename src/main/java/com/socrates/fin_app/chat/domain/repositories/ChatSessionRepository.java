package com.socrates.fin_app.chat.domain.repositories;

import com.socrates.fin_app.chat.domain.entities.ChatSession;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface ChatSessionRepository extends CrudRepository<ChatSession, String> {
    Optional<ChatSession> findByUserIdAndStatus(String userId, String status);
}
