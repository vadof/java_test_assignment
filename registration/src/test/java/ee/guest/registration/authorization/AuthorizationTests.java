package ee.guest.registration.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.guest.registration.controllers.AuthorizationController;
import ee.guest.registration.entities.User;
import ee.guest.registration.enums.LoginStatus;
import ee.guest.registration.forms.LoginForm;
import ee.guest.registration.repositories.UserRepository;
import ee.guest.registration.services.UserService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorizationController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class AuthorizationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private LoginForm loginForm;

    @BeforeEach
    public void init() {
        loginForm = new LoginForm();
        loginForm.setFirstname("Vadim");
        loginForm.setLastname("Filonov");
        loginForm.setPersonalCode(50306173710L);
    }

    private User createUser() {
        User user = new User();
        user.setFirstname("Vadim");
        user.setLastname("Filonov");
        user.setPersonalCode(50306173710L);
        userRepository.save(user);
        return user;
    }

    @Test
    public void testLoginUser_InvalidPersonalCodeLogin_Failure() throws Exception {
        when(userService.loginUser(loginForm)).thenReturn(LoginStatus.INVALID_ISIKUKOOD);

        loginForm.setPersonalCode(60306173710L);

        mockMvc.perform(post("/api/v1/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginForm)))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginUser_NotExistingUserWithCorrectPersonalCodeWithoutNameInForm_Failure() throws Exception {
        when(userService.loginUser(loginForm)).thenReturn(LoginStatus.MISSING_FIRST_AND_LAST_NAME);

        loginForm.setLastname("");
        loginForm.setFirstname("");

        mockMvc.perform(post("/api/v1/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginForm)))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginUser_NotExistingUser_Success() throws Exception {
        when(userService.loginUser(loginForm)).thenReturn(LoginStatus.SUCCESS);

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginForm)))
                        .andExpect(status().isOk());
    }

    @Test
    public void testLoginUser_ExistingUser_Success() throws Exception {
        when(userService.loginUser(loginForm)).thenReturn(LoginStatus.SUCCESS);
        createUser();
        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginForm)))
                        .andExpect(status().isOk());
    }

    @Test
    public void testLoginUser_ExistingUserWithoutNameInForm_Success() throws Exception {
        when(userService.loginUser(loginForm)).thenReturn(LoginStatus.SUCCESS);
        createUser();
        loginForm.setFirstname("");
        loginForm.setLastname("");

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginForm)))
                        .andExpect(status().isOk());
    }

}
