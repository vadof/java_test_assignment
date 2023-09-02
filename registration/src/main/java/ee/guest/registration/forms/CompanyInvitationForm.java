package ee.guest.registration.forms;

import ee.guest.registration.enums.PaymentMethod;
import lombok.Data;

@Data
public class CompanyInvitationForm {

    private String name;
    private Long registryCode;
    private Integer participants;
    private PaymentMethod paymentMethod;
    private String additionalInfo;

}
