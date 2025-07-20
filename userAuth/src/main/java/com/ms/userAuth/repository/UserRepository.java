package com.ms.userAuth.repository;

import com.ms.userAuth.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    @Modifying
    @Query("DELETE FROM UserEntity where email = :email")
    int deleteByEmail(@Param("email") String email);

    @Modifying
    @Query("UPDATE UserEntity u SET u.email = :email, u.password = :password WHERE u.id = :id")
    int updateUserInfo(@Param("id") UUID id, @Param("email") String email, @Param("password") String password);

    @Modifying
    @Query("UPDATE UserEntity u SET u.email = :newEmail, u.password = :password WHERE u.email = :oldEmail")
    int updateUserInfoByEmail(@Param("oldEmail") String oldEmail, @Param("newEmail") String newEmail, @Param("password") String password);
}
