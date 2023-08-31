package ee.guest.registration.services;

import ee.guest.registration.entities.Event;
import ee.guest.registration.entities.User;
import ee.guest.registration.forms.EventForm;
import ee.guest.registration.repositories.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    public final UserService userService;

    public Optional<Event> createNewEvent(EventForm eventForm, Long organizerPersonalCode) {
        Optional<User> user = userService.getUserByPersonalCode(organizerPersonalCode);
        if (user.isPresent() && eventForm.getDate().isAfter(LocalDateTime.now())) {
            Event event = Event.builder()
                    .name(eventForm.getName())
                    .place(eventForm.getPlace())
                    .additionalInfo(eventForm.getAdditionalInfo())
                    .date(eventForm.getDate())
                    .organizer(user.get())
                    .build();

            this.eventRepository.save(event);

            log.info("New event created");
            return Optional.of(event);
        }
        return Optional.empty();
    }

    public Optional<Event> getEvent(Long id, Long personalCode) {
        Optional<Event> event = this.eventRepository.findById(id);
        Optional<User> user = this.userService.getUserByPersonalCode(personalCode);

        System.out.println(event.isPresent());
        System.out.println(user.isPresent());
        System.out.println(userHasAccessToEvent(user.get(), event.get()));

        if (event.isPresent() && user.isPresent()
                && userHasAccessToEvent(user.get(), event.get())) {
            return event;
        } else {
            return Optional.empty();
        }
    }

    private boolean userHasAccessToEvent(User user, Event event) {
        boolean organizer = user.equals(event.getOrganizer());
        boolean admin = event.getAdmins().contains(user);
        boolean invited = event.getUserInvitations()
                .stream()
                .anyMatch(userInvitation -> userInvitation.getUser().equals(user));

        return organizer || admin || invited;
    }
}
