package demo.meetingsmain.controller;

import demo.meetingscontracts.endpoints.RedisApi;
import demo.meetingsmain.service.RedisService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
public class RedisController implements RedisApi {
    private final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public ResponseEntity<String> updateTranscriptionForMeeting(@PathVariable UUID uuid, @RequestBody String body) {
        redisService.updateTranscriptionForMeeting(body, uuid);

        return ResponseEntity.ok("Success updated meeting result!");
    }

    @Override
    public String getMeetingTranscription(@PathVariable UUID uuid) {
        return redisService.getMeetingTranscription(uuid);
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
}
