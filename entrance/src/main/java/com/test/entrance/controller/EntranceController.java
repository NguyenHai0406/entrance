package com.test.entrance.controller;

import com.test.entrance.dto.*;
import com.test.entrance.service.EntranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/api")
public class EntranceController {
    @Autowired
    EntranceService entranceService;

    @PostMapping(value = "/sign-up", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO signUp(@Valid @RequestBody UserDTO user) {
        return entranceService.signUp(user);
    }

    @PostMapping(value = "/sign-in", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public SignInDTO signIn(@Valid @RequestBody SignInRequestDTO loginRequest) {
        return entranceService.signIn(loginRequest);
    }

    @PostMapping(value = "/sign-out")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signOut(@RequestHeader long id) {
        entranceService.signOut(id);
    }

    @PostMapping(value = "/refresh-token", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RefreshTokenDTO refreshToken(@RequestHeader String token) {
        return entranceService.refreshToken(token);
    }
}
