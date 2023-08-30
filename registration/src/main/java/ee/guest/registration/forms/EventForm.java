package ee.guest.registration.forms;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventForm {

    private String name;
    private LocalDateTime date;
    private String place;
    private String additionalInfo;

}
