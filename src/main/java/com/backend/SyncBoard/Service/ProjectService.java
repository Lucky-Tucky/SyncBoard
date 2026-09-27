package com.backend.SyncBoard.Service;

import com.backend.SyncBoard.DTO.Request.InvitationRequest;
import com.backend.SyncBoard.DTO.Request.ProjectDTO;
import com.backend.SyncBoard.DTO.Response.ResponseBodyDTO;
import com.backend.SyncBoard.Model.Invitation;
import com.backend.SyncBoard.Model.Project;
import com.backend.SyncBoard.Model.User;
import com.backend.SyncBoard.Repository.ProjectRepository;
import com.backend.SyncBoard.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@CacheConfig(cacheNames = {"project"})
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseBodyDTO addProject(ProjectDTO projectDTO){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!authentication.isAuthenticated()){
            throw new UsernameNotFoundException("Not Found");
        }

        Optional<User> user = userRepository.findById(UUID.fromString(authentication.getName()));

        if(user.isEmpty()) {
            throw new UsernameNotFoundException("Not Found");
        }

        Project project = Project.builder()
                .adminUser(user.get())
                .name(projectDTO.name())
                .key(projectDTO.key())
                .build();

        projectRepository.save(project);

        return new ResponseBodyDTO(project,200, LocalDateTime.now());
    }

    @Cacheable(key="#id")
    public ResponseBodyDTO getProject(String id){
        UUID userId = getUserId();
        Optional<Project> project = projectRepository.findByUserIdAndProjectId(userId,UUID.fromString(id));

        if(project.isEmpty()){
            throw new EntityNotFoundException("Not Found");
        }

        return new ResponseBodyDTO(project.get(),200,LocalDateTime.now());
    }

    public Invitation projectInvitation(InvitationRequest invitationRequest){

        UUID userId = getUserId();
        Optional<User> user = userRepository.findById(userId);

        Optional<Project> project = projectRepository.findById(UUID.fromString(invitationRequest.projectId()));

        if(project.isEmpty()){
            throw new EntityNotFoundException("Not Found");
        }
        Invitation invitation = Invitation.builder()
                .invitedBy(user.get())
                .email(invitationRequest.email())
                .project(project.get())
                .expiresAt(LocalDateTime.now().plusHours(48))
                .build();

        return invitation;
    }

    private UUID getUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.isAuthenticated()){
            throw new UsernameNotFoundException("Not Found");
        }
        return UUID.fromString(authentication.getName());
    }


}
