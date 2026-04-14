package demo.meetingsmain.service;

import demo.meetingscontracts.dto.MeetingRequest;
import demo.meetingscontracts.dto.MeetingResponse;
import demo.meetingscontracts.dto.MeetingStatus;
import java.util.List;

public interface MeetingService {
    void findByUUID(String uuid);
    MeetingResponse createMeeting(MeetingRequest request);
    MeetingResponse getMeeting(String uuid);
    List<MeetingResponse> getMeetings();
    void changeMeetingStatus(String uuid, MeetingStatus status);
}
