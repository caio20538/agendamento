package com.ms.userAuth.repository;

import com.ms.userAuth.model.RoleEntity;
import com.ms.userAuth.model.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByName(RoleEnum name);
}
