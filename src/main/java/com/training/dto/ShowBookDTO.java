package com.training.dto;

import com.training.entity.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowBookDTO {
    private Long id;
    private String nameUkr;
    private String nameEng;
    private String authorNameUkr;
    private String authorNameEng;
    private String expDate;
    private Boolean available;
    private List<Genre> genres;
}
