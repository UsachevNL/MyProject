package ru.sbercources.cinemalibrary.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "directors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "directors_seq", allocationSize = 1)
public class Directors extends GenericModel {

    @Column(name = "directors_fio")
    private String directorFIO;
    @Column(name = "position")
    private String position;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(
            name = "films_directors",
            joinColumns = @JoinColumn(name = "directors_id"),
            foreignKey = @ForeignKey(name = "FK_DIRECTORS_FILMS"),
            inverseJoinColumns = @JoinColumn(name = "films_id"),
            inverseForeignKey = @ForeignKey(name = "FK_FILMS_DIRECTORS"))
    private Set<Films> films= new HashSet<>();

    @Builder
    public Directors(Long id, LocalDateTime createdWhen, String createdBy, LocalDateTime updateWhen, String updateBy, boolean isDeleted, LocalDateTime deletedWhen, String deletedBy, String directorFIO, String position, Set<Films> films) {
        super(id, createdWhen, createdBy, updateWhen, updateBy, isDeleted, deletedWhen, deletedBy);
        this.directorFIO = directorFIO;
        this.position = position;
        this.films = films;
    }

}
