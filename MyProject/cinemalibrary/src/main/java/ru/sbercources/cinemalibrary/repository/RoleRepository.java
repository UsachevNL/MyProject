package ru.sbercources.cinemalibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sbercources.cinemalibrary.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
