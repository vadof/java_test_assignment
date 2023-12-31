package ee.guest.registration.controllers;

import ee.guest.registration.entities.CompanyInvitation;
import ee.guest.registration.entities.Event;
import ee.guest.registration.entities.User;
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

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEvent(@PathVariable Long eventId, @RequestHeader Long personalCode) {
        Optional<Event> optionalEvent = this.eventService.getEvent(eventId, personalCode);
        if (optionalEvent.isPresent()) {
            return ResponseEntity.ok(optionalEvent.get());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
        }
    }

    @PutMapping("/{eventId}/user/{invitationId}")
    public ResponseEntity<?> changeUserInvitationData(@PathVariable Long eventId,
                                                      @PathVariable Long invitationId,
                                                      @RequestBody UserInvitationForm userInvitationForm,
                                                      @RequestHeader Long personalCode) {
        Optional<UserInvitation> optionalUserInvitation =
                this.eventService.changeUserInvitationData(eventId, userInvitationForm, invitationId, personalCode);
        if (optionalUserInvitation.isPresent()) {
            return ResponseEntity.ok(optionalUserInvitation.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update user");
        }
    }

    @PostMapping("/{eventId}/user")
    public ResponseEntity<?> addUserToEvent(@PathVariable Long eventId,
                                            @RequestBody UserInvitationForm userInvitationForm,
                                            @RequestHeader Long personalCode) {
        Optional<UserInvitation> userInvitation =
                this.eventService.addUserToEvent(eventId, userInvitationForm, personalCode);
        if (userInvitation.isPresent()) {
            return ResponseEntity.ok(userInvitation.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add user");
        }
    }

    @PostMapping("/{eventId}/company")
    public ResponseEntity<?> addCompanyToEvent(@PathVariable Long eventId,
                                               @RequestBody CompanyInvitationForm companyInvitationForm,
                                               @RequestHeader Long personalCode) {
        Optional<CompanyInvitation> companyInvitation =
                this.eventService.addCompanyToEvent(eventId, companyInvitationForm, personalCode);
        if (companyInvitation.isPresent()) {
            return ResponseEntity.ok(companyInvitation.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add company");
        }
    }

    @PutMapping("/{eventId}/company/{invitationId}")
    public ResponseEntity<?> changeCompanyInvitationData(@PathVariable Long eventId,
                                                      @PathVariable Long invitationId,
                                                      @RequestBody CompanyInvitationForm companyInvitationForm,
                                                      @RequestHeader Long personalCode) {
        Optional<CompanyInvitation> optionalCompanyInvitation =
                this.eventService.changeCompanyInvitationData(eventId, companyInvitationForm, invitationId, personalCode);
        if (optionalCompanyInvitation.isPresent()) {
            return ResponseEntity.ok(optionalCompanyInvitation.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update company");
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

    @DeleteMapping("/{eventId}/user")
    public ResponseEntity<ResponseMessage> leaveFromEvent(@PathVariable Long eventId,
                                                               @RequestHeader Long personalCode) {
        Optional<Long> optionalId = this.eventService.leaveFromEvent(eventId, personalCode);
        if (optionalId.isPresent()) {
            return ResponseEntity.ok(new ResponseMessage("You are removed from the event"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("An error occurred while leaving the event"));
        }
    }

    @PostMapping("/{eventId}/moderator")
    public ResponseEntity<?> setUserAsModerator(@PathVariable Long eventId,
                                                              @RequestBody User user,
                                                              @RequestHeader Long personalCode) {
        Optional<User> optionalUser =
                this.eventService.changeUserModeratorRoleAtEvent(eventId, user, personalCode);
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("Failed to change user role"));
        }
    }
}
