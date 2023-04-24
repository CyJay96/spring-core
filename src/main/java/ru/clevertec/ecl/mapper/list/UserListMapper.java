package ru.clevertec.ecl.mapper.list;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.model.dto.response.UserDtoResponse;
import ru.clevertec.ecl.model.entity.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserListMapper {

    List<UserDtoResponse> toDto(List<User> userList);
}
