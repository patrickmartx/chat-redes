package uff.redes.chat.config;

import uff.redes.chat.entity.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    private final SimpMessageSendingOperations messagingTemplate;

    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String duvidaId = (String) headerAccessor.getSessionAttributes().get("duvidaId");

        if (username != null && duvidaId != null) {
            logger.info("Usuário desconectado: {} da dúvida {}", username, duvidaId);

            if ("Professor".equals(username)) {
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setTipo("PROFESSOR_OFFLINE");
                chatMessage.setRemetente(username);
                messagingTemplate.convertAndSend("/topic/chat/" + duvidaId, chatMessage);
            }
        }
    }
}