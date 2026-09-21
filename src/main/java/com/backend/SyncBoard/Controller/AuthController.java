package com.backend.SyncBoard.Controller;

import com.backend.SyncBoard.DTO.Response.AuthBodyDTO;
import com.backend.SyncBoard.DTO.Response.SignUpDTO;
import com.backend.SyncBoard.DTO.Request.UserRequestDTO;
import com.backend.SyncBoard.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${spring.samesite}")
    private String sameSiteConfig;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDTO userRequestDTO){
        AuthBodyDTO authBodyDTO = authService.login(userRequestDTO);
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token",authBodyDTO.refreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite(sameSiteConfig)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE,responseCookie.toString());
        return new ResponseEntity<>(authBodyDTO,headers, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpDTO userRequestDTO){
        AuthBodyDTO authBodyDTO = authService.signUp(userRequestDTO);
        return new ResponseEntity<>(authBodyDTO,HttpStatus.OK);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refresh-token") String token){
        AuthBodyDTO authBodyDTO = authService.refreshToken(token);
        return new ResponseEntity<>(authBodyDTO,HttpStatus.OK);
    }
}
