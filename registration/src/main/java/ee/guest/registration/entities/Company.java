package ee.guest.registration.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 8, unique = true)
    private Long registryCode;

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company otherCompany)) return false;
        return Objects.equals(this.registryCode, otherCompany.registryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registryCode);
    }
}
