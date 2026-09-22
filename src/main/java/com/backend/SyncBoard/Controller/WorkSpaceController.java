package com.backend.SyncBoard.Controller;

import com.backend.SyncBoard.DTO.Response.ResponseBodyDTO;
import com.backend.SyncBoard.DTO.Request.WorkSpaceCreationRequestDTO;
import com.backend.SyncBoard.DTO.Response.WorkSpaceResponseDTO;
import com.backend.SyncBoard.Service.WorkSpaceService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workspace")
public class WorkSpaceController {

    @Autowired
    private WorkSpaceService workSpaceService;

    @PostMapping("/add")
    public ResponseEntity<?> createWorkSpace(@RequestBody WorkSpaceCreationRequestDTO workSpaceCreationRequestDTO){
        ResponseBodyDTO responseBodyDTO = workSpaceService.createWorkSpace(workSpaceCreationRequestDTO);
        return new ResponseEntity<>(responseBodyDTO, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getWorkSpace(@PathVariable @NotNull @NotBlank String id){
        WorkSpaceResponseDTO response = workSpaceService.getWorkSpace(id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
