package demo.meetingsmain.controller;

import demo.meetingscontracts.dto.MeetingRequest;
import demo.meetingscontracts.dto.MeetingResponse;
import demo.meetingscontracts.endpoints.MeetingsApi;
import demo.meetingsmain.service.MeetingService;
import demo.meetingsmain.service.RedisService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

@RestController
public class MeetingController implements MeetingsApi {

    private final MeetingService meetingService;
    private final RedisService redisService;

    public MeetingController(MeetingService meetingService, RedisService redisService) {
        this.meetingService = meetingService;
        this.redisService = redisService;
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

    @Override
    public String uploadFile(MultipartFile file, Integer ord, Boolean isLast, UUID uuid) {
        String fullPath = uuid.toString() + "_chunk_" + ord;
        try {
            redisService.saveAudio(fullPath, file.getBytes());
        } catch (IOException e) {
            return "Error";
        }
        return "Ok";
    }
}
