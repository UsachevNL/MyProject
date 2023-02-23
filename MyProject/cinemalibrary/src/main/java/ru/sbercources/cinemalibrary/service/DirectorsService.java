package ru.sbercources.cinemalibrary.service;

import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.sbercources.cinemalibrary.dto.AddFilmsDto;
import ru.sbercources.cinemalibrary.model.Directors;
import ru.sbercources.cinemalibrary.model.Films;
import ru.sbercources.cinemalibrary.repository.DirectorsRepository;
import ru.sbercources.cinemalibrary.repository.FilmsRepository;
import ru.sbercources.cinemalibrary.repository.GenericRepository;
import ru.sbercources.cinemalibrary.repository.OrdersRepository;
import ru.sbercources.cinemalibrary.service.userDetails.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DirectorsService extends GenericService<Directors>{
    private final FilmsRepository filmsRepository;
    private final FilmsService filmsService;
    private final DirectorsRepository directorRepository;
    private final OrdersRepository ordersRepository;


    public DirectorsService(GenericRepository<Directors> repository, DirectorsRepository directorsRepository, FilmsRepository filmsRepository, OrdersRepository ordersRepository, FilmsService filmsService) {
        super(repository);
        this.directorRepository = directorsRepository;
        this.filmsRepository = filmsRepository;
        this.ordersRepository = ordersRepository;
        this.filmsService = filmsService;
    }

    public Page<Directors> searchByDirectorsFIO(Pageable pageable, String fio) {
        return directorRepository.findAllByDirectorFIO(pageable, fio);
    }
    public Page<Directors> listAllPaginated(Pageable pageable) {
        return directorRepository.findAll(pageable);
    }

    public Directors addFilms(AddFilmsDto addFilmsDto) {
        Directors directors = getOne(addFilmsDto.getDirectorsId());
        Films films = filmsService.getOne(addFilmsDto.getFilmId());
        directors.getFilms().add(films);
        return update(directors);
    }

    @Override
    public void delete(Long id) {
        List<Films> films = filmsRepository.findFilmsByDirectorsId(id);
        if(films.isEmpty()) {
            directorRepository.deleteById(id);
            return;
        }
        List<Long> filmIds = films.stream()
                .filter(i -> i.getDirectors().size() == 1)
                .map(Films::getId)
                .toList();

        if (filmIds.isEmpty()) {
            throw new OpenApiResourceNotFoundException("У данного участника нет фильма где он один.");
        }
        filmIds.stream()
                .filter(i -> {
                    if (ordersRepository.findOrdersByFilmsId(i).isEmpty()) {
                        return true;
                    } else {
                        throw new OpenApiResourceNotFoundException("Книга с данным автором в данный момент арендована");
                    }
                })
                .forEach(i -> {
                    filmsRepository.deleteById(i);
                    directorRepository.deleteById(id);
                });
    }
    public void block(Long id) {
        Directors director = getOne(id);
        director.setDeleted(true);
        director.setDeletedBy(((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        director.setDeletedWhen(LocalDateTime.now());
        update(director);
    }

    public void unblock(Long id) {
        Directors director = getOne(id);
        director.setDeleted(false);
        director.setDeletedBy(null);
        director.setDeletedWhen(null);
        director.setUpdateBy(((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        director.setUpdateWhen(LocalDateTime.now());
        update(director);
    }
}

