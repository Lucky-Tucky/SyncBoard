package com.backend.SyncBoard.Service;

import com.backend.SyncBoard.DTO.AuthBodyDTO;
import com.backend.SyncBoard.DTO.ResponseBodyDTO;
import com.backend.SyncBoard.DTO.SignUpDTO;
import com.backend.SyncBoard.DTO.UserRequestDTO;
import com.backend.SyncBoard.Enum.Roles;
import com.backend.SyncBoard.Model.CustomUserDetail;
import com.backend.SyncBoard.Model.RefreshToken;
import com.backend.SyncBoard.Model.User;
import com.backend.SyncBoard.Repository.RefreshTokenRepository;
import com.backend.SyncBoard.Repository.UserRepository;
import com.backend.SyncBoard.Utils.AuthUtils;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Transactional
    public AuthBodyDTO login(UserRequestDTO userRequestDTO){
        Optional<User> user = userRepository.findByEmail(userRequestDTO.email());

        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not Found");
        }
        CustomUserDetail customUserDetail = new CustomUserDetail(user.get());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequestDTO.email(),userRequestDTO.password())
        );
        System.out.println("After Authentication");
        String jwtToken = authUtils.generateJwtToken(customUserDetail,100);
        String refreshToken = generateAndSaveRefreshToken(user.get().getId());
        return new AuthBodyDTO(user.get().getName(),refreshToken,jwtToken,200, LocalDateTime.now());

    }

    @Transactional
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
        return new AuthBodyDTO(new_user.getName() , null ,null,200,LocalDateTime.now());
    }

    public AuthBodyDTO refreshToken(String token){

        String _id = authUtils.getJwtClaim(token);
        if(_id.trim().isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        Optional<User> user = userRepository.findById(UUID.fromString(_id));
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        RefreshToken user_token = user.get().getRefreshToken();

        String tmp_encode = authUtils.hashToken(token);

        if(!user_token.getToken().equals(tmp_encode) && user_token.isRevoked()){
            throw new UsernameNotFoundException("User not found");
        }

        String jwtToken= authUtils.generateJwtToken(new CustomUserDetail(user.get()),100);

        return new AuthBodyDTO(user.get().getName(),null,jwtToken,200,LocalDateTime.now());
    }

    private String generateAndSaveRefreshToken(UUID user_id){

        Optional<User> user = userRepository.findById(user_id);

        if(user.isEmpty()){
            throw new UsernameNotFoundException("Not found!");
        }

        User get_user = user.get();

        RefreshToken refreshToken = get_user.getRefreshToken();
        String token = authUtils.generateJwtToken(new CustomUserDetail(get_user),604800000L);

        if(refreshToken==null){
                refreshToken = RefreshToken.builder()
                        .token(token)
                        .user(get_user)
                        .isRevoked(false)
                        .build();
        }else{
            refreshToken.setToken(authUtils.hashToken(token));
            refreshToken.setRevoked(false);
        }

        refreshTokenRepository.save(refreshToken);
        return token;
    }
}
