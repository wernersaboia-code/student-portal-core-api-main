package br.com.student.portal.controller;

import br.com.student.portal.dto.request.UserRequest;
import br.com.student.portal.dto.response.UserResponse;
import br.com.student.portal.service.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest userRequest) {
        var registeredUser = authService.registerUser(userRequest);
        return ResponseEntity.status(CREATED).body(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest loginRequest) {
        var user = authService.login(
                loginRequest.getRegistration(),
                loginRequest.getPassword()
        );
        return ResponseEntity.status(OK).body(user);
    }
}