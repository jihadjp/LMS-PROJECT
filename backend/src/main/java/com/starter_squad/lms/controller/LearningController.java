package com.starter_squad.lms.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.starter_squad.lms.dto.EnrollRequest;
import com.starter_squad.lms.entity.Course;
import com.starter_squad.lms.entity.Learning;
import com.starter_squad.lms.service.LearningService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/learning")
public class LearningController {

    @Autowired
    private LearningService learningService;

    @GetMapping("/{userId}")
    public List<Course> getLearningCourses(@PathVariable UUID userId) {
        return learningService.getLearningCourses(userId);
    }
    
    @GetMapping
    public List<Learning> getEnrollments() {
        return learningService.getEnrollments();
    }

    @PostMapping
    public String enrollCourse(@RequestBody EnrollRequest enrollRequest) {
        return learningService.enrollCourse(enrollRequest);
    }

    @DeleteMapping("/{id}")
    public void unenrollCourse(@PathVariable UUID id) {
        learningService.unenrollCourse(id);
    }
}
