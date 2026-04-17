package demo.meetingsmain.service.impl;

import demo.meetingscontracts.dto.MeetingRequest;
import demo.meetingscontracts.dto.MeetingResponse;
import demo.meetingscontracts.dto.MeetingStatus;
import demo.meetingscontracts.dto.UserDTO;
import demo.meetingscontracts.exceptions.ResourceNotFoundException;
import demo.meetingsmain.domain.Meeting;
import demo.meetingsmain.domain.User;
import demo.meetingsmain.repository.MeetingRepository;
import demo.meetingsmain.repository.UserRepository;
import demo.meetingsmain.service.MeetingService;
import demo.meetingsmain.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeetingServiceImpl implements MeetingService {
    private MeetingRepository meetingRepository;
    private UserRepository userRepository;
    private UserService userService;
    private static final Logger log = LoggerFactory.getLogger(MeetingServiceImpl.class);

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setMeetingRepository(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public void findByUUID(String uuid) {
        meetingRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("Meeting", uuid));
    }

    @Transactional
    public MeetingResponse createMeeting(MeetingRequest request) {

        User author = userRepository.findById(request.authorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author", request.authorId()));

        List<Integer> userIds = request.users();
        List<User> foundUsers = userRepository.findAllById(userIds);

        if (foundUsers.size() != userIds.size()) {
            throw new ResourceNotFoundException("User", userIds);
        }

        Set<User> participants = new HashSet<>(foundUsers);

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

        UserDTO authorDto = userService.findById(author.getId());
        String authorName = authorDto.lastName() + " " +
                authorDto.firstName().charAt(0) + "." +
                authorDto.patronymic().charAt(0) + ".";

        Set<UserDTO> participantDtos = foundUsers.stream()
                .map(user -> userService.findById(user.getId()))
                .collect(Collectors.toSet());

        return new MeetingResponse(
                savedMeeting.getId(),
                savedMeeting.getName(),
                savedMeeting.getAuthor().getId(),
                authorName,
                savedMeeting.getCreatedAt(),
                savedMeeting.getDuration(),
                savedMeeting.getComment(),
                savedMeeting.getLink(),
                savedMeeting.getStatus(),
                participantDtos
        );
    }

    public MeetingResponse getMeeting(String uuid) {
        Meeting meeting = meetingRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting", uuid));

        log.info("GET meeting: {}", meeting);
        return toResponse(meeting);
    }

    public List<MeetingResponse> getMeetings() {
        log.info("GET all meetings");
        return meetingRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public void changeMeetingStatus(String uuid, MeetingStatus status) {
        Optional<Meeting> optionalMeeting = meetingRepository.findById(uuid);

        if (optionalMeeting.isPresent()) {
            Meeting newMeeting = optionalMeeting.get();
            newMeeting.setStatus(status);
            meetingRepository.save(newMeeting);
        } else {
            throw new ResourceNotFoundException("Meeting", uuid);
        }
        log.info("Changed meeting: {} status to: {}", uuid, status.name());
    }

    private MeetingResponse toResponse(Meeting meeting) {
        Set<UserDTO> participantDtos = meeting.getParticipants().stream()
                .map(user -> userService.findById(user.getId()))
                .collect(Collectors.toSet());
        UserDTO author = userService.findById(meeting.getAuthor().getId());
        String authorName = author.lastName() + " " + author.firstName().charAt(0) + "." + author.patronymic().charAt(0) + ".";

        return new MeetingResponse(
                meeting.getId(),
                meeting.getName(),
                meeting.getAuthor().getId(),
                authorName,
                meeting.getCreatedAt(),
                meeting.getDuration(),
                meeting.getComment(),
                meeting.getLink(),
                meeting.getStatus(),
                participantDtos
                );
    }
}
