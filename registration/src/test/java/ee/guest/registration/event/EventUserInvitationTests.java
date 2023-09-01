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


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class EventUserInvitationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    UserInvitation userInvitation;
    UserInvitationForm userInvitationForm;
    User organizer;
    Event event;
    User registeredUser;


    @BeforeEach
    public void init() {
        organizer = new User();
        organizer.setFirstname("Vadim");
        organizer.setLastname("Filonov");
        Long personalCode = 50306173710L;
        organizer.setPersonalCode(personalCode);

        event = new Event();
        event.setId(1L);
        event.setName("New event");
        event.setPlace("Tallinn");
        event.setAdditionalInfo("No info");
        event.setDate(LocalDateTime.of(2023, 10, 30, 12, 0));
        event.setOrganizer(organizer);

        String invitationFirstname = "TestName";
        String invitationLastname = "TestLastName";
        PaymentMethod invitationPaymentMethod = PaymentMethod.BANK_TRANSFER;
        Long invitationPersonalCode = 37605030299L;
        String invitationAddInfo = "Additional info";

        registeredUser = new User();
        registeredUser.setFirstname(invitationFirstname);
        registeredUser.setLastname(invitationLastname);
        registeredUser.setPersonalCode(invitationPersonalCode);

        userInvitationForm = new UserInvitationForm();
        userInvitationForm.setFirstname(invitationFirstname);
        userInvitationForm.setLastname(invitationLastname);
        userInvitationForm.setPaymentMethod(invitationPaymentMethod);
        userInvitationForm.setPersonalCode(invitationPersonalCode);
        userInvitationForm.setAdditionalInfo(invitationAddInfo);

        userInvitation = new UserInvitation();
        userInvitation.setAdditionalInfo(invitationAddInfo);
        userInvitation.setEvent(event);
        userInvitation.setUser(registeredUser);
        userInvitation.setPaymentMethod(invitationPaymentMethod);
    }

    @Test
    public void testAddUserToEvent_Success() throws Exception {
        when(eventService.addUserToEvent(event.getId(), userInvitationForm, organizer.getPersonalCode()))
                .thenReturn(Optional.of(userInvitation));

        mockMvc.perform(post("/api/v1/event/" + event.getId() + "/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInvitationForm))
                        .header("personalCode", organizer.getPersonalCode()))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddUserToEvent_NotRegisteredUser_Success() throws Exception {
        userInvitationForm.setPersonalCode(38001085718L);

        when(eventService.addUserToEvent(event.getId(), userInvitationForm, organizer.getPersonalCode()))
                .thenReturn(Optional.of(userInvitation));

        mockMvc.perform(post("/api/v1/event/" + event.getId() + "/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInvitationForm))
                        .header("personalCode", organizer.getPersonalCode()))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddUserToEvent_InvalidPersonalCode_Failure() throws Exception {
        userInvitationForm.setPersonalCode(434L);

        when(eventService.addUserToEvent(event.getId(), userInvitationForm, organizer.getPersonalCode()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/event/" + event.getId() + "/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInvitationForm))
                        .header("personalCode", organizer.getPersonalCode()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddUserToEvent_UserWithoutPrivilegesTryToAdd_Failure() throws Exception {
        event.getUserInvitations().add(userInvitation);
        userInvitationForm.setPersonalCode(38001085718L);

        when(eventService.addUserToEvent(event.getId(), userInvitationForm, organizer.getPersonalCode()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/event/" + event.getId() + "/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInvitationForm))
                        .header("personalCode", userInvitation.getUser().getPersonalCode()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddUserToEvent_NullableValues_Failure() throws Exception {
        userInvitationForm.setPaymentMethod(null);

        when(eventService.addUserToEvent(event.getId(), userInvitationForm, organizer.getPersonalCode()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/event/" + event.getId() + "/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInvitationForm))
                        .header("personalCode", userInvitation.getUser().getPersonalCode()))
                .andExpect(status().isBadRequest());
    }

}
