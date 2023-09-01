package ee.guest.registration.controllers;

import ee.guest.registration.entities.Event;
import ee.guest.registration.forms.EventForm;
import ee.guest.registration.forms.UserInvitationForm;
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
    public ResponseEntity<?> addEvent(@RequestBody EventForm eventForm, @RequestHeader Long personalCode) {
        Optional<Event> optionalEvent = this.eventService.createNewEvent(eventForm, personalCode);
        if (optionalEvent.isPresent()) {
            return ResponseEntity.ok(optionalEvent.get().getId());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create event.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable Long id, @RequestHeader Long personalCode) {
        Optional<Event> optionalEvent = this.eventService.getEvent(id, personalCode);
        if (optionalEvent.isPresent()) {
            return ResponseEntity.ok(optionalEvent.get());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
        }
    }

    @PostMapping("/{id}/user")
    public ResponseEntity<?> addUserToEvent(@PathVariable Long id,
                                            @RequestBody UserInvitationForm userInvitationForm,
                                            @RequestHeader Long personalCode) {
        boolean added = this.eventService.addUserToEvent(id, userInvitationForm, personalCode);
        if (added) {
            return ResponseEntity.ok("User added");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add user");
        }
    }

}
