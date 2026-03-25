package demo.meetingsmain.service;

import demo.meetingscontracts.dto.MeetingRequest;
import demo.meetingscontracts.dto.MeetingResponse;
import demo.meetingscontracts.dto.MeetingStatus;
import demo.meetingscontracts.exceptions.ResourceNotFoundException;
import demo.meetingsmain.storage.InMemoryStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class MeetingService {
    private final InMemoryStorage storage;
    private static final Logger log = LoggerFactory.getLogger(MeetingService.class);

    public MeetingService(InMemoryStorage storage) {
        this.storage = storage;
    }

    public void findByUUID(UUID uuid) {
        Optional.ofNullable(storage.meetings.get(uuid))
                .orElseThrow(() -> new ResourceNotFoundException("Meeting", uuid));
    }

    public MeetingResponse createMeeting(MeetingRequest request) {
        MeetingResponse meetingResponse = new MeetingResponse(
                UUID.randomUUID(),
                request.name(),
                request.users(),
                request.authorId(),
                LocalDateTime.now(),
                0,
                request.comment(),
                request.link(),
                MeetingStatus.NEW);

        storage.meetings.put(meetingResponse.uid(), meetingResponse);
        log.info("Created new meeting: {}", meetingResponse);

        return meetingResponse;
    }

    public MeetingResponse getMeeting(UUID uuid) {
        findByUUID(uuid); // check meeting exists

        return storage.meetings.get(uuid);
    }

    public List<MeetingResponse> getMeetings() {
        return storage.meetings.values().stream().toList();
    }

    private boolean checkMeetingExists(UUID uuid) {
        for (Map.Entry<UUID, MeetingResponse> entry: storage.meetings.entrySet()) {
            if (uuid.equals(entry.getValue().uid())) {
                return true;
            }
        }
        return false;
    }
}
