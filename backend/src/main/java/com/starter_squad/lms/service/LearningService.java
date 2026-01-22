package com.starter_squad.lms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.starter_squad.lms.dto.EnrollRequest;
import com.starter_squad.lms.entity.Course;
import com.starter_squad.lms.entity.Learning;
import com.starter_squad.lms.entity.Progress;
import com.starter_squad.lms.entity.User;
import com.starter_squad.lms.repository.CourseRepository;
import com.starter_squad.lms.repository.LearningRepository;
import com.starter_squad.lms.repository.ProgressRepository;
import com.starter_squad.lms.repository.UserRepository;
import java.util.*;

@RequiredArgsConstructor
@Service
public class LearningService {

    private final LearningRepository learningRepository;

    private final UserRepository userRepository;

    private final CourseRepository courseRepository;
    
    private final ProgressRepository progressRepository;

    public List<Course> getLearningCourses(UUID userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Course> learningCourses = new ArrayList<>();

            for (Learning learning : user.getLearningCourses()) {
                Course course = learning.getCourse();
                learningCourses.add(course);
            }

            return learningCourses;
        }

        return null;
    }
    
    public List<Learning> getEnrollments() {
    	return learningRepository.findAll();
    }

    public String enrollCourse(EnrollRequest enrollRequest) {
        User user = userRepository.findById(enrollRequest.getUserId()).orElse(null);
        Course course = courseRepository.findById(enrollRequest.getCourseId()).orElse(null);

        if (user != null && course != null) {
            Learning existingLearning = learningRepository.findByUserAndCourse(user, course);
            if (existingLearning != null) {
                return "Course already enrolled";
            }

            Progress progress = new Progress();
            progress.setUser(user);
            progress.setCourse(course);
            progressRepository.save(progress);

            Learning learning = new Learning();
            learning.setUser(user);
            learning.setCourse(course);
            learningRepository.save(learning);

            return "Enrolled successfully";
        }

        return "Failed to enroll";
    }


    public void unenrollCourse(UUID id) {
        learningRepository.deleteById(id);
    }
    
    // Get all enrollments for instructor's courses
    public List<Learning> getEnrollmentsByInstructorId(UUID instructorId) {
        return learningRepository.findByInstructorId(instructorId);
    }
    
    // Get total unique students enrolled in instructor's courses
    public long getStudentCountByInstructorId(UUID instructorId) {
        return learningRepository.countStudentsByInstructorId(instructorId);
    }
    
    // Get recent enrollments for instructor's courses (limited)
    public List<Learning> getRecentEnrollmentsByInstructorId(UUID instructorId, int limit) {
        List<Learning> enrollments = learningRepository.findRecentByInstructorId(instructorId);
        return enrollments.stream().limit(limit).toList();
    }
    
    // Get enrollments by course ID
    public List<Learning> getEnrollmentsByCourseId(UUID courseId) {
        return learningRepository.findByCourseId(courseId);
    }
}