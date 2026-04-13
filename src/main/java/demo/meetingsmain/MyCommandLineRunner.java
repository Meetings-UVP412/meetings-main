package demo.meetingsmain;

import com.github.javafaker.Faker;
import demo.meetingscontracts.dto.MeetingStatus;
import demo.meetingsmain.domain.Meeting;
import demo.meetingsmain.domain.User;
import demo.meetingsmain.repository.MeetingRepository;
import demo.meetingsmain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class MyCommandLineRunner implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    private final Faker faker = new Faker(new Locale("ru"));
    private final Random random = new Random();
    private static final Logger log = LoggerFactory.getLogger(MyCommandLineRunner.class);


    @Override
    public void run(String... args) {
        initUsers();
        initMeetings();
    }

    private void initUsers() {
        if (userRepository.count() >= 99) {
            log.info("Skip users init");
            return;
        }

        for (int i = 0; i < 100; i++) {
            String fullName = faker.name().nameWithMiddle();
            String firstName = fullName.split("\\s+")[0];
            String lastName = fullName.split("\\s+")[1];
            String middleName = fullName.split("\\s+")[2];

            User user = new User(firstName, lastName, middleName, null, null);
            userRepository.save(user);
        }
        log.info("Generated 100 test users");
    }

    private void initMeetings() {
        if (meetingRepository.count() >= 50) {
            log.info("Skip meetings init");
            return;
        }

        Set<String> names = Set.of(
                "Лекция СОП",
                "Администрирование операционных систем",
                "Лекция Сети",
                "Лекция ИИ",
                "Лекция Android",
                "Лекция DevOps",
                "Встреча от 13.04.26",
                "Встреча от 01.02.26",
                "Встреча от 27.12.25"
        );

        for (int i = 0; i < 50; i++) {
            String name = names.stream().skip(new Random().nextInt(names.size())).findFirst().get();
            Integer duration = random.nextInt(60, 100000);
            LocalDateTime now = LocalDateTime.now();
            User author = userRepository.findById(random.nextInt(1, 100)).get();
            List<User> userIds = userRepository.findAll();
            Collections.shuffle(userIds);
            Meeting meeting = new Meeting(name, MeetingStatus.ARCHIVED, duration, now, now, faker.hobbit().quote(), "https://rut-miit.ru/", new HashSet<>(userIds.subList(0, 10)), author);

            meetingRepository.save(meeting);
        }
        log.info("Generated 50 test meetings");
    }
}
