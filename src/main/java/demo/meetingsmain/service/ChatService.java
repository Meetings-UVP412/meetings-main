package demo.meetingsmain.service;

import demo.meetingscontracts.dto.ChatDTO;
import demo.meetingscontracts.dto.ChatRequest;
import demo.meetingscontracts.dto.MessageDTO;

import java.util.List;

public interface ChatService {
    List<ChatDTO> findAll();
    List<ChatDTO> getChatsForMeeting(String meetingUUID);
    ChatDTO createChat(ChatRequest request);
    ChatDTO chatHistory(String chatUUID);
    void deleteChat(String chatUUID);
    void updateMessages(String chatId, List<MessageDTO> messageDTOs);
    Boolean checkStatus(String meetingUUID);
}
