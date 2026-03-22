package com.fret.io.auth_service.repository;

import com.fret.io.auth_service.model.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRoleRepository extends JpaRepository <UserRoles, UUID> {
}
