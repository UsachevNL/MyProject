package ru.sbercources.cinemalibrary.MVC.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sbercources.cinemalibrary.dto.AddFilmsDto;
import ru.sbercources.cinemalibrary.dto.DirectorsDto;
import ru.sbercources.cinemalibrary.mapper.DirectorsMapper;
import ru.sbercources.cinemalibrary.mapper.DirectorsWithFilmsMapper;
import ru.sbercources.cinemalibrary.mapper.FilmsMapper;
import ru.sbercources.cinemalibrary.model.Directors;
import ru.sbercources.cinemalibrary.service.DirectorsService;
import ru.sbercources.cinemalibrary.service.FilmsService;

import javax.validation.Valid;
import java.util.List;
@Controller
@Slf4j
@RequestMapping("/directors")
public class MVCDirectorsController {
    private final FilmsService filmsService;
    private final FilmsMapper filmsMapper;
    private final DirectorsWithFilmsMapper directorsWithFilmsMapper;
    private final DirectorsService service;
    private final DirectorsMapper mapper;

    public MVCDirectorsController(FilmsService filmsService, FilmsMapper filmsMapper, DirectorsWithFilmsMapper directorsWithFilmsMapper, DirectorsService service, DirectorsMapper mapper) {
        this.filmsService = filmsService;
        this.filmsMapper = filmsMapper;
        this.directorsWithFilmsMapper = directorsWithFilmsMapper;
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String getAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int pageSize,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "directorFIO"));
        Page<Directors> directorsPage = service.listAllPaginated(pageRequest);
        List<DirectorsDto> DirectorsDtos = directorsPage
                .stream()
                .map(mapper::toDto)
                .toList();
        model.addAttribute("directors", new PageImpl<>(DirectorsDtos, pageRequest, directorsPage.getTotalElements()));
        return "directors/viewAllDirector";
    }

    @GetMapping("/add")
    public String create(@ModelAttribute("directorForm") DirectorsDto directorsDto) {
        return "/directors/addDirector";
    }

    @PostMapping("/add")
    public String create(
            @ModelAttribute("directorForm") @Valid DirectorsDto directorsDto, BindingResult result) {
        if (result.hasErrors()) {
            return "/directors/addDirector";
        }
        else {
            service.create(mapper.toEntity(directorsDto));
            return "redirect:/directors";
        }

    }
    @GetMapping("/block/{id}")
    public String block(@PathVariable Long id) {
        service.block(id);
        return "redirect:/films";
    }

    @GetMapping("/unblock/{id}")
    public String unblock(@PathVariable Long id) {
        service.unblock(id);
        return "redirect:/films";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("director", mapper.toDto(service.getOne(id)));
        return "directors/updateDirector";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("directorForm") DirectorsDto DirectorsDto) {
        service.update(mapper.toEntity(DirectorsDto));
        return "redirect:/directors";
    }

    @PostMapping("/search")
    public String searchByDirectorsFIO(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int pageSize,
            Model model, @ModelAttribute("searchDirectors") DirectorsDto directorsDto
    ) {
        if (directorsDto.getDirectorFIO().trim().equals("")) {
            model.addAttribute("directors", mapper.toDtos(service.listAll()));
        } else {
            PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "directorFIO"));
            Page<Directors> directorPage = service.searchByDirectorsFIO(pageRequest, directorsDto.getDirectorFIO());
            List<DirectorsDto> directorDtos = directorPage
                    .stream()
                    .map(mapper::toDto)
                    .toList();
            model.addAttribute("directors", new PageImpl<>(directorDtos, pageRequest, directorPage.getTotalElements()));
        }
        return "directors/viewAllDirector";
    }


    @GetMapping("/add-film/{directorsId}")
    public String addFilms(Model model, @PathVariable Long directorsId) {
        model.addAttribute("films", filmsMapper.toDtos(filmsService.listAll()));
        model.addAttribute("directorsId", directorsId);
        model.addAttribute("director", mapper.toDto(service.getOne(directorsId)).getDirectorFIO());
        return "directors/addDirectorFilm";
    }

    @PostMapping("/add-film")
    public String addFilms(@ModelAttribute("directorFilmForm") AddFilmsDto addFilmsDto) {
        service.addFilms(addFilmsDto);
        return "redirect:/directors";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable Long id, Model model) {
        model.addAttribute("director", directorsWithFilmsMapper.toDto(service.getOne(id)));
        return "directors/viewDirector";
    }

}


