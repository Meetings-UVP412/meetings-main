package demo.meetingsmain.service;

import demo.meetingscontracts.dto.MeetingRequest;
import demo.meetingscontracts.dto.MeetingResponse;
import demo.meetingscontracts.dto.MeetingStatus;
import demo.meetingscontracts.exceptions.ResourceNotFoundException;
import demo.meetingsmain.storage.InMemoryStorage;
import org.springframework.stereotype.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class MeetingService {
    private final InMemoryStorage storage;

    public MeetingService(InMemoryStorage storage) {
        this.storage = storage;
    }

    public MeetingResponse createMeeting(MeetingRequest request) throws MalformedURLException {
        MeetingResponse meetingResponse = new MeetingResponse(
                UUID.randomUUID(),
                request.name(),
                request.users(),
                request.authorId(),
                LocalDateTime.now(),
                0,
                "",
                "",
                MeetingStatus.NEW);

        storage.meetings.put(meetingResponse.uid(), meetingResponse);
        return meetingResponse;
    }

    public MeetingResponse getMeeting(UUID uuid) {
        if (!checkMeetingExists(uuid)) { throw new ResourceNotFoundException("Meeting", uuid); }
        return storage.meetings.get(uuid);
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
