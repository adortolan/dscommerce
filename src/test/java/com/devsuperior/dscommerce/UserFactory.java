package com.devsuperior.dscommerce;

import com.devsuperior.dscommerce.entities.Role;
import com.devsuperior.dscommerce.entities.User;

import java.time.LocalDate;

public class UserFactory {

    public static User createClientUser() {
        User user = new User(1l, "Maria", "maria@gmail.com", "123456",
                LocalDate.parse("1999-01-01"), "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");

        user.addRole(new Role(1l, "ROLE_CLIENT"));
        return user;
    }

    public static User createAdmintUser() {
        User user = new User(1l, "Alex", "alex@gmail.com", "977777777",
                LocalDate.parse("1987-12-13"), "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO" );

        user.addRole(new Role(1l, "ROLE_ADMIN"));
        return user;
    }

    public static User createCustomClientUser(Long id, String name){
        User user = new User(id, name, "maria@gmail.com", "123456",
                LocalDate.parse("1999-01-01"), "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");

        user.addRole(new Role(1l, "ROLE_CLIENT"));
        return user;
    }

    public static User createCustomAdminUser(Long id, String name){
        User user = new User(id, name, "maria@gmail.com", "123456",
                LocalDate.parse("1999-01-01"), "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");

        user.addRole(new Role(1l, "ROLE_ADMIN"));
        return user;
    }
}
