package com.training.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user", uniqueConstraints={@UniqueConstraint(columnNames={"email", "phone"})})
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Email
    @NotEmpty
    @Column(name="email",unique=true, nullable = false)
    private String email;

    @NotEmpty
    @Column(name="name_ukr",unique=true, nullable = false)
    private String nameUkr;

    @NotEmpty
    @Column(name="name_eng", nullable = false)
    private String nameEng;

    @NotEmpty
    @Column(name="phone", nullable = false)
    private String phone;

    @NotEmpty
    @Column(name="password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "reader")
    private List<Book> booksList;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
}
