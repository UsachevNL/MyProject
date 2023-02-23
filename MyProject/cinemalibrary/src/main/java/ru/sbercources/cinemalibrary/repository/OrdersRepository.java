package ru.sbercources.cinemalibrary.repository;

import org.springframework.stereotype.Repository;
import ru.sbercources.cinemalibrary.model.Orders;

import java.util.List;

@Repository
public interface OrdersRepository extends GenericRepository<Orders> {
    List<Orders> findOrdersByFilmsId(Long filmId);
}
