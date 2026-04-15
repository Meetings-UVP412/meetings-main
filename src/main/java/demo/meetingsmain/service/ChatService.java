package demo.meetingsmain.service;

import demo.meetingsmain.domain.Chat;
import java.util.List;

public interface ChatService {
    List<Chat> getChatsForMeeting(String meetingUUID);
}
