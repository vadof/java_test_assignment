package ee.guest.registration.repositories;

import ee.guest.registration.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPersonalCode(Long personalCode);

}
