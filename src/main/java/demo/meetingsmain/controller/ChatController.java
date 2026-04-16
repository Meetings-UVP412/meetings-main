package demo.meetingsmain.controller;

import demo.meetingscontracts.dto.ChatDTO;
import demo.meetingscontracts.dto.ChatRequest;
import demo.meetingscontracts.dto.MessageRequest;
import demo.meetingscontracts.dto.UpdateMessagesRequest;
import demo.meetingscontracts.exceptions.MeetingArchivedException;
import demo.meetingsmain.controller.api.ChatApi;
import demo.meetingsmain.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import java.util.List;
import java.util.Map;

@RestController
public class ChatController implements ChatApi {
    private final ChatService chatService;
    private final WebClient webClient;
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    public ChatController(ChatService chatService, WebClient.Builder webClientBuilder) {
        this.chatService = chatService;
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8000")
                .build();
    }

    @Override
    public List<ChatDTO> getAllChats() {
        return chatService.findAll();
    }

    @Override
    public List<ChatDTO> getChatsForMeeting(String meetingUUID) {
        return chatService.getChatsForMeeting(meetingUUID);
    }

    @Override
    public ChatDTO createChat(ChatRequest request) {
        return chatService.createChat(request);
    }

    @Override
    public Flux<String> sendMessage(MessageRequest request) {
        String chatId = request.chatUUID();

        if (chatId == null || chatId.isEmpty()) {
            return Flux.error(new IllegalArgumentException("chatUUID is required"));
        }

        if (!chatService.checkStatus(request.meetingUUID())) {
            return Flux.error(new MeetingArchivedException("Messages can only be sent to meetings with status PROCESSED", request.meetingUUID()));
        }

        Map<String, Object> body = Map.of("message", request.message());

        return webClient.post()
                .uri("/internal/chat/{chatId}/stream", chatId)
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(String.class)
                .doOnError(error -> log.error("Ошибка при стриминге чата {}: {}", chatId, error.getMessage()))
                .onErrorResume(error -> Flux.just("error: " + error.getMessage()));
    }

    @Override
    public ChatDTO chatHistory(String chatUUID) {
        return chatService.chatHistory(chatUUID);
    }

    @Override
    public ResponseEntity<String> deleteChat(String chatUUID) {
        chatService.deleteChat(chatUUID);
        return ResponseEntity.ok("Chat successfully deleted");
    }

    @Override
    public ResponseEntity<String> updateChatMessages(String chatUUID, UpdateMessagesRequest request) {
        chatService.updateMessages(chatUUID, request.messages());
        return ResponseEntity.ok("Messages updated");
    }
}
