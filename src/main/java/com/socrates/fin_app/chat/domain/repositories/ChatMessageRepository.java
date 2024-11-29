package com.socrates.fin_app.chat.domain.repositories;

import com.socrates.fin_app.chat.domain.entities.ChatMessage;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, String> {
    List<ChatMessage> findBySessionIdOrderByTimestampAsc(String sessionId);
}
