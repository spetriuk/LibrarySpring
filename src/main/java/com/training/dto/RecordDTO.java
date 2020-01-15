package com.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecordDTO {
    private String nameUkr;
    private String nameEng;
    private String authorNameUkr;
    private String authorNameEng;
    private String reader;
    private String takeDate;
    private String returnDate;
}
