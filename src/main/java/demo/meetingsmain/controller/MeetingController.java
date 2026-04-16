package demo.meetingsmain.controller;

import demo.meetingscontracts.dto.MeetingRequest;
import demo.meetingscontracts.dto.MeetingResponse;
import demo.meetingsmain.controller.api.MeetingsApi;
import demo.meetingsmain.service.MeetingService;
import demo.meetingsmain.service.RedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
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
        return meetingService.getMeeting(uuid.toString());
    }

    @Override
    public List<MeetingResponse> getMeetings() {
        return meetingService.getMeetings();
    }

    @Override
    public MeetingResponse createMeeting(MeetingRequest request) {
        return meetingService.createMeeting(request);
    }

    @Override
    public ResponseEntity<String> uploadFile(MultipartFile file, Integer ord, Boolean isLast, UUID uuid) {
        try {
            redisService.saveAudio(ord, isLast, uuid, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok("Файл успешно загружен!");
    }
}
