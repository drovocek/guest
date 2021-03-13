package ru.volkov.guest.data.service;

import com.vaadin.flow.component.Component;
import ru.volkov.guest.data.entity.User;
import ru.volkov.guest.data.service.user.UserRepository;
import ru.volkov.guest.util.exception.NotFoundException;

public class AuthService {

    public record AuthorizedRoute(String route, String name, Class<? extends Component> view){

    }

    private final UserRepository repository;

    public AuthService(UserRepository repository) {
        this.repository = repository;
    }

    public boolean authenticate(String name, String password) {
        User user = repository.findUsersByName(name);
        if (user == null) {
            throw new NotFoundException("No such user");
        } else if (!user.checkPassword(password)) {
            throw new ArithmeticException("Password incorrect");
        } else {
            return true;
        }
    }
}
