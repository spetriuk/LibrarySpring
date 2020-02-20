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

import static com.training.util.Constants.*;

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

    @Email(message = MESSAGE_EMAIL)
    @NotEmpty(message = MESSAGE_EMPTY)
    @Column(name="email",unique=true, nullable = false)
    private String email;

    @NotEmpty(message = MESSAGE_EMPTY)
    @Column(name="name_ukr",unique=true, nullable = false)
    private String nameUkr;

    @NotEmpty(message = MESSAGE_EMPTY)
    @Column(name="name_eng", nullable = false)
    private String nameEng;

    @NotEmpty(message = MESSAGE_EMPTY)
    @Pattern(regexp = REGEX_PHONE, message = MESSAGE_PHONE)
    @Column(name="phone", nullable = false)
    private String phone;

    @NotEmpty(message = MESSAGE_EMPTY)
    @Column(name="password", nullable = false)
    @Pattern(regexp = REGEX_PASSWORD, message = MESSAGE_PASSWORD)
    private String password;

    @OneToMany(mappedBy = "reader")
    private List<Book> booksList;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
}
