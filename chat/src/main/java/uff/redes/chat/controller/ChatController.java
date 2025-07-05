package uff.redes.chat.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uff.redes.chat.entity.ChatMessage;
import uff.redes.chat.entity.Duvida;
import uff.redes.chat.entity.Professor;
import uff.redes.chat.repository.ProfessorRepository;
import uff.redes.chat.service.ChatMessageService;
import uff.redes.chat.service.DuvidaService;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final DuvidaService duvidaService;
    private final ProfessorRepository professorRepository;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService, DuvidaService duvidaService, ProfessorRepository professorRepository) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
        this.duvidaService = duvidaService;
        this.professorRepository = professorRepository;
    }

    @GetMapping("/chat/{duvidaId}")
    public String getChatPage(@PathVariable Long duvidaId, Model model, Principal principal) {
        Duvida duvida = duvidaService.buscarPorId(duvidaId)
                .orElseThrow(() -> new RuntimeException("Dúvida não encontrada"));

        model.addAttribute("duvida", duvida);
        String professorName = "Professor";

        if (principal != null) {
            Professor professor = professorRepository.findByEmail(principal.getName()).orElse(null);
            if (professor != null) {
                professorName = professor.getNome();
            }
            model.addAttribute("nomeUsuario", professorName);
        } else {
            model.addAttribute("nomeUsuario", duvida.getNomeAluno());
        }

        model.addAttribute("professorName", professorName);
        return "chat";
    }

    @MessageMapping("/chat.sendMessage/{duvidaId}")
    public void sendMessage(@DestinationVariable Long duvidaId, @Payload ChatMessage chatMessage) {
        ChatMessage savedMessage = chatMessageService.salvarMensagemDeTexto(duvidaId, chatMessage);
        messagingTemplate.convertAndSend("/topic/chat/" + duvidaId, savedMessage);
    }

    @MessageMapping("/chat.addUser/{duvidaId}")
    public void addUser(@DestinationVariable String duvidaId, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getRemetente());
        headerAccessor.getSessionAttributes().put("duvidaId", duvidaId);

        if (professorRepository.findByNome(chatMessage.getRemetente()).isPresent()) {
            chatMessage.setTipo("PROFESSOR_ONLINE");
        } else {
            chatMessage.setTipo("JOIN");
        }
        messagingTemplate.convertAndSend("/topic/chat/" + duvidaId, chatMessage);
    }

    @PostMapping("/chat/{duvidaId}/upload")
    @ResponseBody
    public void uploadImage(@PathVariable Long duvidaId,
                            @RequestParam("file") MultipartFile file,
                            @RequestParam("remetente") String remetente) throws IOException {
        ChatMessage savedMessage = chatMessageService.salvarMensagemDeImagem(duvidaId, remetente, file);
        messagingTemplate.convertAndSend("/topic/chat/" + duvidaId, savedMessage);
    }

    @GetMapping("/chat/message/{messageId}/imagem")
    public ResponseEntity<byte[]> getMessageImage(@PathVariable Long messageId) {
        ChatMessage message = chatMessageService.getMensagemPorId(messageId)
                .orElseThrow(() -> new RuntimeException("Mensagem não encontrada"));

        if (message.getDadosImagem() == null || message.getDadosImagem().length == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(message.getDadosImagem());
    }

    @GetMapping("/chat/historico/{duvidaId}")
    @ResponseBody
    public List<ChatMessage> getChatHistory(@PathVariable Long duvidaId) {
        return chatMessageService.getHistorico(duvidaId);
    }
}
