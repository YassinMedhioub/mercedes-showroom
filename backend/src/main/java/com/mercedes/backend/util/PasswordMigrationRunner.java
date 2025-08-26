package com.mercedes.backend.util;

import com.mercedes.backend.entity.User;
import com.mercedes.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PasswordMigrationRunner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordMigrationRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (!user.getPassword().startsWith("$2a$")) { // if not already BCrypt
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
                System.out.println("✅ Migrated password for user: " + user.getUsername());
            }
        }
    }
}
