package com.backend.SyncBoard.Service;

import com.backend.SyncBoard.DTO.ResponseBodyDTO;
import com.backend.SyncBoard.DTO.WorkSpaceRequestDTO;
import com.backend.SyncBoard.Model.User;
import com.backend.SyncBoard.Model.Workspace;
import com.backend.SyncBoard.Repository.UserRepository;
import com.backend.SyncBoard.Repository.WorkSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkSpaceService {

    @Autowired
    private WorkSpaceRepository workSpaceRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseBodyDTO createWorkSpace(WorkSpaceRequestDTO workSpaceRequestDTO){

        StringBuilder tags = new StringBuilder("");
        for(String tag : workSpaceRequestDTO.tags()){
            tags.append(tag).append(":");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication!=null && authentication.isAuthenticated()){
            String id = authentication.getName();
            Optional<User> user = userRepository.findById(UUID.fromString(id));
            if(user.isEmpty()){
                throw new UsernameNotFoundException("Not Found");
            }
            Workspace workspace = Workspace.builder()
                    .name(workSpaceRequestDTO.name())
                    .tags(tags.toString())
                    .slug(workSpaceRequestDTO.key())
                    .user(user.get())
                    .build();
            workSpaceRepository.save(workspace);
            return new ResponseBodyDTO(workspace,200, LocalDateTime.now());
        }
        throw new UsernameNotFoundException("Not Found");

    }
}
