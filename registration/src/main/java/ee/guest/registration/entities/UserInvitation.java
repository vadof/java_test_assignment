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
public class UserInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Event event;

    @Column(nullable = false)
    @ManyToOne(cascade = CascadeType.DETACH)
    private User user;

    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(length = 1500)
    private String additionalInfo;

}
