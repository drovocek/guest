package ru.volkov.guest.data.generator;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;
import ru.volkov.guest.data.entity.CarPass;
import ru.volkov.guest.data.service.CarPassRepository;

import java.time.LocalDateTime;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(CarPassRepository carPassRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (carPassRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 100 Car Pass entities...");
            ExampleDataGenerator<CarPass> carPassRepositoryGenerator = new ExampleDataGenerator<>(CarPass.class,
                    LocalDateTime.of(2021, 2, 28, 0, 0, 0));
            carPassRepositoryGenerator.setData(CarPass::setId, DataType.ID);
            carPassRepositoryGenerator.setData(CarPass::setRegNum, DataType.ZIP_CODE);
            carPassRepositoryGenerator.setData(CarPass::setArrivalDate, DataType.DATE_NEXT_7_DAYS);
            carPassRepositoryGenerator.setData(CarPass::setPassed, DataType.BOOLEAN_10_90);
            carPassRepositoryGenerator.setData(CarPass::setCompanyName, DataType.COMPANY_NAME);
            carPassRepository.saveAll(carPassRepositoryGenerator.create(100, seed));

            logger.info("Generated demo data");
        };
    }

}