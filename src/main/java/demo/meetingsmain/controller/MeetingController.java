package demo.meetingsmain.controller;

import demo.meetingscontracts.dto.MeetingRequest;
import demo.meetingscontracts.dto.MeetingResponse;
import demo.meetingscontracts.endpoints.MeetingsApi;
import demo.meetingsmain.service.MeetingService;
import demo.meetingsmain.service.RedisService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    public String uploadFile(MultipartFile file, Integer ord, Boolean isLast, UUID uuid) {
        try {
            redisService.saveAudio(ord, isLast, uuid, file.getBytes());
        } catch (IOException e) {
            return "Error";
        }
        return "Встреча создана!";
    }

    @Override
    public ResponseEntity<byte[]> getAudioChunk(@PathVariable UUID uuid, @PathVariable Integer ord) {
        byte[] audioData = redisService.getAudio(uuid.toString() + "_chunk_" + ord);

        if (audioData == null || audioData.length == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/wav"))
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(audioData.length))
                .body(audioData);
    }

    @PatchMapping("/updateText/{uuid}")
    public ResponseEntity<String> updateTranscriptionForMeeting(@PathVariable UUID uuid, @RequestBody String body) {
        redisService.updateTranscriptionForMeeting(body, uuid);

        return ResponseEntity.ok("Success updated meeting result!");
    }

    @GetMapping("/meetingResult/{uuid}")
    public String getMeetingTranscription(@PathVariable UUID uuid) {
        return redisService.getMeetingTranscription(uuid);
    }
}
