package com.backend.SyncBoard.Config;

import com.backend.SyncBoard.Model.CustomUserDetail;
import com.backend.SyncBoard.Model.User;
import com.backend.SyncBoard.Repository.UserRepository;
import com.backend.SyncBoard.Utils.AuthUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("authorization");

        if(authorization==null){
            filterChain.doFilter(request,response);
            return;
        }
        String token = authorization.split("Bearer ")[1].trim();
        logger.info("Token-->"+token);
        if(token == null || token.trim().length()<=0){
            filterChain.doFilter(request,response);
            return;
        }

        String _id = authUtils.getJwtClaim(token);

        if(_id.trim().length()<=0){
            filterChain.doFilter(request,response);
            return;
        }

        Optional<User> user = userRepository.findById(UUID.fromString(_id));

        if(user.isEmpty()){
            filterChain.doFilter(request,response);
            return;
        }

        CustomUserDetail customUserDetail = new CustomUserDetail(user.get());
        logger.info(customUserDetail.getUsername());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(customUserDetail,null,customUserDetail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        filterChain.doFilter(request,response);
    }
}
