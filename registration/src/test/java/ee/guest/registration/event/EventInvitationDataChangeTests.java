package ee.guest.registration.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.guest.registration.controllers.EventController;
import ee.guest.registration.entities.Event;
import ee.guest.registration.entities.User;
import ee.guest.registration.entities.UserInvitation;
import ee.guest.registration.enums.PaymentMethod;
import ee.guest.registration.forms.EventForm;
import ee.guest.registration.forms.UserInvitationForm;
import ee.guest.registration.services.EventService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class EventInvitationDataChangeTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    private Event event;

    User organizer;
    User invitedUser;
    UserInvitation userInvitation;

    @BeforeEach
    public void init() {
        String eventName = "New event";
        String eventPlace = "Tallinn";
        String addInfo = "No info";
        LocalDateTime date = LocalDateTime.of(2023, 10, 30, 12, 0);

        event = new Event();
        event.setName(eventName);
        event.setPlace(eventPlace);
        event.setAdditionalInfo(addInfo);
        event.setDate(date);

        organizer = new User();
        organizer.setFirstname("Vadim");
        organizer.setPersonalCode(50306173710L);
        organizer.setLastname("Filonov");
        organizer.setId(1L);

        event.setOrganizer(organizer);
        event.setId(1L);

        invitedUser = new User();
        invitedUser.setFirstname("Test");
        invitedUser.setLastname("Test");
        invitedUser.setPersonalCode(37605030299L);
        invitedUser.setId(2L);

        userInvitation = new UserInvitation();
        userInvitation.setUser(invitedUser);
        userInvitation.setEvent(event);
        userInvitation.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
        userInvitation.setId(1L);
        userInvitation.setAdditionalInfo("dsad");

        event.getUserInvitations().add(userInvitation);
    }

    @Test
    public void testChangeUserData_Success() throws Exception {
        UserInvitationForm userInvitationForm = new UserInvitationForm();
        userInvitationForm.setFirstname("t");
        userInvitationForm.setLastname("d");
        userInvitationForm.setAdditionalInfo("0000");
        userInvitationForm.setPaymentMethod(PaymentMethod.CASH);
        userInvitationForm.setPersonalCode(37605030299L);

        UserInvitation newUserInvitation = new UserInvitation();
        newUserInvitation.setUser(invitedUser);
        newUserInvitation.setEvent(event);
        newUserInvitation.setPaymentMethod(PaymentMethod.CASH);
        newUserInvitation.setAdditionalInfo("0000");

        when(eventService.changeUserInvitationData(event.getId(), userInvitationForm,
                userInvitation.getId(), organizer.getPersonalCode())).thenReturn(Optional.of(newUserInvitation));

        mockMvc.perform(put("/api/v1/event/" + event.getId() + "/user/" + userInvitation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInvitationForm))
                        .header("personalCode", organizer.getPersonalCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.additionalInfo", CoreMatchers.is(newUserInvitation.getAdditionalInfo())));
    }

    @Test
    public void testChangeUserData_InvalidPersonalCode_Failure() throws Exception {
        UserInvitationForm userInvitationForm = new UserInvitationForm();
        userInvitationForm.setFirstname("t");
        userInvitationForm.setLastname("d");
        userInvitationForm.setAdditionalInfo("0000");
        userInvitationForm.setPaymentMethod(PaymentMethod.CASH);
        userInvitationForm.setPersonalCode(34332L);

        when(eventService.changeUserInvitationData(event.getId(), userInvitationForm,
                userInvitation.getId(), organizer.getPersonalCode())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/event/" + event.getId() + "/user/" + userInvitation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInvitationForm))
                        .header("personalCode", organizer.getPersonalCode()))
                .andExpect(status().isBadRequest());
    }
}
