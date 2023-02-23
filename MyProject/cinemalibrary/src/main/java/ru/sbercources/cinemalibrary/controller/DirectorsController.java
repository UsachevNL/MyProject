package ru.sbercources.cinemalibrary.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.sbercources.cinemalibrary.dto.AddFilmsDto;
import ru.sbercources.cinemalibrary.dto.DirectorsDto;
import ru.sbercources.cinemalibrary.dto.DirectorsWithFilmsDto;
import ru.sbercources.cinemalibrary.mapper.DirectorsMapper;
import ru.sbercources.cinemalibrary.mapper.DirectorsWithFilmsMapper;
import ru.sbercources.cinemalibrary.model.Directors;
import ru.sbercources.cinemalibrary.service.DirectorsService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rest/director")
public class DirectorsController extends GenericController<Directors, DirectorsDto> {
    private final DirectorsService service;
    private final DirectorsWithFilmsMapper directorsWithFilmsMapper;
    public DirectorsController(DirectorsService service, DirectorsMapper mapper, DirectorsWithFilmsMapper directorsWithFilmsMapper) {
        super(service, mapper);
        this.service = service;
        this.directorsWithFilmsMapper = directorsWithFilmsMapper;
    }
    @GetMapping("/director-films")
    public List<DirectorsWithFilmsDto> getDirectorsWithFilms() {
        return service.listAll().stream().map(directorsWithFilmsMapper::toDto).toList();
    }
    @PostMapping("/add-film")
    public DirectorsDto addFilm(@RequestBody AddFilmsDto addFilmsDto) {
        return directorsWithFilmsMapper.toDto(service.addFilms(addFilmsDto));
    }
}


