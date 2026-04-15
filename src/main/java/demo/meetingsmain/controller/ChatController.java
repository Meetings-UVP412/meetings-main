package demo.meetingsmain.controller;

import demo.meetingscontracts.dto.ChatDTO;
import demo.meetingscontracts.dto.ChatRequest;
import demo.meetingscontracts.dto.MessageRequest;
import demo.meetingsmain.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import demo.meetingscontracts.endpoints.ChatApi;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import java.util.List;

@RestController
public class ChatController implements ChatApi {
    private final ChatService chatService;

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
        return null;
    }

    @Override
    public ResponseEntity<String> deleteChat(String chatUUID) {
        chatService.deleteChat(chatUUID);
        return ResponseEntity.ok("Chat successfully deleted");
    }
}
