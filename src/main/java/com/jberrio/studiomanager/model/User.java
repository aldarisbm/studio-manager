package com.jberrio.studiomanager.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private int id;

    @NotNull(message = "Please provide your name")
    @Size(min = 2, max = 12)
    private String name;

    @NotNull(message = "Please provide your last name")
    @Size(min = 2, max = 12)
    private String lastName;

    @Column(nullable = false, unique = true)
    @Email
    @NotNull(message = "Please provide your email")
    private String email;

    @NotEmpty
    @Length(min = 6, message = "Your password must have at least 6 characters")
    private String password;

    @NotNull
    @Column(name = "color")
    private String color;

    @Column(name = "active")
    private int active;

    @Column(name = "is_instructor")
    private int isInstructor;

    @Column(columnDefinition = "LONGTEXT", name = "bio")
    private String biography;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany
    @JoinColumn(name = "event_id")
    private List<Event> events = new ArrayList<>();


    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getIsInstructor() {
        return isInstructor;
    }

    public void setIsInstructor(int isInstructor) {
        this.isInstructor = isInstructor;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
