package demo.meetingsmain.controller;

import demo.meetingscontracts.dto.ChatDTO;
import demo.meetingscontracts.dto.ChatRequest;
import demo.meetingscontracts.dto.MessageRequest;
import demo.meetingscontracts.dto.UpdateMessagesRequest;
import demo.meetingsmain.service.ChatService;
import demo.meetingsmain.service.impl.MeetingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import demo.meetingscontracts.endpoints.ChatApi;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
public class ChatController implements ChatApi {
    private final ChatService chatService;
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
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
    public ResponseEntity<StreamingResponseBody> sendMessage(MessageRequest request) {
        String chatId = request.chatUUID();
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(outputStream -> {
                    try {
                        Map<String, Object> body = Map.of("message", request.message());
                        URI uri = URI.create("http://aichat-service/internal/chat/" + chatId + "/stream");

                        WebClient webClient = WebClient.builder()
                                .baseUrl("http://aichat-service")
                                .build();

                        webClient.post()
                                .uri("/internal/chat/{chatId}/stream", chatId)
                                .bodyValue(body)
                                .retrieve()
                                .bodyToFlux(String.class)
                                .subscribe(
                                        token -> {
                                            try {
                                                outputStream.write(token.getBytes(StandardCharsets.UTF_8));
                                                outputStream.flush();
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        },
                                        error -> {
                                            try {
                                                outputStream.write(("error: " + error.getMessage()).getBytes(StandardCharsets.UTF_8));
                                                outputStream.flush();
                                            } catch (IOException e) {
                                                log.error("Ошибка при отправке ошибки клиенту", e);
                                            }
                                        }
                                );
                    } catch (Exception e) {
                        try {
                            outputStream.write(("error: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
                            outputStream.flush();
                        } catch (IOException ex) {
                            log.error("Ошибка при отправке ошибки клиенту", ex);
                        }
                    }
                });
    }

    @Override
    public ResponseEntity<String> deleteChat(String chatUUID) {
        chatService.deleteChat(chatUUID);
        return ResponseEntity.ok("Chat successfully deleted");
    }

    @PostMapping("/chats/update-messages")
    public ResponseEntity<String> updateChatMessages(@RequestBody UpdateMessagesRequest request) {
        chatService.updateMessages(request.chatId(), request.messages());
        return ResponseEntity.ok("Messages updated");
    }
}
