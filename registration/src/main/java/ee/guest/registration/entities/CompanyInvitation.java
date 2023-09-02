package ee.guest.registration.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ee.guest.registration.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CompanyInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Company company;

    @Column(nullable = false)
    private Integer participants;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(length = 5000)
    private String additionalInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyInvitation otherCompanyInvitation)) return false;
        return Objects.equals(this.id, otherCompanyInvitation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
