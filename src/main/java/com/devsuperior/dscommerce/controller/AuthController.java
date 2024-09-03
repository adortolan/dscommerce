package com.devsuperior.dscommerce.controller;

import com.devsuperior.dscommerce.dto.*;
import com.devsuperior.dscommerce.services.AuthService;
import com.devsuperior.dscommerce.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping(value = "/recover-token")
    public ResponseEntity<Void> createRecoverToken(@RequestBody @Valid EmailBodyDTO body) {

        authService.createRecoverToken(body);
        return ResponseEntity.noContent().build();

    }

}
