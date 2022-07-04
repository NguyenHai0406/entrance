package com.test.entrance.service;

import com.test.entrance.dto.*;

public interface EntranceService {
    /**
     * user sign up
     * @param login
     * @return
     */
    UserDTO signUp(UserDTO login);

    /**
     * User sign in
     * @param loginRequest
     * @return
     */
    SignInDTO signIn(SignInRequestDTO loginRequest);

    /**
     * user sign out
     */
    void signOut(long id);

    /**
     * user refresh token
     * @param refreshToken
     * @return
     */
    RefreshTokenDTO refreshToken(String refreshToken);
}
