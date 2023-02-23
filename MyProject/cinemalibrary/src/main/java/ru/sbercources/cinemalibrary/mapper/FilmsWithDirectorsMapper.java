package ru.sbercources.cinemalibrary.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sbercources.cinemalibrary.dto.FilmsWithDirectorsDto;
import ru.sbercources.cinemalibrary.model.Films;
import ru.sbercources.cinemalibrary.model.GenericModel;
import ru.sbercources.cinemalibrary.repository.DirectorsRepository;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
@Component
public class FilmsWithDirectorsMapper extends GenericMapper<Films, FilmsWithDirectorsDto>{

    private final ModelMapper mapper;
    private final DirectorsRepository directorsRepository;

    private FilmsWithDirectorsMapper(ModelMapper mapper, DirectorsRepository directorsRepository) {
        super(mapper, Films.class, FilmsWithDirectorsDto.class);
        this.mapper = mapper;
        this.directorsRepository = directorsRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Films.class, FilmsWithDirectorsDto.class)
                .addMappings(m -> m.skip(FilmsWithDirectorsDto::setDirectorsIds)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(FilmsWithDirectorsDto.class, Films.class)
                .addMappings(m -> m.skip(Films::setDirectors)).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(FilmsWithDirectorsDto source, Films destination) {
        destination.setDirectors(directorsRepository.findAllByIdIn(source.getDirectorsIds()));
    }

    @Override
    void mapSpecificFields(Films source, FilmsWithDirectorsDto destination) {
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
