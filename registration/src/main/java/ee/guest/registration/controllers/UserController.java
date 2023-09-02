package ee.guest.registration.controllers;

import ee.guest.registration.entities.Event;
import ee.guest.registration.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllUserEvents(@RequestHeader Long personalCode) {
        return ResponseEntity.ok(userService.getAllUserEvents(personalCode));
    }

}
