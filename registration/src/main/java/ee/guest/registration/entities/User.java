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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 11, unique = true, nullable = false)
    private Long personalCode;

    private String firstname;

    private String lastname;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User otherUser)) return false;
        return Objects.equals(this.personalCode, otherUser.personalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personalCode);
    }
}
