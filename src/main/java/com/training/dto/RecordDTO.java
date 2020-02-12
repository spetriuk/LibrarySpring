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
    private String name;
    private String authorName;
    private String reader;
    private String takeDate;
    private String returnDate;
}
