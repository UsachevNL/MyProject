package ru.sbercources.cinemalibrary.repository;

import org.springframework.stereotype.Repository;
import ru.sbercources.cinemalibrary.model.Films;
import ru.sbercources.cinemalibrary.model.Genre;

import java.util.List;
import java.util.Set;
@Repository
public interface FilmsRepository extends GenericRepository<Films> {
    List<Films> findAllByGenreOrTitle(Genre genre, String title);

    Set<Films> findAllByIdIn(Set<Long> ids);
    List<Films> findAllByTitle(String title);
    List<Films> findFilmsByDirectorsId(Long id);
}
