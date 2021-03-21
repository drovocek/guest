package ru.volkov.guest.data.service.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.volkov.guest.data.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User getByUserName(String userName);

    User getByActivationCode(String activationCode);
}