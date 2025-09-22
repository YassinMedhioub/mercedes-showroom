package com.mercedes.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mercedes.backend.entity.User;

import java.time.Instant;


public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String full_name;
    private String phone;
    private String role;
    private boolean active;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Instant last_login;
    private Instant created_at;
    private Instant updated_at;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.full_name = user.getFull_name();
        this.phone = user.getPhone();
        this.role = user.getRole();
        this.active = user.isActive();

        this.last_login = user.getLast_login();
        this.updated_at = user.getUpdated_at();
    }

    public UserDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getLast_login() {
        return last_login;
    }

    public void setLast_login(Instant last_login) {
        this.last_login = last_login;
    }

    public Instant getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }

    public Instant getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Instant updated_at) {
        this.updated_at = updated_at;
    }
}