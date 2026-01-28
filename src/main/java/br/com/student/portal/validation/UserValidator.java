package br.com.student.portal.validation;

import br.com.student.portal.dto.request.UserRequest;
import br.com.student.portal.entity.User;
import br.com.student.portal.exception.BadRequestException;
import org.springframework.stereotype.Component;

import static br.com.student.portal.validation.FieldValidator.validateRequiredField;
import static io.micrometer.common.util.StringUtils.isEmpty;
import static java.util.regex.Pattern.matches;

@Component
public class UserValidator {


    //TODO: adding regex on the one Utils


    public static void validateName(String name) {
        if (isEmpty(name)) {
            validateRequiredField(name, "Name");
        }

        if (!name.matches("^[a-zA-Z\\s]+$")) {
            throw new BadRequestException("Name should only contain letters and spaces.");
        }
    }

    public static void validateEmail(String email) {
        if (isEmpty(email)) {
            validateRequiredField(email, "Email");
        }

        var emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!matches(emailRegex, email)) {
            throw new BadRequestException("Invalid email format.");
        }

        if (!email.endsWith("@gmail.com")) {
            throw new BadRequestException("Unrecognized Gmail Account Access Attempt");
        }
    }

    public static void validatePassword(String password) {
        if (isEmpty(password)) {
            validateRequiredField(password, "Password");
        }

        var passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
        if (!matches(passwordRegex, password)) {
            throw new BadRequestException("Password must be at least 8 characters long, contain at least one letter, one number, and one special character.");
        }
    }


    public static void validateFieldsUserRequest(UserRequest userRequest) {
        validateName(userRequest.getName());
        validateEmail(userRequest.getEmail());
        validatePassword(userRequest.getPassword());
    }

    public static void validateFields(User user) {
        validateName(user.getName());
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
    }


}



