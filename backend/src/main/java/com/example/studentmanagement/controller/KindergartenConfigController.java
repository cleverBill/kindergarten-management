package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.KindergartenConfigDTO;
import com.example.studentmanagement.service.KindergartenConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class KindergartenConfigController {

    @Autowired
    private KindergartenConfigService configService;

    @GetMapping
    public ResponseEntity<KindergartenConfigDTO> getConfig() {
        KindergartenConfigDTO config = configService.getConfig();
        return ResponseEntity.ok(config);
    }

    @PutMapping
    public ResponseEntity<KindergartenConfigDTO> updateConfig(@RequestBody KindergartenConfigDTO configDTO) {
        KindergartenConfigDTO updated = configService.updateConfig(configDTO);
        return ResponseEntity.ok(updated);
    }
}
