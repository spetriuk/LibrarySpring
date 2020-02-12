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
    private String name;
    private String authorName;
    private String requestDate;
    private Boolean approved;
}