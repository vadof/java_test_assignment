package ee.guest.registration.repositories;

import ee.guest.registration.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByRegistryCode(Long registryCode);

}
