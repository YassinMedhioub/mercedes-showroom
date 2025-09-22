package com.mercedes.backend.controller;

import com.mercedes.backend.dto.UserDTO;
import com.mercedes.backend.entity.User;
import com.mercedes.backend.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> userPage = userService.getAllUsers(pageable, q);
        List<UserDTO> userDTOs = userPage.getContent().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new PageResponse(userDTOs, userPage.getTotalElements()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(new UserDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        User user = userService.createUser(convertToEntity(userDTO));
        return ResponseEntity.status(201).body(new UserDTO(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reset-password")
    public ResponseEntity<Void> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<UserDTO> toggleUser(@PathVariable Long id) {
        User user = userService.toggleActive(id);
        return ResponseEntity.ok(new UserDTO(user));
    }

    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setFull_name(userDTO.getFull_name());
        user.setPhone(userDTO.getPhone());
        user.setRole(userDTO.getRole());
        user.setActive(userDTO.isActive());
        user.setPassword(userDTO.getPassword());
        user.setLast_login(userDTO.getLast_login());
        user.setUpdated_at(userDTO.getUpdated_at());
        return user;
    }

    public static class PageResponse {
        private List<UserDTO> content;
        private long totalElements;

        public PageResponse(List<UserDTO> content, long totalElements) {
            this.content = content;
            this.totalElements = totalElements;
        }

        public List<UserDTO> getContent() {
            return content;
        }

        public long getTotalElements() {
            return totalElements;
        }
    }
}