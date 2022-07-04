package com.test.entrance.service.impl;

import com.test.entrance.domain.Token;
import com.test.entrance.domain.User;
import com.test.entrance.dto.*;
import com.test.entrance.exception.NotFoundException;
import com.test.entrance.exception.ValidationException;
import com.test.entrance.mapstruct.UserMapper;
import com.test.entrance.repository.TokenRepository;
import com.test.entrance.repository.UserRepository;
import com.test.entrance.service.EntranceService;
import com.test.entrance.service.JwtToken;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.test.entrance.constant.ErrorMessage.*;

@Service
public class  EntranceServiceImpl implements EntranceService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    JwtToken jwtToken;

    @Override
    public UserDTO signUp(UserDTO userLogin) {
        if (userRepository.existsUsersByEmail(userLogin.getEmail())) {
            throw new ValidationException(EMAIL_EXIST);
        }
        String password = BCrypt.hashpw(userLogin.getPassword(), BCrypt.gensalt(12));
        userLogin.setPassword(password);
        User user = userRepository.save(userMapper.toUser(userLogin));
        return userMapper.toUserDTO(user);
    }

    @Override
    public SignInDTO signIn(SignInRequestDTO loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
        if (user.isPresent()) {
            if (!BCrypt.checkpw(loginRequest.getPassword(), user.get().getPassword())) {
                throw new ValidationException(EMAIL_PASSWORD_INCORRECT);
            }
            Token refreshToken = Token.builder()
                    .expiresIn("30")
                    .refreshToken(jwtToken.generateRefreshToken(user.get().getLastName()))
                    .user(user.get())
                    .build();
            tokenRepository.save(refreshToken);
            SignInDTO login = userMapper.toLoginDTO(user.get());
            login.setToken(jwtToken.generateToken(user.get().getLastName()));
            login.setRefreshToken(refreshToken.getRefreshToken());
            return login;
        } else {
            throw new ValidationException(EMAIL_PASSWORD_INCORRECT);
        }
    }

    @Override
    public void signOut(long id) {
        List<Token> tokens = tokenRepository.findAllByUserId(id);
        tokenRepository.deleteAll(tokens);
    }

    @Override
    public RefreshTokenDTO refreshToken(String refreshToken) {
        Optional<Token> tokenOptional = tokenRepository.findByRefreshToken(refreshToken);
        if (tokenOptional.isPresent()) {
            Token token = tokenOptional.get();
            token.setRefreshToken(jwtToken.generateRefreshToken(token.getUser().getLastName()));
            tokenRepository.save(token);
            String tokenData = jwtToken.generateToken(token.getUser().getLastName());
            return userMapper.toRefreshTokenDto(tokenData, token.getRefreshToken());
         } else {
            throw new NotFoundException(NOT_FOUND_TOKEN);
        }
    }
}
