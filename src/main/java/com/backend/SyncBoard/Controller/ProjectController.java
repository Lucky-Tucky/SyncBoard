package com.backend.SyncBoard.Controller;

import com.backend.SyncBoard.DTO.Request.InvitationRequest;
import com.backend.SyncBoard.DTO.Request.ProjectDTO;
import com.backend.SyncBoard.DTO.Response.ResponseBodyDTO;
import com.backend.SyncBoard.Model.Invitation;
import com.backend.SyncBoard.Service.ProjectService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    
    @PostMapping("/add")
    public ResponseEntity<?> addProject(@RequestBody @Valid ProjectDTO projectDTO){
        ResponseBodyDTO responseBodyDTO = projectService.addProject(projectDTO);
        return new ResponseEntity<>(responseBodyDTO, HttpStatus.OK);
    }

    @PostMapping("/user-invite")
    public ResponseEntity<?> inviteUser(@RequestBody @Valid InvitationRequest invitationRequest){
        Invitation invitation = projectService.projectInvitation(invitationRequest);
        return new ResponseEntity<>(new ResponseBodyDTO(invitation,200, LocalDateTime.now()), HttpStatus.OK);
    }

    @GetMapping("/invite-redirect")
    public ResponseEntity<?> inviteRedirect(@RequestParam(name = "token") @NotNull String token){

    }

}
