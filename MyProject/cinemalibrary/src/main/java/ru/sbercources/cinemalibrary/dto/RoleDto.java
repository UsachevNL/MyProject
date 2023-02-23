package ru.sbercources.cinemalibrary.dto;

import lombok.*;
import ru.sbercources.cinemalibrary.model.Role;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoleDto {
    private Long id;
    private String title;
    private String description;
    public RoleDto (Role role) {
        this.id = role.getId();
        this.title = role.getTitle();
        this.description = role.getDescription();
    }
}


