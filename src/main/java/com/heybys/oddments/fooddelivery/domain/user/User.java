package com.heybys.oddments.fooddelivery.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.JavaType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.heybys.oddments.base.domain.AggregateRoot;
import com.heybys.oddments.fooddelivery.domain.generic.AuthProvider;
import com.heybys.oddments.fooddelivery.domain.user.UserId.UserIdJavaType;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"JpaAttributeTypeInspection", "java:S1710"})
@Getter
@Setter
@Entity
@Table(
        name = "user",
        uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User extends AggregateRoot<User, UserId> {

    @Id
    @JavaType(UserIdJavaType.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UserId id;

    @Column(nullable = false)
    private String name;

    @JsonIgnore
    private String password;

    @Email
    @Column(nullable = false)
    private String email;

    private String imageUrl;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private Boolean emailVerified = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    public User() {}

    public User(
            String name,
            String password,
            String email,
            String imageUrl,
            Boolean emailVerified,
            AuthProvider provider,
            String providerId) {
        this(null, name, password, email, imageUrl, emailVerified, provider, providerId);
    }

    public User(
            UserId id,
            String name,
            String password,
            String email,
            String imageUrl,
            Boolean emailVerified,
            AuthProvider provider,
            String providerId) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.imageUrl = imageUrl;
        this.emailVerified = emailVerified;
        this.provider = provider;
        this.providerId = providerId;
    }

    @Override
    public final boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }
}
