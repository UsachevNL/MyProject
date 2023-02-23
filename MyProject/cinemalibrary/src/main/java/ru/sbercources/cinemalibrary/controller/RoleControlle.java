package ru.sbercources.cinemalibrary.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sbercources.cinemalibrary.model.Role;
import ru.sbercources.cinemalibrary.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleControlle {
    private final RoleService service;

    public RoleControlle(RoleService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public List<Role> list() {
        return service.getList();
    }
}

