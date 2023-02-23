package ru.sbercources.cinemalibrary.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sbercources.cinemalibrary.dto.OrdersDto;
import ru.sbercources.cinemalibrary.model.Orders;
import ru.sbercources.cinemalibrary.repository.FilmsRepository;
import ru.sbercources.cinemalibrary.repository.UserRepository;

import javax.annotation.PostConstruct;
@Component
public class OrdersMapper  extends GenericMapper<Orders, OrdersDto> {

    private final FilmsRepository filmsRepository;
    private final UserRepository userRepository;

    private OrdersMapper(ModelMapper mapper, FilmsRepository filmsRepository, UserRepository userRepository) {
        super(mapper, Orders.class, OrdersDto.class);
        this.filmsRepository = filmsRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void setupMapper() {
        super.mapper.createTypeMap(Orders.class, OrdersDto.class)
                .addMappings(m -> m.skip(OrdersDto::setUserId)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrdersDto::setFilmsId)).setPostConverter(toDtoConverter());
//    super.mapper.createTypeMap(PublishDto.class, Publish.class)
//        .addMappings(m -> m.skip(Publish::setUser)).setPostConverter(toEntityConverter())
//        .addMappings(m -> m.skip(Publish::setBook)).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(OrdersDto source, Orders destination) {
        destination.setFilms(filmsRepository.findById(source.getFilmsId()).orElseThrow());
        destination.setUser(userRepository.findById(source.getUserId()).orElseThrow());
    }

    @Override
    void mapSpecificFields(Orders source, OrdersDto destination) {
        destination.setUserId(source.getUser().getId());
        destination.setFilmsId(source.getFilms().getId());
    }
}
