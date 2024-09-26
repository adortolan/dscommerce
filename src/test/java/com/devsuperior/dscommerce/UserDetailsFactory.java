package com.devsuperior.dscommerce;

import com.devsuperior.dscommerce.projections.UserDetailsProjection;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsFactory {
    public static List<UserDetailsProjection> createCustomUserDetails(String userName) {
        List<UserDetailsProjection> userDetails = new ArrayList<>();

        userDetails.add(new UserDetailsImpl(userName, "123456", 1l, "ROLE_CLIENT"));
        return userDetails;
    }

    public static List<UserDetailsProjection> createCustomAdminDetails(String userName) {
        List<UserDetailsProjection> userDetails = new ArrayList<>();

        userDetails.add(new UserDetailsImpl(userName, "123456", 2l, "ROLE_ADMIN"));
        return userDetails;
    }

    public static List<UserDetailsProjection> createCustomAdminClientDetails(String userName) {
        List<UserDetailsProjection> userDetails = new ArrayList<>();

        userDetails.add(new UserDetailsImpl(userName, "123456", 1l, "ROLE_CLIENT"));
        userDetails.add(new UserDetailsImpl(userName, "123456", 2l, "ROLE_ADMIN"));
        return userDetails;
    }
}

class UserDetailsImpl implements UserDetailsProjection {
    private String username;
    private String password;
    private Long roleId;
    private String authority;

    public UserDetailsImpl(){

    }

    public UserDetailsImpl(String username, String password, Long roleId, String authority) {
        this.username = username;
        this.password = password;
        this.roleId = roleId;
        this.authority = authority;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Long getRoleId() {
        return roleId;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}

