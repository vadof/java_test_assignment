package ee.guest.registration.controllers;

import ee.guest.registration.entities.Event;
import ee.guest.registration.forms.EventForm;
import ee.guest.registration.services.EventService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/event")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<?> addEvent(@RequestBody EventForm eventForm, @RequestHeader Integer personalCode) {
        Optional<Event> optionalEvent = this.eventService.createNewEvent(eventForm, personalCode);
        if (optionalEvent.isPresent()) {
            return ResponseEntity.ok(optionalEvent);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create event.");
    }

}
