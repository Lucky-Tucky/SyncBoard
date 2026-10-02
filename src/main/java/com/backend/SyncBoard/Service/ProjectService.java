package com.backend.SyncBoard.Service;

import com.backend.SyncBoard.Config.KafkaConfig;
import com.backend.SyncBoard.DTO.Request.InvitationRequest;
import com.backend.SyncBoard.DTO.Request.ProjectDTO;
import com.backend.SyncBoard.DTO.Response.ResponseBodyDTO;
import com.backend.SyncBoard.Model.Invitation;
import com.backend.SyncBoard.Model.Project;
import com.backend.SyncBoard.Model.User;
import com.backend.SyncBoard.Model.Workspace;
import com.backend.SyncBoard.Repository.InvitationRepository;
import com.backend.SyncBoard.Repository.ProjectRepository;
import com.backend.SyncBoard.Repository.UserRepository;
import com.backend.SyncBoard.Repository.WorkSpaceRepository;
import org.springframework.beans.factory.annotation.Value;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@CacheConfig(cacheNames = {"project"})
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaConfig kafkaConfig;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private WorkSpaceRepository workSpaceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.redirect.url}")
    private String url;


    public ResponseBodyDTO addProject(ProjectDTO projectDTO){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!authentication.isAuthenticated()){
            throw new UsernameNotFoundException("Not Found");
        }

        Optional<User> user = userRepository.findById(UUID.fromString(authentication.getName()));

        if(user.isEmpty()) {
            throw new UsernameNotFoundException("Not Found");
        }

        Optional<Workspace> workspace = workSpaceRepository.findById(UUID.fromString(projectDTO.workspaceId()));

        if(workspace.isEmpty()){
            throw new EntityNotFoundException("Not Found");
        }

        Project project = Project.builder()
                .adminUser(user.get())
                .workspace(workspace.get())
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
                .token(UUID.randomUUID().toString())
                .status(com.backend.SyncBoard.Enum.InvitationStatus.PENDING)
                .expiresAt(LocalDateTime.now().plusHours(48))
                .build();

        invitationRepository.save(invitation);

        String htmlString = String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
                        .container { max-width: 600px; margin: 40px auto; background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
                        .header { text-align: center; padding-bottom: 20px; border-bottom: 1px solid #eeeeee; }
                        .header h1 { color: #333333; }
                        .content { padding: 20px 0; text-align: center; color: #555555; line-height: 1.6; }
                        .button { display: inline-block; padding: 12px 24px; margin: 20px 0; background-color: #007bff; color: #ffffff; text-decoration: none; border-radius: 5px; font-weight: bold; }
                        .footer { text-align: center; font-size: 12px; color: #aaaaaa; padding-top: 20px; border-top: 1px solid #eeeeee; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>SyncBoard Invitation</h1>
                        </div>
                        <div class="content">
                            <p>Hello,</p>
                            <p><strong>%s</strong> has invited you to join the project <strong>%s</strong> on SyncBoard.</p>
                            <p>Click the button below to accept the invitation and get started.</p>
                            <a href="%s" class="button">Accept Invitation</a>
                            <p>If you did not expect this invitation, you can safely ignore this email.</p>
                        </div>
                        <div class="footer">
                            <p>&copy; %s SyncBoard. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """, user.get().getName(), project.get().getName(), url+"/project/invite-redirect?token="+invitation.getToken(),LocalDateTime.now().getYear());
        Map<String,String> email = new HashMap<>();

        email.put("email",invitationRequest.email());
        email.put("subject","Invitation Request SyncBoard");
        email.put("body",htmlString);
        kafkaConfig.kafkaProducer("email-topic", objectMapper.writeValueAsString(email));
        return invitation;
    }

    public ResponseBodyDTO inviteUser(String token){

    }
    private UUID getUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.isAuthenticated()){
            throw new UsernameNotFoundException("Not Found");
        }
        return UUID.fromString(authentication.getName());
    }


}
