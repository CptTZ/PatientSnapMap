package bluehack.table9.medical;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class MedicalApplication implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(MedicalApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MedicalApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        if (args.getSourceArgs().length > 0)
            logger.warn("Command-line arguments: {}", String.join(",", args.getSourceArgs()));
        for (String name : args.getOptionNames()) {
            logger.warn(String.format("Non-option->%s=%s", name, args.getOptionValues(name)));
        }
    }

}
