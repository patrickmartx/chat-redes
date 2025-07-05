package uff.redes.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uff.redes.chat.entity.ChatMessage;
import uff.redes.chat.entity.Duvida;
import uff.redes.chat.repository.ChatMessageRepository;
import uff.redes.chat.repository.DuvidaRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final DuvidaRepository duvidaRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, DuvidaRepository duvidaRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.duvidaRepository = duvidaRepository;
    }

    /**
     * Salva uma mensagem de texto, associando-a à Dúvida correta.
     * @param duvidaId O ID da dúvida à qual a mensagem pertence.
     * @param chatMessage O objeto da mensagem (com remetente, conteúdo, etc.).
     * @return A entidade ChatMessage salva.
     */
    @Transactional
    public ChatMessage salvarMensagemDeTexto(Long duvidaId, ChatMessage chatMessage) {
        Duvida duvida = findDuvidaById(duvidaId);
        chatMessage.setDuvida(duvida);
        return chatMessageRepository.save(chatMessage);
    }

    /**
     * Salva uma mensagem de imagem, convertendo o arquivo para bytes e associando-o à Dúvida.
     * @param duvidaId O ID da dúvida à qual a imagem pertence.
     * @param remetente O nome do usuário que enviou a imagem.
     * @param file O arquivo de imagem.
     * @return A entidade ChatMessage salva com a imagem.
     * @throws IOException Se houver um erro ao ler os bytes do arquivo.
     */
    @Transactional
    public ChatMessage salvarMensagemDeImagem(Long duvidaId, String remetente, MultipartFile file) throws IOException {
        Duvida duvida = findDuvidaById(duvidaId);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setDuvida(duvida);
        chatMessage.setRemetente(remetente);
        chatMessage.setTipo("IMAGE");
        chatMessage.setConteudo(file.getOriginalFilename());
        chatMessage.setDadosImagem(file.getBytes());

        return chatMessageRepository.save(chatMessage);
    }

    /**
     * Retorna o histórico completo de mensagens para uma determinada dúvida.
     * @param duvidaId O ID da dúvida.
     * @return Uma lista de ChatMessage, ordenada pela data de envio.
     */
    public List<ChatMessage> getHistorico(Long duvidaId) {
        return chatMessageRepository.findByDuvidaIdOrderByTimestampAsc(duvidaId);
    }

    /**
     * Busca uma mensagem específica pelo seu ID.
     * @param messageId O ID da mensagem.
     * @return Um Optional contendo a ChatMessage, se encontrada.
     */
    public Optional<ChatMessage> getMensagemPorId(Long messageId) {
        return chatMessageRepository.findById(messageId);
    }

    /**
     * Método auxiliar para encontrar uma Dúvida ou lançar uma exceção.
     */
    private Duvida findDuvidaById(Long duvidaId) {
        return duvidaRepository.findById(duvidaId)
                .orElseThrow(() -> new RuntimeException("Dúvida não encontrada para o ID: " + duvidaId));
    }
}
