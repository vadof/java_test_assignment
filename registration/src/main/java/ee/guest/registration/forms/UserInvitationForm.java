package ee.guest.registration.forms;

import ee.guest.registration.enums.PaymentMethod;
import lombok.Data;

@Data
public class UserInvitationForm {

    private String firstname;
    private String lastname;
    private Long personalCode;
    private PaymentMethod paymentMethod;
    private String additionalInfo;

}
