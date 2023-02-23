package ru.sbercources.cinemalibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDto extends GenericDto{

    private LocalDateTime rentDate;
    private LocalDateTime returnDate;
    private boolean returned;
    private Integer rentPeriod;
    private boolean purchase;
    private Long filmsId;
    private Long userId;
    private FilmsDto film;
    private UserDto user;
}
