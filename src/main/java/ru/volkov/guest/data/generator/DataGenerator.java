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

            ExampleDataGenerator<User> userGenerator = new ExampleDataGenerator<>(User.class, LocalDateTime.now());
            userGenerator.setData(User::setRole, DataType.WORD);
            userGenerator.setData(User::setCompany, DataType.COMPANY_NAME);
            userGenerator.setData(User::setName, DataType.FIRST_NAME);
            userGenerator.setData(User::setEmail, DataType.EMAIL);
            userGenerator.setData(User::setPhone, DataType.PHONE_NUMBER);
            userGenerator.setData(User::setPassword, DataType.TWO_WORDS);
            userGenerator.setData(User::setRegDate, DataType.DATE_OF_BIRTH);
//            userGenerator.setData(User::setLastActivity, DataType.);
            userGenerator.setData(User::setPassCount, DataType.NUMBER_UP_TO_100);
            userRepository.saveAll(userGenerator.create(50, 30));

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