package ru.sbercources.cinemalibrary.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sbercources.cinemalibrary.dto.FilmsDto;
import ru.sbercources.cinemalibrary.model.Films;
import ru.sbercources.cinemalibrary.model.GenericModel;
import ru.sbercources.cinemalibrary.repository.DirectorsRepository;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FilmsMapper extends GenericMapper<Films, FilmsDto> {
    private final ModelMapper mapper;
    private final DirectorsRepository directorsRepository;

    public FilmsMapper(ModelMapper mapper, DirectorsRepository directorsRepository) {
        super(mapper, Films.class, FilmsDto.class);
        this.mapper = mapper;
        this.directorsRepository = directorsRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Films.class, FilmsDto.class)
                .addMappings(m -> m.skip(FilmsDto::setDirectorsIds)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(FilmsDto.class, Films.class)
                .addMappings(m -> m.skip(Films::setDirectors)).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(FilmsDto source, Films destination) {
        if (!Objects.isNull(source.getDirectorsIds())) {
            destination.setDirectors(directorsRepository.findAllByIdIn(source.getDirectorsIds()));
        } else {
            destination.setDirectors(null);
        }
    }

    @Override
    void mapSpecificFields(Films source, FilmsDto destination) {
        destination.setDirectorsIds(getIds(source));

    }

    private Set<Long> getIds(Films films) {
        return Objects.isNull(films) || Objects.isNull(films.getId())
                ? null
                : films.getDirectors().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
}
