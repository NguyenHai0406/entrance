package com.test.entrance.mapstruct;

import com.test.entrance.domain.User;
import com.test.entrance.dto.RefreshTokenDTO;
import com.test.entrance.dto.SignInDTO;
import com.test.entrance.dto.UserDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-07-04T16:20:30+0700",
    comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.4.1.jar, environment: Java 11 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        if ( user.getId() != null ) {
            userDTO.id( user.getId() );
        }
        userDTO.firstName( user.getFirstName() );
        userDTO.lastName( user.getLastName() );
        userDTO.email( user.getEmail() );

        userDTO.displayName( user.getFirstName() + " " + user.getLastName() );

        return userDTO.build();
    }

    @Override
    public User toUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( userDTO.getId() );
        user.firstName( userDTO.getFirstName() );
        user.lastName( userDTO.getLastName() );
        user.email( userDTO.getEmail() );
        user.password( userDTO.getPassword() );

        return user.build();
    }

    @Override
    public SignInDTO toLoginDTO(User user) {
        if ( user == null ) {
            return null;
        }

        SignInDTO.SignInDTOBuilder signInDTO = SignInDTO.builder();

        signInDTO.user( toUserDTO( user ) );

        return signInDTO.build();
    }

    @Override
    public RefreshTokenDTO toRefreshTokenDto(String token, String refreshToken) {
        if ( token == null && refreshToken == null ) {
            return null;
        }

        RefreshTokenDTO.RefreshTokenDTOBuilder refreshTokenDTO = RefreshTokenDTO.builder();

        refreshTokenDTO.token( token );
        refreshTokenDTO.refreshToken( refreshToken );

        return refreshTokenDTO.build();
    }
}
