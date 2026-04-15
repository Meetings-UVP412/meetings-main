package demo.meetingsmain.service.impl;

import demo.meetingsmain.domain.Chat;
import demo.meetingsmain.repository.ChatRepository;
import demo.meetingsmain.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    private ChatRepository chatRepository;
    private static final Logger log = LoggerFactory.getLogger(MeetingServiceImpl.class);

    @Autowired
    public void setChatRepository(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public List<Chat> getChatsForMeeting(String meetingUUID) {
        log.info("GET Chats for meeting: {}", meetingUUID);
        return chatRepository.findByMeetingUUID(meetingUUID);
    }
}
