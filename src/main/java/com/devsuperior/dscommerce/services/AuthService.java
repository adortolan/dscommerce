package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    public void vaidateSelfOrAdmin(Long userId) {
        User user = userService.authenticated();
        if (!user.hasHole("ROLE_ADMIN") && !user.getId().equals(userId)) {
            throw new ForbiddenException("Acesso negado");
        }
    }
}
