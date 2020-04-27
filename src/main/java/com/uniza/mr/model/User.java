package com.uniza.mr.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "MRUSER")
public class User {

    @Id
    @NotNull(message="User id cannot be null.")
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;

    @Column(name = "NAME")
    @Size(min = 1, max = 255)
    @NotNull (message="User name cannot be null")
    private String name;

    @Column(name = "EMAIL", unique = true)
    @Size(min = 1, max = 255)
    @NotNull (message="User email cannot be null")
    private String email;

    @Column(name = "PASSWORD")
    @Size(min = 1, max = 255)
    @NotNull (message="User role cannot be null")
    private String password;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
