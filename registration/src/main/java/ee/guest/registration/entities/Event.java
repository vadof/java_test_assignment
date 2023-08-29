package ee.guest.registration.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import ee.guest.registration.annotations.FutureDate;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @FutureDate
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime date;

    private String place;

    @Column(length = 1000)
    private String additionalInfo;

    @ManyToOne
    private User organizer;

    @ManyToMany(cascade = CascadeType.DETACH)
    private Set<User> admins = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private Set<CompanyInvitation> companyInvitations = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private Set<UserInvitation> userInvitations = new HashSet<>();
}
