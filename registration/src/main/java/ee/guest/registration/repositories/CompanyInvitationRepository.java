package ee.guest.registration.repositories;

import ee.guest.registration.entities.CompanyInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyInvitationRepository extends JpaRepository<CompanyInvitation, Long> {
}
