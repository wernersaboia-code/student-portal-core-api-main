package br.com.student.portal.service.auth;

import br.com.student.portal.entity.User;
import com.auth0.jwt.JWT;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;

@Service
public class TokenService {
    @Value("${api.security.token}")
    private String secret;

    public String generateToken(User user){
        try{
            var alg = HMAC256(secret);
            return JWT.create()
                    .withIssuer("api/users")
                    .withSubject(user.getEmail())
                    .withExpiresAt(LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00")))
                    .sign(alg);

        }catch (JWTCreationException jwtCreationException){
            throw new RuntimeException("Erro ao gerar um token", jwtCreationException);
        }

    }
    public String validateToken(String token){
        try {
            var alg = HMAC256(secret);
            return JWT.require(alg)
                    .withIssuer("api/users")
                    .build()
                    .verify(token)
                    .getSubject();

        }catch (JWTVerificationException jwtVerificationException){
            throw new JWTVerificationException("Erro ao validar token", jwtVerificationException);
        }
    }
}
