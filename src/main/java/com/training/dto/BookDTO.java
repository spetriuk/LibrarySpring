package com.training.dto;

import com.training.entity.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

import static com.training.util.Constants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    @NotEmpty(message = MESSAGE_EMPTY)
    @Pattern(regexp = CYRILLIC_REGEX, message = MESSAGE_CYRILLIC)
    private String nameUkr;
    @NotEmpty(message = MESSAGE_EMPTY)
    @Pattern(regexp = ENGLISH_REGEX, message = MESSAGE_ENGLISH)
    private String nameEng;
    private Boolean available;
    private Long authorId;
    @NotEmpty(message = MESSAGE_EMPTY)
    private List<Genre> genres;
}
