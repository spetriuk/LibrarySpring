package com.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRequestDTO {
    private Long id;
    private String userEmail;
    private String nameUkr;
    private String nameEng;
    private String authorNameUkr;
    private String authorNameEng;
    private String requestDate;
    private Boolean approved;
}