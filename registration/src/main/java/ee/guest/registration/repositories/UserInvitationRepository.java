package ee.guest.registration.repositories;

import ee.guest.registration.entities.UserInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInvitationRepository extends JpaRepository<UserInvitation, Long> {
}
