package com.backend.SyncBoard.Controller;

import com.backend.SyncBoard.DTO.ResponseBodyDTO;
import com.backend.SyncBoard.DTO.WorkSpaceRequestDTO;
import com.backend.SyncBoard.Service.WorkSpaceService;
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
    public ResponseEntity<?> createWorkSpace(@RequestBody WorkSpaceRequestDTO workSpaceRequestDTO){
        ResponseBodyDTO responseBodyDTO = workSpaceService.createWorkSpace(workSpaceRequestDTO);
        return new ResponseEntity<>(responseBodyDTO, HttpStatus.OK);
    }
}
