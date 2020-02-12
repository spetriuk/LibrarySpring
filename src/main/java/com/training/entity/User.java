package com.training.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", uniqueConstraints={@UniqueConstraint(columnNames={"email", "phone"})})
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Email(message = "{message.email}")
    @NotEmpty(message = "{message.empty}")
    @Column(name="email",unique=true, nullable = false)
    private String email;

    @NotEmpty(message = "{message.empty}")
    @Column(name="name_ukr",unique=true, nullable = false)
    private String nameUkr;

    @NotEmpty(message = "{message.empty}")
    @Column(name="name_eng", nullable = false)
    private String nameEng;

    @NotEmpty(message = "{message.empty}")
    @Pattern(regexp = "^\\+?3?8?(0\\d{9})$", message = "{messages.phone}")
    @Column(name="phone", nullable = false)
    private String phone;

    @NotEmpty(message = "{message.empty}")
    @Column(name="password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "reader")
    private List<Book> booksList;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
}
