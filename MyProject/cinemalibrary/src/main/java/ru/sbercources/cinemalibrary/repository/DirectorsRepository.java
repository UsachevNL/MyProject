package ru.sbercources.cinemalibrary.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.sbercources.cinemalibrary.model.Directors;

import java.util.Set;

@Repository
public interface DirectorsRepository extends GenericRepository<Directors> {
    Set<Directors> findAllByIdIn(Set<Long> ids);
    Page<Directors> findAllByDirectorFIO(Pageable pageable, String directorFIO);
}
