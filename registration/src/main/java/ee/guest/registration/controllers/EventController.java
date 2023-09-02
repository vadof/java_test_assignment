package ee.guest.registration.controllers;

import ee.guest.registration.entities.CompanyInvitation;
import ee.guest.registration.entities.Event;
import ee.guest.registration.entities.UserInvitation;
import ee.guest.registration.forms.CompanyInvitationForm;
import ee.guest.registration.forms.EventForm;
import ee.guest.registration.forms.UserInvitationForm;
import ee.guest.registration.responses.ResponseMessage;
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
        Optional<UserInvitation> userInvitation =
                this.eventService.addUserToEvent(id, userInvitationForm, personalCode);
        if (userInvitation.isPresent()) {
            return ResponseEntity.ok(userInvitation.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add user");
        }
    }

    @PostMapping("/{id}/company")
    public ResponseEntity<?> addCompanyToEvent(@PathVariable Long id,
                                            @RequestBody CompanyInvitationForm companyInvitationForm,
                                            @RequestHeader Long personalCode) {
        Optional<CompanyInvitation> companyInvitation =
                this.eventService.addCompanyToEvent(id, companyInvitationForm, personalCode);
        if (companyInvitation.isPresent()) {
            return ResponseEntity.ok(companyInvitation.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add company");
        }
    }

    @DeleteMapping("/{eventId}/user/{invitationId}")
    public ResponseEntity<ResponseMessage> removeUserFromEvent(@PathVariable Long eventId,
                                                               @PathVariable Long invitationId,
                                                               @RequestHeader Long personalCode) {
        Optional<Long> removedId =
                this.eventService.removeUserInvitationFromEvent(eventId, invitationId, personalCode);
        if (removedId.isPresent()) {
            return ResponseEntity.ok(new ResponseMessage("User invitation removed"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("Failed to remove user invitation"));
        }
    }

    @DeleteMapping("/{eventId}/company/{invitationId}")
    public ResponseEntity<ResponseMessage> removeCompanyInvitationFromEvent(@PathVariable Long eventId,
                                                                            @PathVariable Long invitationId,
                                                                            @RequestHeader Long personalCode) {
        Optional<Long> removedId =
                this.eventService.removeCompanyInvitationFromEvent(eventId, invitationId, personalCode);
        if (removedId.isPresent()) {
            return ResponseEntity.ok(new ResponseMessage("Company invitation removed"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("Failed to remove company invitation"));
        }
    }

}
