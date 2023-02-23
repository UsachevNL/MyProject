package ru.sbercources.cinemalibrary.MVC.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sbercources.cinemalibrary.dto.FilmsDto;
import ru.sbercources.cinemalibrary.dto.FilmsWithDirectorsDto;
import ru.sbercources.cinemalibrary.mapper.FilmsMapper;
import ru.sbercources.cinemalibrary.mapper.FilmsWithDirectorsMapper;
import ru.sbercources.cinemalibrary.model.Films;
import ru.sbercources.cinemalibrary.service.FilmsService;

import java.util.List;

@Hidden
@Controller
@RequestMapping("films")
public class MVCFilmsController {
    private final FilmsService service;
    private final FilmsWithDirectorsMapper filmsWithDirectorsMapper;
    private final FilmsMapper mapper;

    public MVCFilmsController(FilmsService service, FilmsWithDirectorsMapper filmsWithDirectorsMapper, FilmsMapper mapper) {
        this.service = service;
        this.filmsWithDirectorsMapper = filmsWithDirectorsMapper;
        this.mapper = mapper;
    }
    @GetMapping("")
    public String getAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int pageSize,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "title"));
        Page<Films> directorPage = service.listAllPaginated(pageRequest);
        List<FilmsWithDirectorsDto> filmsDtos = directorPage
                .stream()
                .map(filmsWithDirectorsMapper::toDto)
                .toList();
        model.addAttribute("films", new PageImpl<>(filmsDtos, pageRequest, directorPage.getTotalElements()));
        return "films/viewAllFilms";
    }

    @GetMapping("/add")
    public String create() {
        return "films/addFilm";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("filmForm") FilmsDto filmsDto) {
        service.create(mapper.toEntity(filmsDto));
        return "redirect:/films";
    }
    @GetMapping("/block/{id}")
    public String block(@PathVariable Long id) {
        service.block(id);
        return "redirect:/directors";
    }

    @GetMapping("/unblock/{id}")
    public String unblock(@PathVariable Long id) {
        service.unblock(id);
        return "redirect:/directors";
    }


    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("film", filmsWithDirectorsMapper.toDto(service.getOne(id)));
        return "films/updateFilm";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("directorForm")FilmsDto filmsDto) {
        service.update(mapper.toEntity(filmsDto));
        return "redirect:/films";
    }

    @PostMapping("/search")
    public String searchByDirectorFIO(Model model, @ModelAttribute("searchDirectors") FilmsDto filmsDto) {
        if (filmsDto.getTitle().trim().equals("")) {
            model.addAttribute("films", filmsWithDirectorsMapper.toDtos(service.listAll()));
        } else {
            model.addAttribute("films", filmsWithDirectorsMapper.toDtos(service.searchByTitle(filmsDto.getTitle())));
        }
        return "films/viewAllFilms";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable Long id, Model model) {
        model.addAttribute("film", filmsWithDirectorsMapper.toDto(service.getOne(id)));
        return "films/viewFilm";
    }
}
