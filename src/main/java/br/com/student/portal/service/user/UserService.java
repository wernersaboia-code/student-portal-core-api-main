package br.com.student.portal.service.user;

import br.com.student.portal.dto.request.UserRequest;
import br.com.student.portal.dto.response.UserResponse;
import br.com.student.portal.entity.User;
import br.com.student.portal.exception.ObjectNotFoundException;
import br.com.student.portal.mapper.UserMapper;
import br.com.student.portal.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static br.com.student.portal.validation.UserValidator.validateFieldsUserRequest;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;  // ✅ Adicionar

    // ✅ ADICIONAR ESTE MÉTODO COMPLETO
    public UserResponse createUser(UserRequest userRequest) {
        // Validar campos
        validateFieldsUserRequest(userRequest);

        // Converter DTO para Entity usando o mapper
        User user = userMapper.userRequestIntoUser(userRequest);

        // Criptografar a senha
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        // Salvar no banco de dados
        User savedUser = userRepository.save(user);

        // Retornar resposta
        return userMapper.userIntoUserResponse(savedUser);
    }

    /**
     * Buscar usuário por ID
     */
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado com ID: " + id));
        return userMapper.toResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        var users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new ObjectNotFoundException("No users found");
        }
        return users.stream()
                .map(userMapper::userIntoUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse updateUser(UUID id, UserRequest userRequest) {
        var user = findUserById(id);
        validateFieldsUserRequest(userRequest);

        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());

        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        return userMapper.userIntoUserResponse(userRepository.save(user));
    }

    public void deleteUser(UUID id) {
        var user = findUserById(id);
        userRepository.delete(user);
    }

    private User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User with id " + id + " not found"));
    }

    public UserResponse getUserByRegistration(String registration) {
        User user = userRepository.findByRegistration(registration)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário com matrícula " + registration + " não encontrado"));
        return userMapper.userIntoUserResponse(user);
    }

}
