package demo.meetingsmain.service;

import demo.meetingscontracts.dto.ChatDTO;
import demo.meetingscontracts.dto.ChatRequest;
import java.util.List;

public interface ChatService {
    List<ChatDTO> findAll();
    List<ChatDTO> getChatsForMeeting(String meetingUUID);
    ChatDTO createChat(ChatRequest request);
    void deleteChat(String chatUUID);
}
