package ee.guest.registration.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // TODO Check before saving
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime date;

    private String place;

    @Column(length = 1000)
    private String additionalInfo;

    @ManyToOne
    private User organizer;

    @ManyToMany(cascade = CascadeType.DETACH)
    private Set<User> admins = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<CompanyInvitation> companyInvitations = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<UserInvitation> userInvitations = new HashSet<>();
}
