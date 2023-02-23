package ru.sbercources.cinemalibrary.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.sbercources.cinemalibrary.dto.FilmsDto;
import ru.sbercources.cinemalibrary.mapper.FilmsMapper;
import ru.sbercources.cinemalibrary.mapper.GenericMapper;
import ru.sbercources.cinemalibrary.model.Films;
import ru.sbercources.cinemalibrary.model.Genre;
import ru.sbercources.cinemalibrary.service.FilmsService;
import ru.sbercources.cinemalibrary.service.GenericService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rest/film")
public class FilmsController extends GenericController<Films, FilmsDto> {
    private final FilmsService service;
    private final FilmsMapper mapper;

    public FilmsController(FilmsService service,  FilmsMapper mapper) {
        super(service, mapper);
        this.service = service;
        this.mapper = mapper;
    }


    @PostMapping("/search")
    public List<FilmsDto> search(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "genre", required = false) Genre genre
    ) {
        return mapper.toDtos(service.search(title, genre));
    }

}

