package uff.redes.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uff.redes.chat.entity.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByDuvidaIdOrderByTimestampAsc(Long duvidaId);
}
