package ee.guest.registration.services;

import ee.guest.registration.entities.Company;
import ee.guest.registration.repositories.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public Optional<Company> getCompanyByRegistryCode(Long registryCode) {
        return this.companyRepository.findByRegistryCode(registryCode);
    }

    public Optional<Company> createNewCompany(String name, Long registryCode) {
        if (companyRepository.findByRegistryCode(registryCode).isEmpty()
                && String.valueOf(registryCode).length() == 8) {
            Company company = new Company();
            company.setName(name);
            company.setRegistryCode(registryCode);

            this.companyRepository.save(company);
            return Optional.of(company);
        }
        return Optional.empty();
    }

}
