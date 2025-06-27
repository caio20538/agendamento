package com.ms.userAuth.config;

import com.ms.userAuth.model.RoleEntity;
import com.ms.userAuth.model.UserEntity;
import com.ms.userAuth.model.enums.RoleEnum;
import com.ms.userAuth.repository.RoleRepository;
import com.ms.userAuth.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Configuration
public class AdminConfig implements CommandLineRunner {
    private static final Logger logger = Logger.getLogger(AdminConfig.class.getName());

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminConfig(RoleRepository repository,
                       UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = repository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Garante que a role ADMIN exista, usando Optional e lambda
        RoleEntity roleAdmin = Optional.ofNullable(roleRepository.findByName(RoleEnum.ADMIN))
                .orElseGet(() -> {
                    logger.warning("Role ADMIN não encontrada. Criando...");
                    RoleEntity newRole = new RoleEntity();
                    newRole.setName(RoleEnum.ADMIN);
                    return roleRepository.save(newRole);
                });

        // Cria o usuário admin se não existir, com lambda
        userRepository.findByEmail("admin@gmail.com")
                .ifPresentOrElse(
                        user -> logger.info("Usuário ADMIN já existe."),
                        () -> {
                            UserEntity adminUser = new UserEntity();
                            adminUser.setEmail("admin@gmail.com");
                            adminUser.setPassword(passwordEncoder.encode("123"));
                            adminUser.setRoles(Set.of(roleAdmin));
                            userRepository.save(adminUser);
                            logger.info("Usuário ADMIN criado com sucesso.");
                        }
                );
    }
}
