package com.antony.petclinic.repository;

import java.util.Optional;

import com.antony.petclinic.models.ERole;
import com.antony.petclinic.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
