package ee.guest.registration.controllers;

import ee.guest.registration.enums.LoginStatus;
import ee.guest.registration.forms.LoginForm;
import ee.guest.registration.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api/v1/login")
public class AuthorizationController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<LoginStatus> login(@RequestBody LoginForm loginForm) {
        LoginStatus loginStatus = this.userService.loginUser(loginForm);
        if (loginStatus.equals(LoginStatus.SUCCESS)) {
            return ResponseEntity.ok(loginStatus);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginStatus);
        }
    }

}
