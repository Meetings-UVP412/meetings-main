package demo.meetingsmain.controller;

import demo.meetingscontracts.dto.MeetingRequest;
import demo.meetingscontracts.dto.MeetingResponse;
import demo.meetingscontracts.endpoints.MeetingsApi;
import demo.meetingsmain.service.MeetingService;
import org.springframework.web.bind.annotation.RestController;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

@RestController
public class MeetingController implements MeetingsApi {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @Override
    public MeetingResponse getMeeting(UUID uuid) {
        return meetingService.getMeeting(uuid);
    }

    @Override
    public List<MeetingResponse> getMeetings() {
        return meetingService.getMeetings();
    }

    @Override
    public MeetingResponse createMeeting(MeetingRequest request) {
        try {
            return meetingService.createMeeting(request);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
