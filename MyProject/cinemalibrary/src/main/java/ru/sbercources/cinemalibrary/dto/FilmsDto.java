package ru.sbercources.cinemalibrary.dto;

import lombok.*;
import ru.sbercources.cinemalibrary.model.Genre;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmsDto extends GenericDto{

    private String title;
    private String premierYear;
    private String country;
    private Genre genre;
    private Set<Long> directorsIds;
}
