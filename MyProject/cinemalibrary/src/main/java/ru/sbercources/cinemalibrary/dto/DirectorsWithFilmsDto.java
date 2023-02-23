package ru.sbercources.cinemalibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DirectorsWithFilmsDto extends DirectorsDto {
    private Set<FilmsDto> films;

}
