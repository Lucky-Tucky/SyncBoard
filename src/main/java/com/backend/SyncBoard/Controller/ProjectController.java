package com.backend.SyncBoard.Controller;

import com.backend.SyncBoard.DTO.Request.ProjectDTO;
import com.backend.SyncBoard.DTO.Response.ResponseBodyDTO;
import com.backend.SyncBoard.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    
    @PostMapping("/add")
    public ResponseEntity<?> addProject(@RequestBody ProjectDTO projectDTO){
        ResponseBodyDTO responseBodyDTO = projectService.addProject(projectDTO);
        return new ResponseEntity<>(responseBodyDTO, HttpStatus.OK);
    }

//    @GetMapping("/")
//    public ResponseEntity<>

}
