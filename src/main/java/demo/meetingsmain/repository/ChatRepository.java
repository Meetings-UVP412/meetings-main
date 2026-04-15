package demo.meetingsmain.repository;

import demo.meetingsmain.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, String> {
    List<Chat> findByMeetingUUID(String meetingUUID);
}
