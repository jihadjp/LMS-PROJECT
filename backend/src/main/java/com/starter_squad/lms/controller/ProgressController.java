package com.starter_squad.lms.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.starter_squad.lms.dto.ProgressRequest;
import com.starter_squad.lms.service.ProgressService;

import java.util.UUID;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @GetMapping("/{userId}/{courseId}")
    public float getProgress(@PathVariable UUID userId, @PathVariable UUID courseId) {
        return progressService.getProgress(userId, courseId);
    }

    @PutMapping("/update-progress")
    public ResponseEntity<String> updateProgress(@RequestBody ProgressRequest request) {
        return progressService.updateProgress(request);
    }
    
    @PutMapping("/update-duration")
    public ResponseEntity<String> updateDuration(@RequestBody ProgressRequest request) {
        return progressService.updateDuration(request);
    }
}