package com.Api.Entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table(name = "users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private  String username;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
      name = "user_roles" ,
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> userRoles = new HashSet<>();
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST,orphanRemoval = true)
    private UserProfile profile;

    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public UserProfile getProfile() {
        return this.profile;
    }

    public void setProfile(final UserProfile profile) {
        this.profile = profile;
    }

    public Set<Role> getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(final Set<Role> userRoles) {
        this.userRoles = userRoles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userRoles=" + userRoles +
                ", profile=" + profile +
                '}';
    }
}
