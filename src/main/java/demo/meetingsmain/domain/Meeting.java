package demo.meetingsmain.domain;

import demo.meetingscontracts.dto.MeetingStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "meetings")
public class Meeting extends BaseEntityUUID {
    private String name;
    private MeetingStatus status;
    private Integer duration;
    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private String comment;
    private String link;
    private User author;
    private Set<User> participants = new HashSet<>();

    protected Meeting() {}

    public Meeting(String name, MeetingStatus status, Integer duration, LocalDateTime createdAt, LocalDateTime startedAt, String comment, String link, Set<User> participants, User authorId) {
        this();

        this.name = name;
        this.status = status;
        this.duration = duration;
        this.createdAt = createdAt;
        this.startedAt = startedAt;
        this.comment = comment;
        this.link = link;
        this.participants = participants;
        this.author = authorId;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "duration", nullable = false)
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 31)
    public MeetingStatus getStatus() {
        return status;
    }

    public void setStatus(MeetingStatus status) {
        this.status = status;
    }

    @Column(name = "created_at", nullable = false)
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Column(name = "started_at", nullable = false)
    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    @Column(name = "comment", nullable = false, length = 511)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User authorId) {
        this.author = authorId;
    }

    @Column(name = "link", nullable = false)
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "meetings_participants",
            joinColumns = @JoinColumn(name = "meeting_uuid", referencedColumnName = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    public Set<User> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<User> participants) {
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
                ", authorId=" + author +
                '}';
    }
}
