package ru.sbercources.cinemalibrary.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sbercources.cinemalibrary.dto.DirectorsDto;
import ru.sbercources.cinemalibrary.model.Directors;
import ru.sbercources.cinemalibrary.model.GenericModel;
import ru.sbercources.cinemalibrary.repository.FilmsRepository;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DirectorsMapper extends GenericMapper<Directors, DirectorsDto> {
    private final ModelMapper mapper;
    private final FilmsRepository filmsRepository;
    private DirectorsMapper(ModelMapper mapper,FilmsRepository filmsRepository) {
        super(mapper, Directors.class, DirectorsDto.class);
        this.mapper = mapper;
        this.filmsRepository = filmsRepository;

    }
    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Directors.class, DirectorsDto.class)
                .addMappings(m -> m.skip(DirectorsDto::setFilmsIds)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(DirectorsDto.class, Directors.class)
                .addMappings(m -> m.skip(Directors::setFilms)).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(DirectorsDto source, Directors destination) {
        if (!Objects.isNull(source.getFilmsIds())) {
        destination.setFilms(filmsRepository.findAllByIdIn(source.getFilmsIds()));
        } else {
            destination.setFilms(null);
        }
    }

    @Override
    void mapSpecificFields(Directors source, DirectorsDto destination) {
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
