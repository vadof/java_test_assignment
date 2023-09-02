package ee.guest.registration.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.guest.registration.controllers.EventController;
import ee.guest.registration.entities.Event;
import ee.guest.registration.entities.User;
import ee.guest.registration.entities.UserInvitation;
import ee.guest.registration.enums.PaymentMethod;
import ee.guest.registration.forms.EventForm;
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
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class EventTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private EventForm eventForm;
    private Event event;

    User organizer;
    User invitedUser;
    UserInvitation userInvitation;

    @BeforeEach
    public void init() {
        user = new User();
        user.setFirstname("Vadim");
        user.setLastname("Filonov");
        Long personalCode = 50306173710L;
        user.setPersonalCode(personalCode);

        String eventName = "New event";
        String eventPlace = "Tallinn";
        String addInfo = "No info";
        LocalDateTime date = LocalDateTime.of(2023, 10, 30, 12, 0);

        eventForm = new EventForm();
        eventForm.setName(eventName);
        eventForm.setPlace(eventPlace);
        eventForm.setAdditionalInfo(addInfo);
        eventForm.setDate(date);

        event = new Event();
        event.setName(eventName);
        event.setPlace(eventPlace);
        event.setAdditionalInfo(addInfo);
        event.setDate(date);
    }

    @Test
    public void testAddEvent_Success() throws Exception {
        when(eventService.createNewEvent(eventForm, user.getPersonalCode())).thenReturn(Optional.of(event));

        mockMvc.perform(post("/api/v1/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventForm))
                .header("personalCode", user.getPersonalCode()))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddEvent_PersonalCodeFailure() throws Exception {
        when(eventService.createNewEvent(eventForm, 60306173710L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventForm))
                        .header("personalCode", 60306173710L))
                        .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddEvent_DateFailure() throws Exception {
        eventForm.setDate(LocalDateTime.of(2000, 10, 30, 12, 0));
        when(eventService.createNewEvent(eventForm, user.getPersonalCode())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventForm))
                .header("personalCode", user.getPersonalCode()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetEvent_Success() throws Exception {
        event.setOrganizer(user);
        event.setId(2L);

        when(eventService.getEvent(event.getId(), user.getPersonalCode())).thenReturn(Optional.of(event));

        ResultActions result = mockMvc.perform(get("/api/v1/event/" + event.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("personalCode", user.getPersonalCode()));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(event.getName())));
    }

    @Test
    public void testGetEvent_NoAccess_Failure() throws Exception {
        event.setId(2L);

        when(eventService.getEvent(event.getId(), user.getPersonalCode())).thenReturn(Optional.empty());

        System.out.println(event.getId());
        System.out.println(event.getOrganizer());

        mockMvc.perform(get("/api/v1/event/" + event.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("personalCode", user.getPersonalCode()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetEvent_NotExistingEvent_Failure() throws Exception {
        event.setId(2L);

        when(eventService.getEvent(event.getId(), user.getPersonalCode())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/event/" + 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("personalCode", user.getPersonalCode()))
                .andExpect(status().isForbidden());
    }

    private void addUsersToEvent() {
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
    public void testUserLeaveFromEvent_Success() throws Exception {
        addUsersToEvent();

        when(eventService.leaveFromEvent(event.getId(), invitedUser.getPersonalCode())).thenReturn(Optional.of(1L));

        mockMvc.perform(delete("/api/v1/event/" + event.getId() + "/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("personalCode", invitedUser.getPersonalCode()))
                .andExpect(status().isOk());
    }

    @Test
    public void testUserLeaveFromEvent_OrganizerLeave_Success() throws Exception {
        addUsersToEvent();

        when(eventService.leaveFromEvent(event.getId(), organizer.getPersonalCode())).thenReturn(Optional.of(0L));

        mockMvc.perform(delete("/api/v1/event/" + event.getId() + "/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("personalCode", organizer.getPersonalCode()))
                .andExpect(status().isOk());
    }

}
