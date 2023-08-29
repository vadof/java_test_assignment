package ee.guest.registration.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ee.guest.registration.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(nullable = false)
    private Event event;

    @ManyToOne
    @Column(nullable = false)
    private Company company;

    @Column(nullable = false)
    private Integer participants;

    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(length = 5000)
    private String additionalInfo;

}
