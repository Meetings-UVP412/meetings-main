package demo.meetingsmain.service.impl;

import demo.meetingscontracts.dto.ChatDTO;
import demo.meetingscontracts.dto.ChatRequest;
import demo.meetingscontracts.dto.MessageDTO;
import demo.meetingscontracts.exceptions.ResourceNotFoundException;
import demo.meetingsmain.domain.Chat;
import demo.meetingsmain.domain.Message;
import demo.meetingsmain.repository.ChatRepository;
import demo.meetingsmain.service.ChatService;
import demo.meetingsmain.service.MeetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {
    private ChatRepository chatRepository;
    private MeetingService meetingService;
    private static final Logger log = LoggerFactory.getLogger(MeetingServiceImpl.class);

    @Autowired
    public void setChatRepository(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Autowired
    public void setMeetingRepository(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @Override
    public List<ChatDTO> findAll() {
        log.info("GET all Chats");
        return chatRepository.findAll().stream().map(this::toChatDTO).collect(Collectors.toList());
    }

    @Override
    public List<ChatDTO> getChatsForMeeting(String meetingUUID) {
        meetingService.findByUUID(meetingUUID); // check meeting exists

        log.info("GET Chats for meeting: {}", meetingUUID);
        return chatRepository.findByMeetingUUID(meetingUUID).stream().map(this::toChatDTO).collect(Collectors.toList());
    }

    @Override
    public ChatDTO createChat(ChatRequest request) {
        meetingService.findByUUID(request.meetingUUID()); // check meeting exists

        List<Message> messages = new ArrayList<>();
        messages.add(new Message(
                request.firstMessage().role(),
                request.firstMessage().content()
        ));
        Chat chat = new Chat(request.meetingUUID(), request.name(), messages, LocalDateTime.now());
        chatRepository.save(chat);
        log.info("Created new chat: {} to meeting: {}", chat.getId(), chat.getMeetingUUID());
        return toChatDTO(chat);
    }

    @Override
    public void deleteChat(String chatUUID) {
        chatRepository.deleteById(chatUUID);
        log.info("Deleted chat: {}", chatUUID);
    }

    public void updateMessages(String chatId, List<MessageDTO> messageDTOs) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat", chatId));

        List<Message> messages = messageDTOs.stream()
                .map(dto -> new Message(dto.role(), dto.content()))
                .collect(Collectors.toList());

        chat.setMessages(messages);
        chatRepository.save(chat);
    }

    private ChatDTO toChatDTO(Chat chat) {
        List<MessageDTO> messageDTOs = chat.readMessages().stream()
                .map(msg -> new MessageDTO(msg.getRole(), msg.getContent()))
                .toList();

        return new ChatDTO(
                chat.getId(),
                chat.getTitle(),
                messageDTOs,
                chat.getCreatedAt()
        );
    }
}
