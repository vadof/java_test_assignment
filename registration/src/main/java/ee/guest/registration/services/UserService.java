package ee.guest.registration.services;

import ee.guest.registration.entities.User;
import ee.guest.registration.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public boolean userIsValid(Integer personalCode) {
        return userRepository.findByPersonalCode(personalCode).isPresent();
    }

    public Optional<User> getUserByPersonalCode(Integer personalCode) {
        return userRepository.findByPersonalCode(personalCode);
    }

}
