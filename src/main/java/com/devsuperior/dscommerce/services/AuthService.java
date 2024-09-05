package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.EmailBodyDTO;
import com.devsuperior.dscommerce.dto.NewPasswordDTO;
import com.devsuperior.dscommerce.entities.PasswordRecover;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.repositories.PasswordRecoverRepository;
import com.devsuperior.dscommerce.repositories.UserRepository;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    @Value("${email.password-recover.uri}")
    private String recoverUri;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoverRepository recoverRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void vaidateSelfOrAdmin(Long userId) {
        User user = userService.authenticated();
        if (!user.hasHole("ROLE_ADMIN") && !user.getId().equals(userId)) {
            throw new ForbiddenException("Acesso negado");
        }
    }

    @Transactional
    public void createRecoverToken(EmailBodyDTO body) {
        User user = userRepository.findByEmail(body.getEmail());

        if(user == null) {
            throw new ResourceNotFoundException("Email não encontrado");
        }

        PasswordRecover recover = new PasswordRecover();
        recover.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));
        recover.setEmail(body.getEmail());

        String token = UUID.randomUUID().toString();

        recover.setToken(token);
        recoverRepository.save(recover);

        String text = "Acesse o link para recuperar sua senha: \n\n" +
                recoverUri + token;

        emailService.sendEmail(body.getEmail(), "Recuperação de senha", text);
    }

    @Transactional
    public void saveNewPassword(NewPasswordDTO body) {
        List<PasswordRecover> result = recoverRepository.searchValidTokens(body.getToken(), Instant.now());
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Token inválido");
        }
        PasswordRecover recover = result.get(0);
        User user = userRepository.findByEmail(recover.getEmail());
        user.setPassword(passwordEncoder.encode(body.getPassword()));
        userRepository.save(user);
        recoverRepository.delete(recover);
    }
}
