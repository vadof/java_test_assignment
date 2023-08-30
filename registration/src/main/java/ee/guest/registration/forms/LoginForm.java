package ee.guest.registration.forms;

import lombok.Data;

@Data
public class LoginForm {

    private Long personalCode;
    private String firstname;
    private String lastname;
}
