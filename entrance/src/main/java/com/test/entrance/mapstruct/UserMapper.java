package com.test.entrance.mapstruct;

import com.test.entrance.domain.User;
import com.test.entrance.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "displayName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    @Mapping(target = "password", ignore = true)
    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);

    SignInDTO toLoginDTO(User user);

    RefreshTokenDTO toRefreshTokenDto(String token, String refreshToken);

}
