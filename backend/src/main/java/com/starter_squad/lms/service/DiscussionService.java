package com.starter_squad.lms.service;
import java.util.List;
import java.util.UUID;

import com.starter_squad.lms.dto.DiscussionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.starter_squad.lms.entity.Course;
import com.starter_squad.lms.entity.Discussion;
import com.starter_squad.lms.repository.DiscussionRepository;

@RequiredArgsConstructor
@Service
public class DiscussionService {

    private final DiscussionRepository discussionRepository;
    private final CourseService courseService;

    public List<Discussion> getDiscussionsCourse(UUID courseId) {
        Course course = courseService.getCourseById(courseId);
        return discussionRepository.findByCourse(course);
    }
    public Discussion createDiscussion( DiscussionRequest discussionRequest) {
        Course course = courseService.getCourseById(discussionRequest.getCourse_id());
        Discussion discussion = new Discussion();
        discussion.setUserName(discussionRequest.getName());
        discussion.setCourse(course);
        discussion.setContent(discussionRequest.getContent());
        return discussionRepository.save(discussion);
    }
}
