package com.training.dto;

import com.training.entity.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    @NotEmpty(message = "{message.empty}")
    @Pattern(regexp = "^[\\p{InCyrillic}\\s]+$", message = "{message.cyrillic}")
    private String nameUkr;
    @NotEmpty(message = "{message.empty}")
    private String nameEng;
    private Boolean available;
    private Long authorId;
    @NotEmpty(message = "{message.empty}")
    private List<Genre> genres;
}
