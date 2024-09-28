package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.UserFactory;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    private User admin, userClient, otherClient;

    @BeforeEach
    void setUp() throws Exception {
        admin = UserFactory.createAdmintUser();
        userClient = UserFactory.createCustomClientUser(1L,"Ferreira");
        otherClient = UserFactory.createCustomClientUser(2L,"Couto");
    }

    @Test
    public void validateSelfOrAdminShouldDoNothingWhenUserIsAdmin() {
        Mockito.when(userService.authenticated()).thenReturn(admin);
        Long userId = admin.getId();

        Assertions.assertDoesNotThrow(() -> authService.vaidateSelfOrAdmin(userId));
        Mockito.verify(userService).authenticated();
    }

    @Test
    public void validateSelfOrAdminShouldDoNothingWhenUserIsUserClient() {
        Mockito.when(userService.authenticated()).thenReturn(userClient);
        Long userId = userClient.getId();

        Assertions.assertDoesNotThrow(() -> authService.vaidateSelfOrAdmin(userId));
        Mockito.verify(userService).authenticated();
    }

    @Test
    public void validateSelfOrAdminShouldThrowForbiddenExceptionWhenUserIsOtherClient() {
        Mockito.when(userService.authenticated()).thenReturn(userClient);
        Long userId = otherClient.getId();

        Assertions.assertThrows(ForbiddenException.class, () -> authService.vaidateSelfOrAdmin(userId));
    }
}
