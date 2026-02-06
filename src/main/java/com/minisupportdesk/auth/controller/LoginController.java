package com.minisupportdesk.auth.controller;

import com.minisupportdesk.auth.DTO.LoginRequestDTO;
import com.minisupportdesk.auth.DTO.LoginResponseDTO;
import com.minisupportdesk.auth.services.LoginServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginServices loginServices;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid  @RequestBody LoginRequestDTO req){
        LoginResponseDTO responseDTO = loginServices.login(req);

        ResponseCookie cookie =ResponseCookie.from("access_token",responseDTO.getAccessToken())
                .secure(false)
                .httpOnly(true)
                .maxAge(1800)
                .path("/")
                .sameSite("Strict")
                .build();

        LoginResponseDTO response = LoginResponseDTO.builder()
                .message("Login Successfully")
                .role(responseDTO.getRole())
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(response);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(){
        ResponseCookie cookie =ResponseCookie.from("access_token","")
                .httpOnly(true)
                .sameSite("Strict")
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logout Successfully");
    }

}
