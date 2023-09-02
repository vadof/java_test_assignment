package ee.guest.registration.services;

import ee.guest.registration.entities.*;
import ee.guest.registration.forms.CompanyInvitationForm;
import ee.guest.registration.forms.EventForm;
import ee.guest.registration.forms.UserInvitationForm;
import ee.guest.registration.repositories.CompanyInvitationRepository;
import ee.guest.registration.repositories.EventRepository;
import ee.guest.registration.repositories.UserInvitationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final CompanyService companyService;
    private final UserInvitationRepository userInvitationRepository;
    private final CompanyInvitationRepository companyInvitationRepository;

    public Optional<Event> createNewEvent(EventForm eventForm, Long organizerPersonalCode) {
        Optional<User> user = userService.getUserByPersonalCode(organizerPersonalCode);
        if (user.isPresent() && eventForm.getDate().isAfter(LocalDateTime.now())) {
            Event event = Event.builder()
                    .name(eventForm.getName())
                    .place(eventForm.getPlace())
                    .additionalInfo(eventForm.getAdditionalInfo())
                    .date(eventForm.getDate())
                    .organizer(user.get())
                    .build();

            this.eventRepository.save(event);

            log.info("Created new event - {}", eventForm.getName());
            return Optional.of(event);
        }
        return Optional.empty();
    }

    public Optional<Event> getEvent(Long id, Long personalCode) {
        Optional<Event> event = this.eventRepository.findById(id);
        Optional<User> user = this.userService.getUserByPersonalCode(personalCode);

        if (event.isPresent() && user.isPresent()
                && userHasAccessToEvent(user.get(), event.get())) {
            return event;
        } else {
            return Optional.empty();
        }
    }

    private boolean userHasAccessToEvent(User user, Event event) {
        boolean organizer = user.equals(event.getOrganizer());
        boolean admin = event.getAdmins().contains(user);
        boolean invited = event.getUserInvitations()
                .stream()
                .anyMatch(userInvitation -> userInvitation.getUser().equals(user));

        return organizer || admin || invited;
    }

    @Transactional
    public Optional<UserInvitation> addUserToEvent(Long eventId, UserInvitationForm userInvitationForm, Long personalCode) {
        try {
            Event event = this.eventRepository.findById(eventId).orElseThrow();

            if (!this.userHasAccessToAddOrRemoveMembers(personalCode, event)) {
                return Optional.empty();
            }

            Optional<User> optionalUser = this.userService.getUserByPersonalCode(userInvitationForm.getPersonalCode());

            UserInvitation userInvitation = new UserInvitation();

            if (optionalUser.isPresent()) {
                if (!this.userAlreadyInvitedToEvent(event, optionalUser.get())) {
                    userInvitation.setUser(optionalUser.get());
                } else {
                    return Optional.empty();
                }
            } else {
                User user = this.userService.createNewUser(userInvitationForm.getPersonalCode(),
                        userInvitationForm.getFirstname(), userInvitationForm.getLastname()).orElseThrow();
                userInvitation.setUser(user);
            }

            userInvitation.setPaymentMethod(userInvitationForm.getPaymentMethod());
            userInvitation.setAdditionalInfo(userInvitationForm.getAdditionalInfo());
            userInvitation.setEvent(event);

            this.userInvitationRepository.save(userInvitation);

            event.getUserInvitations().add(userInvitation);
            this.eventRepository.save(event);

            return Optional.of(userInvitation);
        } catch (Exception e) {
            log.error("Error adding user to event {}, eventId = {}, personalCode = {}", e.getMessage(), eventId,
                    userInvitationForm.getPersonalCode());
            return Optional.empty();
        }
    }

    private boolean userAlreadyInvitedToEvent(Event event, User user) {
        return event.getOrganizer().equals(user) || event.getUserInvitations()
                .stream()
                .anyMatch(userInvitation -> userInvitation.getUser().equals(user));
    }

    @Transactional
    public Optional<CompanyInvitation> addCompanyToEvent(Long eventId, CompanyInvitationForm companyInvitationForm, Long personalCode) {
        try {
            Event event = this.eventRepository.findById(eventId).orElseThrow();

            if (!this.userHasAccessToAddOrRemoveMembers(personalCode, event)) {
                return Optional.empty();
            }

            Optional<Company> optionalCompany = this.companyService.getCompanyByRegistryCode(companyInvitationForm.getRegistryCode());

            CompanyInvitation companyInvitation = new CompanyInvitation();

            if (optionalCompany.isPresent()) {
                if (!this.companyAlreadyInvitedToEvent(event, optionalCompany.get())) {
                    companyInvitation.setCompany(optionalCompany.get());
                } else {
                    return Optional.empty();
                }
            } else {
                Company company = this.companyService.createNewCompany(companyInvitationForm.getName(),
                        companyInvitationForm.getRegistryCode()).orElseThrow();
                companyInvitation.setCompany(company);
            }

            companyInvitation.setPaymentMethod(companyInvitationForm.getPaymentMethod());
            companyInvitation.setParticipants(companyInvitationForm.getParticipants());
            companyInvitation.setAdditionalInfo(companyInvitationForm.getAdditionalInfo());
            companyInvitation.setEvent(event);

            this.companyInvitationRepository.save(companyInvitation);

            event.getCompanyInvitations().add(companyInvitation);
            this.eventRepository.save(event);

            return Optional.of(companyInvitation);
        } catch (Exception e) {
            log.error("Error adding company to event {}, eventId = {}, registryCode = {}", e.getMessage(), eventId,
                    companyInvitationForm.getRegistryCode());
            return Optional.empty();
        }
    }

    private boolean companyAlreadyInvitedToEvent(Event event, Company company) {
        return event.getCompanyInvitations()
                .stream()
                .anyMatch(companyInvitation -> companyInvitation.getCompany().equals(company));
    }

    @Transactional
    public Optional<Long> removeUserInvitationFromEvent(Long eventId, Long invitationId, Long personalCode) {
        try {
            Optional<Event> eventOptional = this.eventRepository.findById(eventId);
            Optional<UserInvitation> invitationOptional = this.userInvitationRepository.findById(invitationId);

            if (eventOptional.isPresent() && invitationOptional.isPresent()) {
                Event event = eventOptional.get();
                UserInvitation userInvitation = invitationOptional.get();

                if (this.userHasAccessToAddOrRemoveMembers(personalCode, event)) {
                    event.getAdmins().remove(userInvitation.getUser());
                    event.getUserInvitations().remove(userInvitation);
                    this.eventRepository.save(event);

                    this.userInvitationRepository.delete(userInvitation);
                    return Optional.of(userInvitation.getId());
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error deleting user invitation from event: {} eventId = {}, invitationId - {}",
                    e.getMessage(), eventId, invitationId);
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<Long> removeCompanyInvitationFromEvent(Long eventId, Long invitationId, Long personalCode) {
        try {
            Optional<Event> eventOptional = this.eventRepository.findById(eventId);
            Optional<CompanyInvitation> invitationOptional = this.companyInvitationRepository.findById(invitationId);

            if (eventOptional.isPresent() && invitationOptional.isPresent()) {
                Event event = eventOptional.get();
                CompanyInvitation companyInvitation = invitationOptional.get();

                if (this.userHasAccessToAddOrRemoveMembers(personalCode, event)) {
                    event.getCompanyInvitations().remove(companyInvitation);
                    this.eventRepository.save(event);

                    this.companyInvitationRepository.delete(companyInvitation);
                    return Optional.of(companyInvitation.getId());
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error deleting company invitation from event: {} eventId = {}, invitationId - {}",
                    e.getMessage(), eventId, invitationId);
            return Optional.empty();
        }
    }

    private boolean userHasAccessToAddOrRemoveMembers(Long personalCode, Event event) {
        Optional<User> optionalUser = this.userService.getUserByPersonalCode(personalCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return event.getOrganizer().equals(user) || event.getAdmins().contains(user);
        }
        return false;
    }
}
