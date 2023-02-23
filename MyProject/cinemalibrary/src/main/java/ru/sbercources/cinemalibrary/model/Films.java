package ru.sbercources.cinemalibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "films")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "films_seq", allocationSize = 1)
public class Films extends GenericModel {

    @Column(name = "title")

    private String title;
    @Column(name = "premier_year")

    private String premierYear;
    @Column(name = "country")

    private String country;
    @Column(name = "genre")
    @Enumerated
    private Genre genre;
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(
            name = "films_directors",
            joinColumns = @JoinColumn(name = "films_id"),
            foreignKey = @ForeignKey(name = "FK_FILMS_DIRECTORS"),
            inverseJoinColumns = @JoinColumn(name = "directors_id"),
            inverseForeignKey = @ForeignKey(name = "FK_DIRECTORS_FILMS"))
    private Set<Directors> directors = new HashSet<>();

    @Builder
    public Films(Long id, LocalDateTime createdWhen, String createdBy, LocalDateTime updateWhen, String updateBy, boolean isDeleted, LocalDateTime deletedWhen, String deletedBy, String title, String premierYear, String country, Genre genre, Set<Directors> directors) {
        super(id, createdWhen, createdBy, updateWhen, updateBy, isDeleted, deletedWhen, deletedBy);
        this.title = title;
        this.premierYear = premierYear;
        this.country = country;
        this.genre = genre;
        this.directors = directors;
    }
}
