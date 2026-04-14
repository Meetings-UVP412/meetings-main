package demo.meetingsmain.service;

import java.util.UUID;

public interface RedisService {
    void saveAudio(Integer ord, Boolean isLast, UUID uuid, byte[] audioData);
    byte[] getAudio(String key);
    void deleteAudio(String key);
    void updateTranscriptionForMeeting(String result, UUID uuid);
    String getMeetingTranscription(UUID uuid);
    void scheduleCleanup(String meetingUuid, Integer ord, long delayMs);
}
