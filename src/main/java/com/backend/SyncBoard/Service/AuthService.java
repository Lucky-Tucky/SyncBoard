package com.backend.SyncBoard.Service;

import com.backend.SyncBoard.DTO.AuthBodyDTO;
import com.backend.SyncBoard.DTO.ResponseBodyDTO;
import com.backend.SyncBoard.DTO.SignUpDTO;
import com.backend.SyncBoard.DTO.UserRequestDTO;
import com.backend.SyncBoard.Enum.Roles;
import com.backend.SyncBoard.Model.CustomUserDetail;
import com.backend.SyncBoard.Model.User;
import com.backend.SyncBoard.Repository.UserRepository;
import com.backend.SyncBoard.Utils.AuthUtils;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthBodyDTO login(UserRequestDTO userRequestDTO){
        Optional<User> user = userRepository.findByEmail(userRequestDTO.email());

        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not Found");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                     user.get().getId(),
                     userRequestDTO.password()
                )
        );

        String jwtToken = authUtils.generateJwtToken(new CustomUserDetail(user.get()));

        return new AuthBodyDTO(user.get().getName(),jwtToken,200, LocalDateTime.now());

    }

    public AuthBodyDTO signUp(SignUpDTO userRequestDTO){

        Optional<User> user  = userRepository.findByEmail(userRequestDTO.email());
        if(user.isPresent()){
            throw new EntityExistsException("Already Present");
        }
        String encode_password = bCryptPasswordEncoder.encode(userRequestDTO.password());
        User new_user = User.builder()
                .role(Roles.ADMIN)
                .name(userRequestDTO.name())
                .email(userRequestDTO.email())
                .password(encode_password)
                .build();
        userRepository.save(new_user);
        String jwtToken= authUtils.generateJwtToken(new CustomUserDetail(new_user));

        return new AuthBodyDTO(new_user.getName(),jwtToken,200,LocalDateTime.now());
    }
}
