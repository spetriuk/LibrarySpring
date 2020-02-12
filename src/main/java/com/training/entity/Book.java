package com.training.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "name_ukr", nullable = false)
    private String nameUkr;
    @Column(name = "name_eng", nullable = false)
    private String nameEng;
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
    @Column(name = "available", nullable = false)
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User reader;
    @Column(name = "expDate")
    private LocalDateTime expDate;
    @Column(name = "genre", nullable = false)
    @ElementCollection
    private List<Genre> genres;
    @Version
    private Long version;
}
