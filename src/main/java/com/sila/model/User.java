package com.sila.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sila.util.enums.ROLE;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString(exclude = {"favourites", "addresses", "cart"}) // exclude fields that can loop
@EntityListeners(AuditingEntityListener.class) // 👈 required for auditing
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String profile;
    private String email;
    private String password;
    private ROLE role = ROLE.USER;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "owner")
    private List<Favorite> favourites = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<Address> addresses = new ArrayList<>();
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @CreationTimestamp // 👈 auto-set on insert
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp // 👈 auto-set on update
    private LocalDateTime updatedAt = LocalDateTime.now();
}
