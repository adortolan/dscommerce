package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.UserDetailsFactory;
import com.devsuperior.dscommerce.UserFactory;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.projections.UserDetailsProjection;
import com.devsuperior.dscommerce.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {
    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    private String invalidEmail, validEmail;
    private List<UserDetailsProjection> userDetails;
    private User user;

    @BeforeEach
    private void setUp() throws Exception {
        invalidEmail = "invalidEmail@email.com";
        validEmail = "maria@gmail.com";

        user = UserFactory.createCustomAdminUser(1L, validEmail);
        userDetails = UserDetailsFactory.createCustomAdminDetails(validEmail);

        Mockito.when(repository.searchUserAndRolesByEmail(validEmail)).thenReturn(userDetails);
        Mockito.when(repository.searchUserAndRolesByEmail(invalidEmail))
                .thenReturn(new ArrayList<>());
    }


    @Test
    public void testLoadUserByUsernameWhenUserNotFound() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(invalidEmail));
    }

    @Test
    public void testLoadUserByUsernameWhenUserFound() {
        UserDetails user = service.loadUserByUsername(validEmail);
        Assertions.assertEquals(userDetails.get(0).getUsername(), user.getUsername());
        Assertions.assertEquals(userDetails.get(0).getPassword(), user.getPassword());
        Assertions.assertEquals(userDetails.get(0).getAuthority(), user.getAuthorities().iterator().next().getAuthority());
    }
}
