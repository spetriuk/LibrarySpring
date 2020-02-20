package com.training.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

import static com.training.util.Constants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name_ukr")
    @NotEmpty
    @Pattern(regexp = CYRILLIC_REGEX, message = MESSAGE_CYRILLIC)
    private String nameUkr;

    @Column(name = "name_eng")
    @NotEmpty
    @Pattern(regexp = ENGLISH_REGEX, message = MESSAGE_ENGLISH)
    private String nameEng;
    @ToString.Exclude
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();
}