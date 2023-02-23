package ru.sbercources.cinemalibrary.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.sbercources.cinemalibrary.dto.OrdersDto;
import ru.sbercources.cinemalibrary.dto.RentFilmsDto;
import ru.sbercources.cinemalibrary.mapper.GenericMapper;
import ru.sbercources.cinemalibrary.mapper.OrdersMapper;
import ru.sbercources.cinemalibrary.model.Orders;
import ru.sbercources.cinemalibrary.service.GenericService;
import ru.sbercources.cinemalibrary.service.OrdersService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rest/order")
public class OrdersController extends GenericController<Orders, OrdersDto> {

    private final OrdersService service;
    private final OrdersMapper mapper;

    private OrdersController(OrdersService service, OrdersMapper mapper) {
        super(service, mapper);
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping("rent-film")
    public OrdersDto rentFilm(@RequestBody RentFilmsDto rentFilmsDto) {
        return mapper.toDto(service.rentFilm(rentFilmsDto));
    }

    @GetMapping("user-order/{userId}")
    public List<OrdersDto> getUserOrder(@PathVariable Long userId) {
        return mapper.toDtos(service.getUserOrders(userId));
    }
}