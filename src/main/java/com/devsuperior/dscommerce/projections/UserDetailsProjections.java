package com.devsuperior.dscommerce.projections;

public interface UserDetailsProjections {
    String getUsername();
    String getPassword();
    Long getRoleId();
    String getAuthority();
}
