package demo.meetingsmain.storage;

import demo.meetingscontracts.dto.MeetingResponse;
import demo.meetingscontracts.dto.MeetingStatus;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryStorage {
    public final Map<UUID, MeetingResponse> meetings = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        MeetingResponse meetingResponse1 = new MeetingResponse(
                UUID.randomUUID(),
                "Лекция СОП",
                List.of(1, 2),
                1,
                LocalDateTime.now(),
                600,
                "Тестовый комментарий",
                "https://rut-miit.ru/",
                MeetingStatus.ARCHIVED);

        meetings.put(meetingResponse1.uid(), meetingResponse1);
    }
}
