package ee.guest.registration.services;

import ee.guest.registration.entities.User;
import ee.guest.registration.enums.LoginStatus;
import ee.guest.registration.forms.LoginForm;
import ee.guest.registration.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public boolean userIsValid(Long personalCode) {
        return userRepository.findByPersonalCode(personalCode).isPresent();
    }

    public Optional<User> getUserByPersonalCode(Long personalCode) {
        return userRepository.findByPersonalCode(personalCode);
    }

    public LoginStatus loginUser(LoginForm loginForm) {
        if (userRepository.findByPersonalCode(loginForm.getPersonalCode()).isPresent()) {
            return LoginStatus.SUCCESS;
        }

        if (loginForm.getFirstname().length() > 0
                && loginForm.getLastname().length() > 0) {
            if (loginForm.getPersonalCode() != null &&
                    this.personalCodeIsValid(loginForm.getPersonalCode())) {
                User user = new User();
                user.setPersonalCode(loginForm.getPersonalCode());
                user.setFirstname(loginForm.getFirstname().trim());
                user.setLastname(loginForm.getLastname().trim());

                userRepository.save(user);

                return LoginStatus.SUCCESS;
            } else {
                return LoginStatus.INVALID_ISIKUKOOD;
            }
        } else {
            return LoginStatus.MISSING_FIRST_AND_LAST_NAME;
        }
    }


    public boolean personalCodeIsValid(Long personalCode) {
        final int idCodeLength = 11;
        String personalCodeStr = personalCode.toString();
        if (personalCodeStr.length() == idCodeLength) {
            return isGenderNumberCorrect(personalCodeStr) && isDayNumberCorrect(personalCodeStr)
                    && isMonthNumberCorrect(personalCodeStr) && isYearNumberCorrect(personalCodeStr)
                    && isControlNumberCorrect(personalCodeStr);
        } else {
            return false;
        }
    }

    private int getFullYear(String personalCode) {
        String year = personalCode.substring(1, 3);
        int firstNumber = Integer.parseInt(personalCode.substring(0, 1));

        if (firstNumber == 1 || firstNumber == 2) {
            return Integer.parseInt("18" + year);
        } else if (firstNumber == 3 || firstNumber == 4) {
            return Integer.parseInt("19" + year);
        } else {
            return Integer.parseInt("20" + year);
        }
    }

    private boolean isGenderNumberCorrect(String personalCode) {
        List<Integer> correctNumbers = List.of(1, 2, 3, 4, 5, 6);
        int genderNumber = Integer.parseInt(personalCode.substring(0, 1));
        return correctNumbers.contains(genderNumber);
    }

    private boolean isYearNumberCorrect(String personalCode) {
        int numbers = Integer.parseInt(personalCode.substring(1, 3));
        final int maxYear = 99;
        return 0 <= numbers && numbers <= maxYear;
    }

    private boolean isMonthNumberCorrect(String personalCode) {
        int numbers = Integer.parseInt(personalCode.substring(3, 5));
        final int maxMonth = 12;
        return 1 <= numbers && numbers <= maxMonth;
    }

    private boolean isDayNumberCorrect(String personalCode) {
        int dayNumber = Integer.parseInt(personalCode.substring(5, 7));
        int monthNumber = Integer.parseInt(personalCode.substring(3, 5));

        final List<Integer> monthNumbers31 = Arrays.asList(1, 3, 5, 7, 8, 10, 12);
        final List<Integer> monthNumbers30 = Arrays.asList(4, 6, 9, 11);

        final int februaryDay = 28;
        final int februaryLeapDay = 29;
        final int maxDay1 = 31;
        final int maxDay2 = 30;

        if (monthNumber == 2 && dayNumber <= februaryDay) {
            return true;
        } else if (isLeapYear(getFullYear(personalCode)) && monthNumber == 2 && dayNumber == februaryLeapDay) {
            return true;
        } else if (monthNumbers31.contains(monthNumber) && dayNumber <= maxDay1) {
            return true;
        } else {
            return monthNumbers30.contains(monthNumber) && dayNumber <= maxDay2;
        }
    }

    private int getIdCodeSum(List<Integer> scale, String personalCode) {
        int sum = 0;
        for (int i = 0; i < scale.size(); i++) {
            sum += scale.get(i) * Character.getNumericValue(personalCode.charAt(i));
        }
        return sum;
    }

    private boolean isControlNumberCorrect(String personalCode) {
        int controlNumber = Character.getNumericValue(personalCode.charAt(10));

        final int idCodeLength = 11;
        int sum = getIdCodeSum(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 1), personalCode);
        int remainder = sum % idCodeLength;
        // First algorithm
        if (remainder == controlNumber) {
            return true;
        } else if (remainder == 10) {
            // Second algorithm
            sum = getIdCodeSum(List.of(3, 4, 5, 6, 7, 8, 9, 1, 2, 3), personalCode);
            remainder = sum % idCodeLength;
            if (remainder == controlNumber) {
                return true;
            } else return remainder == 10;
        } else {
            return false;
        }
    }

    private boolean isLeapYear(int fullYear) {
        final int leapYearNumber = 400;
        if (fullYear % leapYearNumber == 0) {
            return true;
        } else return fullYear % 4 == 0 && fullYear % 100 != 0;
    }

}
