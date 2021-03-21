package ru.volkov.guest.data.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import ru.volkov.guest.data.entity.User;
import ru.volkov.guest.util.exception.NotFoundException;

import java.util.Optional;

@Slf4j
@Service
public class UserService extends CrudService<User, Integer> {

    private UserRepository repository;
    private final static String NOT_FOUND_TEMPLATE = "Not found %s with that %s";

    public UserService(@Autowired UserRepository repository) {
        this.repository = repository;
    }

    @Override
    protected UserRepository getRepository() {
        return repository;
    }

    public User getById(Integer id) {
        return get(id).orElseThrow(() -> new NotFoundException("Not found user with that id"));
    }

    public User getByUserName(String userName) {
        log.info("\n getByUserName: {}", userName);
        return Optional.ofNullable(repository.getByUserName(userName))
                .orElseThrow(() -> new NotFoundException("Not found user with that userName"));
    }

    public User getByActivationCode(String activationCode) {
        return Optional.ofNullable(repository.getByActivationCode(activationCode))
                .orElseThrow(() -> new NotFoundException("Not found user with that activationCode"));
    }

    public boolean isActivated(String activationCode) {
        return Optional.ofNullable(repository.getByActivationCode(activationCode))
                .filter(user -> user.getPasswordHash() != null)
                .isPresent();
    }
}
