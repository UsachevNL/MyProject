package ru.sbercources.cinemalibrary.service;

import org.springframework.stereotype.Service;
import ru.sbercources.cinemalibrary.model.Role;
import ru.sbercources.cinemalibrary.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService  {
    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public List<Role> getList() {
        return repository.findAll();
    }

    public Role getOne(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
