package demo.meetingsmain.domain;

import demo.meetingscontracts.dto.MeetingStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "meetings")
public class Meeting extends BaseEntity {
    private String name;
    private MeetingStatus status;
    private Integer duration;
    private LocalDate createdAt;
    private LocalDate startedAt;
    private String comment;
    private String link;
    private User authorId;
    private List<User> participants;

    protected Meeting() {}

    public Meeting(String name, MeetingStatus status, Integer duration, LocalDate createdAt, LocalDate startedAt, String comment, String link, List<User> participants, User authorId) {
        this();

        this.name = name;
        this.status = status;
        this.duration = duration;
        this.createdAt = createdAt;
        this.startedAt = startedAt;
        this.comment = comment;
        this.link = link;
        this.participants = participants;
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public MeetingStatus getStatus() {
        return status;
    }

    public void setStatus(MeetingStatus status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDate startedAt) {
        this.startedAt = startedAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getAuthorId() {
        return authorId;
    }

    public void setAuthorId(User authorId) {
        this.authorId = authorId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", createdAt=" + createdAt +
                ", startedAt=" + startedAt +
                ", comment='" + comment + '\'' +
                ", link='" + link + '\'' +
                ", authorId=" + authorId +
                ", participants=" + participants +
                '}';
    }
}
