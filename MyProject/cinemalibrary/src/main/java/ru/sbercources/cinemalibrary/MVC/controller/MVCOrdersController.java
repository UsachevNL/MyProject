package ru.sbercources.cinemalibrary.MVC.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sbercources.cinemalibrary.dto.OrdersDto;
import ru.sbercources.cinemalibrary.dto.RentFilmsDto;
import ru.sbercources.cinemalibrary.mapper.FilmsMapper;
import ru.sbercources.cinemalibrary.mapper.OrdersMapper;
import ru.sbercources.cinemalibrary.model.Orders;
import ru.sbercources.cinemalibrary.service.FilmsService;
import ru.sbercources.cinemalibrary.service.OrdersService;
import ru.sbercources.cinemalibrary.service.userDetails.CustomUserDetails;

import java.util.List;

@Controller
@RequestMapping("/order")
public class MVCOrdersController {
    private final FilmsService filmsService;
    private final FilmsMapper filmsMapper;
    private final OrdersMapper mapper;
    private final OrdersService service;

    public MVCOrdersController(FilmsService filmsService, FilmsMapper filmsMapper, OrdersMapper mapper, OrdersService service) {
        this.filmsService = filmsService;
        this.filmsMapper = filmsMapper;
        this.mapper = mapper;
        this.service = service;
    }
    @GetMapping("/get-film/{filmId}")
    public String getFilm(@PathVariable Long filmId, Model model) {
        model.addAttribute("film", filmsMapper.toDto(filmsService.getOne(filmId)));
        return "userFilms/getFilm";
    }

    @PostMapping("/get-film")
    public String getFilm(@ModelAttribute("ordersForm") RentFilmsDto rentFilmsDto) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        rentFilmsDto.setUserId(Long.valueOf(customUserDetails.getUserId()));
        service.rentFilm(rentFilmsDto);
        return "redirect:/films";
    }

    @GetMapping("/user-films/{id}")
    public String userFilms(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int pageSize,
            @PathVariable Long id,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<Orders> directorPage = service.listAllPaginated(pageRequest);
        List<OrdersDto> ordersDtos = directorPage
                .stream()
                .map(mapper::toDto)
                .toList();
        model.addAttribute("orderss", new PageImpl<>(ordersDtos, pageRequest, directorPage.getTotalElements()));
        return "userFilms/viewAllUserFilms";
    }

    @GetMapping("/return-film/{id}")
    public String returnFilm(@PathVariable Long id) {
        service.returnFilm(id);
        return "redirect:/order/user-films/" + service.getOne(id).getUser().getId();
    }
}
