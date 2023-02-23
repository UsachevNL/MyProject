package ru.sbercources.cinemalibrary.dto;

import lombok.Data;

@Data
public class RentFilmsDto {
    Long filmsId;
    Long userId;
    Integer rentPeriod;
}
