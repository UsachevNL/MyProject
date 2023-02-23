package ru.sbercources.cinemalibrary.mapper;

import ru.sbercources.cinemalibrary.dto.GenericDto;
import ru.sbercources.cinemalibrary.model.GenericModel;

import java.util.List;

public interface Mapper <E extends GenericModel, D extends GenericDto>{
    E toEntity(D dto);
    List<E> toEntities(List<D> dtos);

    D toDto(E entity);
    List<D> toDtos(List<E> entities);
}
