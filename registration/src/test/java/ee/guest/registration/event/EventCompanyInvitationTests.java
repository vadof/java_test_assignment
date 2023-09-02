package ee.guest.registration.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.guest.registration.controllers.EventController;
import ee.guest.registration.entities.*;
import ee.guest.registration.enums.PaymentMethod;
import ee.guest.registration.forms.CompanyInvitationForm;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class EventCompanyInvitationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    CompanyInvitation companyInvitation;
    CompanyInvitationForm companyInvitationForm;
    User organizer;
    Event event;
    Company registeredCompany;

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

        String invitationName = "Estonia OU";
        Long invitationCode = 12345678L;
        PaymentMethod invitationPaymentMethod = PaymentMethod.BANK_TRANSFER;
        Integer invitationParticipants = 5;
        String invitationAddInfo = "Additional info";

        registeredCompany = new Company();
        registeredCompany.setId(1L);
        registeredCompany.setRegistryCode(12345678L);
        registeredCompany.setName("Estonia OU");

        companyInvitationForm = new CompanyInvitationForm();
        companyInvitationForm.setName(invitationName);
        companyInvitationForm.setRegistryCode(invitationCode);
        companyInvitationForm.setAdditionalInfo(invitationAddInfo);
        companyInvitationForm.setPaymentMethod(invitationPaymentMethod);
        companyInvitationForm.setParticipants(invitationParticipants);

        companyInvitation = new CompanyInvitation();
        companyInvitation.setId(1L);
        companyInvitation.setEvent(event);
        companyInvitation.setParticipants(invitationParticipants);
        companyInvitation.setPaymentMethod(invitationPaymentMethod);
        companyInvitation.setAdditionalInfo(invitationAddInfo);
        companyInvitation.setCompany(registeredCompany);
    }

    @Test
    public void testAddCompanyToEvent_Success() throws Exception {
        when(eventService.addCompanyToEvent(event.getId(), companyInvitationForm, organizer.getPersonalCode()))
                .thenReturn(Optional.of(companyInvitation));

        mockMvc.perform(post("/api/v1/event/" + event.getId() + "/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyInvitationForm))
                        .header("personalCode", organizer.getPersonalCode()))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddCompanyToEvent_NotRegisteredCompany_Success() throws Exception {
        companyInvitationForm.setRegistryCode(99876543L);
        when(eventService.addCompanyToEvent(event.getId(), companyInvitationForm, organizer.getPersonalCode()))
                .thenReturn(Optional.of(companyInvitation));

        mockMvc.perform(post("/api/v1/event/" + event.getId() + "/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyInvitationForm))
                        .header("personalCode", organizer.getPersonalCode()))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddCompanyToEvent_NotExistingEvent_Failure() throws Exception {
        when(eventService.addCompanyToEvent(event.getId(), companyInvitationForm, organizer.getPersonalCode()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/event/" + 100L + "/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyInvitationForm))
                        .header("personalCode", organizer.getPersonalCode()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCompanyToEvent_CompanyAlreadyInvited_Failure() throws Exception {
        eventService.addCompanyToEvent(event.getId(), companyInvitationForm, organizer.getPersonalCode());
        when(eventService.addCompanyToEvent(event.getId(), companyInvitationForm, organizer.getPersonalCode()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/event/" + event.getId() + "/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyInvitationForm))
                        .header("personalCode", organizer.getPersonalCode()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCompanyToEvent_WrongRegistryCode_Failure() throws Exception {
        companyInvitationForm.setRegistryCode(32L);
        when(eventService.addCompanyToEvent(event.getId(), companyInvitationForm, organizer.getPersonalCode()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/event/" + event.getId() + "/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyInvitationForm))
                        .header("personalCode", organizer.getPersonalCode()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCompanyToEvent_NullableValues_Failure() throws Exception {
        companyInvitationForm.setPaymentMethod(null);
        when(eventService.addCompanyToEvent(event.getId(), companyInvitationForm, organizer.getPersonalCode()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/event/" + event.getId() + "/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyInvitationForm))
                        .header("personalCode", organizer.getPersonalCode()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRemoveCompanyFromEvent_Success() throws Exception {
        when(eventService.removeCompanyInvitationFromEvent(event.getId(), companyInvitation.getId(), organizer.getPersonalCode()))
                .thenReturn(Optional.of(1L));

        mockMvc.perform(delete("/api/v1/event/" + event.getId() + "/company/" + companyInvitation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("personalCode", organizer.getPersonalCode()))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveCompanyFromEvent_NoAccessToRemove_Failure() throws Exception {
        when(eventService.removeCompanyInvitationFromEvent(event.getId(), companyInvitation.getId(), 37605030299L))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/event/" + event.getId() + "/company/" + companyInvitation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("personalCode", organizer.getPersonalCode()))
                .andExpect(status().isBadRequest());
    }
}
