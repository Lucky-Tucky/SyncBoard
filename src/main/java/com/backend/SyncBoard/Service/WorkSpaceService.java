package com.backend.SyncBoard.Service;

import com.backend.SyncBoard.DTO.Response.ResponseBodyDTO;
import com.backend.SyncBoard.DTO.Request.WorkSpaceCreationRequestDTO;
import com.backend.SyncBoard.DTO.Response.WorkSpaceResponseDTO;
import com.backend.SyncBoard.Model.User;
import com.backend.SyncBoard.Model.Workspace;
import com.backend.SyncBoard.Repository.UserRepository;
import com.backend.SyncBoard.Repository.WorkSpaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class WorkSpaceService {

    @Autowired
    private WorkSpaceRepository workSpaceRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseBodyDTO createWorkSpace(WorkSpaceCreationRequestDTO workSpaceCreationRequestDTO){

        StringBuilder tags = new StringBuilder("");
        for(String tag : workSpaceCreationRequestDTO.tags()){
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
                    .name(workSpaceCreationRequestDTO.name())
                    .tags(tags.toString())
                    .slug(workSpaceCreationRequestDTO.key())
                    .user(user.get())
                    .build();
            workSpaceRepository.save(workspace);
            return new ResponseBodyDTO(workspace,200, LocalDateTime.now());
        }
        throw new UsernameNotFoundException("Not Found");

    }

    public WorkSpaceResponseDTO getWorkSpace(String id){

        Authentication securityContext = SecurityContextHolder.getContext().getAuthentication();
        if(securityContext==null || !securityContext.isAuthenticated()){
            throw new UsernameNotFoundException("Not Found");
        }
        String userId = securityContext.getName();

        Optional<Workspace> workspace =  workSpaceRepository.findByIdAndWorkSpaceId(UUID.fromString(id),UUID.fromString(userId));

        if(workspace.isEmpty()){
            throw new EntityNotFoundException("Not Found");
        }

        List<String> tags = new ArrayList<>();
        Arrays.stream(workspace.get().getTags().split(":")).forEach((tag)->{
            if(!tag.trim().isEmpty()){
                tags.add(tag.trim());
            }
        });

        return WorkSpaceResponseDTO.builder()
                .createdAt(workspace.get().getCreatedAt())
                .issue_number(workspace.get().getIssued_value())
                .name(workspace.get().getName())
                .key(workspace.get().getSlug())
                .tags(tags)
                .build();
    }
}
