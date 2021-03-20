package ru.volkov.guest.data.generator;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import ru.volkov.guest.data.entity.CarPass;
import ru.volkov.guest.data.entity.Role;
import ru.volkov.guest.data.entity.User;
import ru.volkov.guest.data.service.carpass.CarPassRepository;
import ru.volkov.guest.data.service.user.UserRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(CarPassRepository carPassRepository, UserRepository userRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (carPassRepository.count() != 0L || userRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }

            logger.info("Generating demo data");

            logger.info("... generating 100 Car Pass entities...");

            List<User> users = Arrays.asList(
                    new User(0, "owner", "OOO OWNER", "ProgerVolkov", "owner@email.ru", "+7 (777) 777-77-77", Role.OWNER, "owner"),
                    new User(1, "guard", "Guard Vasia", "OOO OWNER", "guard@email.ru", "+6 (666) 666-66-66", Role.GUARD, "guard"),
                    new User(1, "company", "OOO COMPANY", "OOO OWNER", "company@email.ru", "+5 (555) 555-55-55", Role.COMPANY, "company"),
                    new User(3, "employee", "Employee Ivan", "OOO COMPANY", "employee@email.ru", "+4 (444) 444-44-44", Role.EMPLOYEE, "employee")
            );
            userRepository.saveAll(users);

            List<CarPass> passes = generateCarPass(100);

            passes.forEach(p -> p.setUser(users.get(3)));

            carPassRepository.saveAll(passes);

            logger.info("Generated demo data");
        };
    }

    private List<CarPass> generateCarPass(int count) {
        return Stream.generate(() -> new CarPass(3, generateRandomHexString(7), rndDate(7)))
                .limit(count)
                .collect(Collectors.toList());
    }

    public String generateRandomHexString(int length) {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while (sb.length() < length) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, length);
    }

    public LocalDate rndDate(int dayDelta) {
        long min = LocalDate.now().toEpochDay();
        long delta = LocalDate.now().plus(dayDelta, ChronoUnit.DAYS).toEpochDay() - min;
        long max = min + delta;
        max -= min;
        long rand = (long) (Math.random() * ++max) + min;
        return LocalDate.ofEpochDay(rand);
    }
}