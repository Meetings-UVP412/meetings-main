package demo.meetingsmain.domain;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntityID {
    private String firstName;
    private String lastName;
    private String patronymic;
    private Set<Meeting> meetings = new HashSet<>();
    private Set<Meeting> authorMeetings = new HashSet<>();

    protected User() {}

    public User(String firstName, String lastName, String patronymic, Set<Meeting> meetings, Set<Meeting> authorMeetings) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.meetings = meetings;
        this.authorMeetings = authorMeetings;
    }

    @Column(name = "firstName", nullable = false, length = 127)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "lastName", nullable = false, length = 127)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "patronymic", nullable = false, length = 127)
    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY)
    public Set<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(Set<Meeting> meetings) {
        this.meetings = meetings;
    }

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    public Set<Meeting> getAuthorMeetings() {
        return authorMeetings;
    }

    public void setAuthorMeetings(Set<Meeting> authorMeetings) {
        this.authorMeetings = authorMeetings;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                '}';
    }
}
