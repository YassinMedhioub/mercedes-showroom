package com.mercedes.backend.service;

import com.mercedes.backend.entity.User;
import com.mercedes.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class PasswordMigrationService {

    @Autowired
    private UserRepository userRepository;

    public void migratePasswordsToBCrypt() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        List<User> users = userRepository.findAll();
        for (User user : users) {
            String plain = user.getPassword();
            // Check if already BCrypt
            if (!plain.startsWith("$2a$")) {
                String hashed = encoder.encode(plain);
                user.setPassword(hashed);
                userRepository.save(user);
            }
        }
        System.out.println("All passwords migrated to BCrypt!");
    }
}

