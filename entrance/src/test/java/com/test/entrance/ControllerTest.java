package com.test.entrance;

import com.test.entrance.domain.Token;
import com.test.entrance.domain.User;
import com.test.entrance.dto.*;
import com.test.entrance.repository.TokenRepository;
import com.test.entrance.repository.UserRepository;
import com.test.entrance.service.EntranceService;
import com.test.entrance.service.impl.EntranceServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ControllerTest {
    private final static String URL_SIGN_UP = "/api/sign-up";
    private final static String URL_SIGN_IN = "/api/sign-in";
    private final static String URL_SIGN_OUT = "/api/sign-out";
    private final static String URL_REFRESH_TOKEN = "/api/refresh-token";
    private final static String EMAIL = "nguyenhai@gmail.com";
    private final static String FIRST_NAME = "nguyen";
    private final static String LAST_NAME = "hai";
    private final static String PASSWORD = "l23456789";
    private final static String TOKEN = "l23456789";

    @Autowired
    WebTestClient webTestClient;

    @InjectMocks
    EntranceService entranceService = new EntranceServiceImpl();

    @Mock
    UserRepository userRepository;

    @Autowired
    UserRepository repository;

    @Autowired
    TokenRepository tokenRepository;

    @AfterEach
    void afterEach() {
        Optional<User> userOptional = repository.findByEmail(EMAIL);
        if (userOptional.isPresent()) {
            tokenRepository.deleteAll(tokenRepository.findAllByUserId(userOptional.get().getId()));
            repository.delete(userOptional.get());
        }
    }

    @Test
    @DisplayName("Test sign up validation un success")
    void signUp_UnSuccess() {
        webTestClient.post()
                .uri(URL_SIGN_UP)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(UserDTO.builder().build()))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .consumeWith(res -> assertNotNull(res.getResponseBody()));
    }

    @Test
    @DisplayName("Test sign up exist email")
    void signUp_ExistEmail() {
        repository.save(createUser());
        webTestClient.post()
                .uri(URL_SIGN_UP)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(createUserDTO()))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .consumeWith(res -> assertNotNull(res.getResponseBody()));
    }

    @Test
    @DisplayName("Test sign up success")
    void signUp() {
        when(userRepository.existsUsersByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        webTestClient.post()
                .uri(URL_SIGN_UP)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(createUserDTO()))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody();
    }

    @Test
    @DisplayName("Test sign in validation un success")
    void signIn_UnSuccess() {
        webTestClient.post()
                .uri(URL_SIGN_IN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(SignInRequestDTO.builder().build()))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .consumeWith(res -> assertNotNull(res.getResponseBody()));
    }

    @Test
    @DisplayName("Test sign in email is not exist")
    void signIn_NotExistEmail() {
        webTestClient.post()
                .uri(URL_SIGN_IN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(createSignInRequest()))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .consumeWith(res -> assertNotNull(res.getResponseBody()));
    }

    @Test
    @DisplayName("Test sign in password is wrong")
    void signIn_WrongPassword() {
        repository.save(createUser());
        SignInRequestDTO request = createSignInRequest();
        request.setPassword("12345678");
        webTestClient.post()
                .uri(URL_SIGN_IN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .consumeWith(res -> assertNotNull(res.getResponseBody()));
    }

    @Test
    @DisplayName("Test sign in success")
    void signIn() {
        repository.save(createUser());
        webTestClient.post()
                .uri(URL_SIGN_IN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(createSignInRequest()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody();
    }

    @Test
    @DisplayName("Test sign out success")
    void signOut() {
        User user = repository.save(createUser());
        webTestClient.post()
                .uri(URL_SIGN_OUT)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers -> headers.add("id", user.getId().toString()))
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody();
    }

    @Test
    @DisplayName("Test refresh token un success")
    void refreshToken_UnSuccess() {
        webTestClient.post()
                .uri(URL_REFRESH_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers -> headers.add("token", TOKEN))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .consumeWith(res -> assertNotNull(res.getResponseBody()));
    }

    @Test
    @DisplayName("Test refresh token success")
    void refreshToken() {
        User user = repository.save(createUser());
        Token token = createToken(user);
        tokenRepository.save(token);
        webTestClient.post()
                .uri(URL_REFRESH_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers -> headers.add("token", TOKEN))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody();
    }

    private UserDTO createUserDTO() {
        return UserDTO.builder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .password(PASSWORD)
                .lastName(LAST_NAME)
                .build();
    }

    private User createUser() {
        return User.builder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .password("$2a$12$KfegV6Bsin0nVO2qQEWrT.YW2JNoqaTPhBvmWZkFKOQ2i6MbFA0p2")
                .lastName(LAST_NAME)
                .build();
    }

    private SignInRequestDTO createSignInRequest() {
        return SignInRequestDTO.builder()
                .password(PASSWORD)
                .email(EMAIL)
                .build();
    }

    private Token createToken(User user) {
        return Token.builder()
                .refreshToken(TOKEN)
                .user(user)
                .build();
    }

}
