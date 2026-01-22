package com.starter_squad.lms.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.starter_squad.lms.dto.FeedbackRequest;
import com.starter_squad.lms.entity.Feedback;
import com.starter_squad.lms.service.FeedbackService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/{courseId}")
    public List<Feedback> getFeedbacksForCourse(@PathVariable UUID courseId) {
        return feedbackService.getFeedbacksForCourse(courseId);
    }

    @PostMapping
    public String submitFeedback(@RequestBody FeedbackRequest fr) {
        return feedbackService.submitFeedback(fr);
    }
}
