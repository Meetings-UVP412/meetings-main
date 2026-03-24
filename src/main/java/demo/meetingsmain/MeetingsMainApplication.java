package demo.meetingsmain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(
        exclude = {DataSourceAutoConfiguration.class}
)
public class MeetingsMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetingsMainApplication.class, args);
    }

}
