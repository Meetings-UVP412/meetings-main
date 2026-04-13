package demo.meetingsmain.service;

import demo.meetingscontracts.dto.MeetingRequest;
import demo.meetingscontracts.dto.MeetingResponse;
import demo.meetingscontracts.dto.MeetingStatus;
import demo.meetingscontracts.exceptions.ResourceNotFoundException;
import demo.meetingsmain.domain.Meeting;
import demo.meetingsmain.domain.User;
import demo.meetingsmain.repository.MeetingRepository;
import demo.meetingsmain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeetingService {
    private MeetingRepository meetingRepository;
    private UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(MeetingService.class);

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setMeetingRepository(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public void findByUUID(String uuid) {
        Optional.of(meetingRepository.findById(uuid))
                .orElseThrow(() -> new ResourceNotFoundException("Meeting", uuid));
    }

    @Transactional
    public MeetingResponse createMeeting(MeetingRequest request) {

        User author = userRepository.findById(request.authorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found: ", request.authorId()));

        Set<User> participants = new HashSet<>(userRepository.findAllById(request.users()));

        LocalDateTime now = LocalDateTime.now();
        Meeting meeting = new Meeting(
                request.name(),
                MeetingStatus.NEW,
                0,
                now,
                now,
                request.comment() != null ? request.comment() : "",
                request.link() != null ? request.link() : "",
                participants,
                author
        );

        Meeting savedMeeting = meetingRepository.save(meeting);
        log.info("Created new meeting: {}", meeting);

        return new MeetingResponse(
                savedMeeting.getId(),
                savedMeeting.getName(),
                new HashSet<>(request.users()),
                savedMeeting.getAuthor().getId(),
                savedMeeting.getCreatedAt(),
                savedMeeting.getDuration(),
                savedMeeting.getComment(),
                savedMeeting.getLink(),
                savedMeeting.getStatus()
        );
    }

    public MeetingResponse getMeeting(String uuid) {
        Meeting meeting = meetingRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found: ", uuid));

        log.info("GET meeting: {}", meeting);
        return toResponse(meeting);
    }

    public List<MeetingResponse> getMeetings() {
        log.info("GET all meeting:");
        return meetingRepository.findAll().stream().map(this::toResponse).toList();
    }

    private MeetingResponse toResponse(Meeting meeting) {
        Set<Integer> participantIds = meeting.getParticipants().stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        return new MeetingResponse(
                meeting.getId(),
                meeting.getName(),
                participantIds,
                meeting.getAuthor().getId(),
                meeting.getCreatedAt(),
                meeting.getDuration(),
                meeting.getComment(),
                meeting.getLink(),
                meeting.getStatus()
        );
    }
}
