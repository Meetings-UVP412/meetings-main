package demo.meetingsmain.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.UUID;

public interface RedisService {
    void saveAudio(Integer ord, Boolean isLast, UUID uuid, byte[] audioData, int chunkDurationSeconds);
    byte[] getAudio(String key);
    void deleteAudio(String key);
    void updateTranscriptionForMeeting(String result, UUID uuid);
    String getMeetingTranscription(UUID uuid);
    void scheduleCleanup(String meetingUuid, Integer ord, long delayMs) throws JsonProcessingException;
    int getMeetingLastOrd(String uuid);
}
