package ee.guest.registration.repositories;

import ee.guest.registration.entities.Event;
import ee.guest.registration.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByUserInvitationsUser(User user);

    List<Event> findAllByOrganizer(User user);

}
