package ru.volkov.guest.data.generator;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;
import ru.volkov.guest.data.entity.CarPass;
import ru.volkov.guest.data.entity.Company;
import ru.volkov.guest.data.entity.Role;
import ru.volkov.guest.data.entity.User;
import ru.volkov.guest.data.service.carpass.CarPassRepository;
import ru.volkov.guest.data.service.company.CompanyRepository;
import ru.volkov.guest.data.service.user.UserRepository;

import java.time.LocalDateTime;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(
            CarPassRepository carPassRepository,
            UserRepository userRepository,
            CompanyRepository companyRepository
    ) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (carPassRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 100 Car Pass entities...");
            ExampleDataGenerator<CarPass> carPassGenerator = new ExampleDataGenerator<>(CarPass.class, LocalDateTime.now());
            carPassGenerator.setData(CarPass::setId, DataType.ID);
            carPassGenerator.setData(CarPass::setRegNum, DataType.ZIP_CODE);
            carPassGenerator.setData(CarPass::setArrivalDate, DataType.DATE_NEXT_7_DAYS);
            carPassGenerator.setData(CarPass::setPassed, DataType.BOOLEAN_10_90);
            carPassGenerator.setData(CarPass::setCompanyName, DataType.COMPANY_NAME);
            carPassRepository.saveAll(carPassGenerator.create(50, seed));

            userRepository.save(new User("owner", "owner@email.ru", "+7(777)777-77-77", Role.OWNER, "owner"));
            userRepository.save(new User("guard", "guard@email.ru", "+6(666)666-66-66", Role.GUARD, "guard"));
            userRepository.save(new User("company", "company@email.ru", "+5(555)555-55-55", Role.COMPANY, "company"));
            userRepository.save(new User("user", "user@email.ru", "+4(444)444-44-44", Role.USER, "user"));

            ExampleDataGenerator<Company> companyGenerator = new ExampleDataGenerator<>(Company.class, LocalDateTime.now());
            companyGenerator.setData(Company::setName, DataType.COMPANY_NAME);
            companyGenerator.setData(Company::setEmail, DataType.EMAIL);
            companyGenerator.setData(Company::setPhone, DataType.PHONE_NUMBER);
            companyGenerator.setData(Company::setPassword, DataType.TWO_WORDS);
            companyGenerator.setData(Company::setRegDate, DataType.DATE_OF_BIRTH);
            companyGenerator.setData(Company::setPassCount, DataType.NUMBER_UP_TO_100);
            companyRepository.saveAll(companyGenerator.create(50, 30));

            logger.info("Generated demo data");
        };
    }

}