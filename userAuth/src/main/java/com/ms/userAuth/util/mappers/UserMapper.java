package com.ms.userAuth.util.mappers;

import com.ms.userAuth.controller.dto.CreateUserDTO;
import com.ms.userAuth.model.RoleEntity;
import com.ms.userAuth.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.Set;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "userDTO.email", target = "email")
    @Mapping(source = "userDTO.password", target = "password", qualifiedByName = "passwordEncoder")
    @Mapping(source = "role", target = "roles", qualifiedByName = "toRoleSet")
    UserEntity dtoToEntity(CreateUserDTO userDTO, RoleEntity role);

    @Named("passwordEncoder")
    default String passwordEncoder(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @Named("toRoleSet")
    default Set<RoleEntity> toRoleSet(RoleEntity role) {
        return Collections.singleton(role);
    }



}
