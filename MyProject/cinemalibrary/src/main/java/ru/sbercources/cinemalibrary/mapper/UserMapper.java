package ru.sbercources.cinemalibrary.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sbercources.cinemalibrary.dto.UserDto;
import ru.sbercources.cinemalibrary.model.User;

@Component
public class UserMapper extends GenericMapper<User, UserDto>{

    private UserMapper(ModelMapper mapper) {
        super(mapper, User.class, UserDto.class);
    }
}
