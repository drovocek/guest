package ru.volkov.guest.data.service.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.volkov.guest.data.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUsersByName(User name);
}