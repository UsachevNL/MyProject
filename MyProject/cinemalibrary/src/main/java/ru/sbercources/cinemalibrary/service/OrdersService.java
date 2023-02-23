package ru.sbercources.cinemalibrary.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.sbercources.cinemalibrary.dto.RentFilmsDto;
import ru.sbercources.cinemalibrary.model.Films;
import ru.sbercources.cinemalibrary.model.Orders;
import ru.sbercources.cinemalibrary.model.User;
import ru.sbercources.cinemalibrary.repository.GenericRepository;
import ru.sbercources.cinemalibrary.repository.OrdersRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdersService extends GenericService<Orders> {
    private final OrdersRepository repository;
    private final UserService userService;
    private final FilmsService filmsService;

    public OrdersService( OrdersRepository repository, UserService userService, FilmsService bookService) {
        super(repository);
        this.repository = repository;
        this.userService = userService;
        this.filmsService = bookService;
    }

    public Page<Orders> listAllPaginated(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Orders rentFilm(RentFilmsDto rentFilmsDto) {
        User user = userService.getOne(rentFilmsDto.getUserId());//?????
        Films film = filmsService.getOne(rentFilmsDto.getFilmsId());//?????

        Orders orders = Orders.builder()
                .rentDate(LocalDateTime.now())
                .returned(false)
                .returnDate(LocalDateTime.now().plusMonths(rentFilmsDto.getRentPeriod()))
                .rentPeriod(rentFilmsDto.getRentPeriod())
                .user(user)
                .films(film)
                .build();
        return repository.save(orders);

    }

    public List<Orders> getUserOrders(Long userId) {
        return (List<Orders>) userService.getOne(userId).getOrders();
    }

    public void returnFilm(Long id) {
        Orders orders = getOne(id);
        orders.setReturned(true);
        orders.setReturnDate(LocalDateTime.now());
        Films film = orders.getFilms();
        update(orders);
        filmsService.update(film);
    }
}