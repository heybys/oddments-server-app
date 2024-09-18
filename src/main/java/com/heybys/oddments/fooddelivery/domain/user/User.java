package com.heybys.oddments.fooddelivery.domain.user;

import com.heybys.oddments.base.domain.AggregateRoot;
import com.heybys.oddments.fooddelivery.domain.generic.Address;
import com.heybys.oddments.fooddelivery.domain.generic.Contact;
import com.heybys.oddments.fooddelivery.domain.user.UserId.UserIdJavaType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.JavaType;

@Getter
@Entity
@Table(name = "user")
public class User extends AggregateRoot<User, UserId> {

    @Id
    @JavaType(UserIdJavaType.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UserId id;

    @Column
    private String username;

    @Embedded
    private Contact contact;

    @AttributeOverrides({
        @AttributeOverride(name = "city", column = @Column(name = "home_city")),
        @AttributeOverride(name = "street", column = @Column(name = "home_street")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "home_zip_code")),
    })
    @Embedded
    private Address homeAddress;

    @AttributeOverrides({
        @AttributeOverride(name = "city", column = @Column(name = "work_city")),
        @AttributeOverride(name = "street", column = @Column(name = "work_street")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "work_zip_code")),
    })
    @Embedded
    private Address workAddress;

    public User() {}

    @Builder
    public User(String username, Contact contact, Address homeAddress, Address workAddress) {
        this(null, username, contact, homeAddress, workAddress);
    }

    @Builder
    public User(UserId id, String username, Contact contact, Address homeAddress, Address workAddress) {
        this.id = id;
        this.username = username;
        this.contact = contact;
        this.homeAddress = homeAddress;
        this.workAddress = workAddress;
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
