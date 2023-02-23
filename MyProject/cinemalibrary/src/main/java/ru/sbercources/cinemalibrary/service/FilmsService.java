package ru.sbercources.cinemalibrary.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.sbercources.cinemalibrary.model.Films;
import ru.sbercources.cinemalibrary.model.Genre;
import ru.sbercources.cinemalibrary.repository.FilmsRepository;
import ru.sbercources.cinemalibrary.service.userDetails.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class FilmsService extends GenericService<Films> {
    private final FilmsRepository repository;
    public FilmsService(FilmsRepository repository) {
        super(repository);
        this.repository = repository;
    }
    public List<Films> search(String title, Genre genre ){
        return  repository.findAllByGenreOrTitle(
                genre,
                title
        );
    }
    public List<Films> searchByTitle(String title) {
        return repository.findAllByTitle(title);
    }

    public Page<Films> listAllPaginated(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void block(Long id) {
        Films film = getOne(id);
        film.setDeleted(true);
        film.setDeletedBy(((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        film.setDeletedWhen(LocalDateTime.now());
        update(film);
    }

    public void unblock(Long id) {
        Films film = getOne(id);
        film.setDeleted(false);
        film.setDeletedBy(null);
        film.setDeletedWhen(null);
        film.setUpdateBy(((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        film.setUpdateWhen(LocalDateTime.now());
        update(film);
    }
}

