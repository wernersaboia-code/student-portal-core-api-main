package br.com.student.portal.service.auth;

import br.com.student.portal.dto.request.UserRequest;
import br.com.student.portal.dto.response.UserResponse;
import br.com.student.portal.entity.User;
import br.com.student.portal.exception.BadRequestException;
import br.com.student.portal.exception.ObjectNotFoundException;
import br.com.student.portal.mapper.UserMapper;
import br.com.student.portal.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static br.com.student.portal.validation.UserValidator.validateFieldsUserRequest;

@AllArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(UserRequest userRequest) {
        validateFieldsUserRequest(userRequest);

        User user = userMapper.userRequestIntoUser(userRequest);

        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        User savedUser = userRepository.save(user);

        return userMapper.userIntoUserResponse(savedUser);
    }


    public UserResponse login(String registration, String password) {
        User user = userRepository.findByRegistration(registration)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Usuário com matrícula " + registration + " não encontrado"
                ));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Matrícula ou senha incorretos");
        }

        return userMapper.userIntoUserResponse(user);
    }
}