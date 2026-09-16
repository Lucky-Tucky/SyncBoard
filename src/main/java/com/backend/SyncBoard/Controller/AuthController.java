package com.backend.SyncBoard.Controller;

import com.backend.SyncBoard.DTO.AuthBodyDTO;
import com.backend.SyncBoard.DTO.SignUpDTO;
import com.backend.SyncBoard.DTO.UserRequestDTO;
import com.backend.SyncBoard.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDTO userRequestDTO){
        AuthBodyDTO authBodyDTO = authService.login(userRequestDTO);
        return new ResponseEntity<>(authBodyDTO, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpDTO userRequestDTO){
        AuthBodyDTO authBodyDTO = authService.signUp(userRequestDTO);
        return  new ResponseEntity<>(authBodyDTO,HttpStatus.OK);
    }
}
