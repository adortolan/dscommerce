package com.devsuperior.dscommerce.repositories;

import com.devsuperior.dscommerce.entities.PasswordRecover;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long> {
}
