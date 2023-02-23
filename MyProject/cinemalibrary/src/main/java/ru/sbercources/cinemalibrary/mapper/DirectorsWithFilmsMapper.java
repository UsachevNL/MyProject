package ru.sbercources.cinemalibrary.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sbercources.cinemalibrary.dto.DirectorsWithFilmsDto;
import ru.sbercources.cinemalibrary.model.Directors;
import ru.sbercources.cinemalibrary.model.GenericModel;
import ru.sbercources.cinemalibrary.repository.FilmsRepository;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
@Component
public class DirectorsWithFilmsMapper extends GenericMapper<Directors, DirectorsWithFilmsDto>{

    private final ModelMapper mapper;
    private final FilmsRepository filmsRepository;

    private DirectorsWithFilmsMapper(ModelMapper mapper, FilmsRepository filmsRepository) {
        super(mapper, Directors.class, DirectorsWithFilmsDto.class);
        this.mapper = mapper;
        this.filmsRepository =filmsRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Directors.class, DirectorsWithFilmsDto.class)
                .addMappings(m -> m.skip(DirectorsWithFilmsDto::setFilmsIds)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(DirectorsWithFilmsDto.class,Directors.class)
                .addMappings(m -> m.skip(Directors::setFilms)).setPostConverter((toEntityConverter()));
    }

    @Override
    void mapSpecificFields(DirectorsWithFilmsDto source, Directors destination) {
        destination.setFilms(filmsRepository.findAllByIdIn(source.getFilmsIds()));
    }

    @Override
    void mapSpecificFields(Directors source, DirectorsWithFilmsDto destination) {
        destination.setFilmsIds(getIds(source));
    }

    private Set<Long> getIds(Directors directors) {
        return Objects.isNull(directors) || Objects.isNull(directors.getId())
                ? null
                : directors.getFilms().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
}
