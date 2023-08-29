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

    public Optional<Event> createNewEvent(EventForm eventForm, Integer organizerPersonalCode) {
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
            return Optional.of(event);
        }
        return Optional.empty();
    }

}
