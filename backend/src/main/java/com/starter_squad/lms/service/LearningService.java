package com.starter_squad.lms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    // ✅ @Transactional — Session open রাখবে, LazyInitializationException হবে না
    @Transactional(readOnly = true)
    public List<Course> getLearningCourses(UUID userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Course> learningCourses = new ArrayList<>();
            for (Learning learning : user.getLearningCourses()) {
                learningCourses.add(learning.getCourse());
            }
            return learningCourses;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<Learning> getEnrollments() {
        return learningRepository.findAll();
    }

    @Transactional
    public String enrollCourse(EnrollRequest enrollRequest) {
        User user = userRepository.findById(enrollRequest.getUserId()).orElse(null);
        Course course = courseRepository.findById(enrollRequest.getCourseId()).orElse(null);

        if (user != null && course != null) {
            Learning existingLearning = learningRepository.findByUserAndCourse(user, course);
            if (existingLearning != null) return "Course already enrolled";

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

    @Transactional
    public void unenrollCourse(UUID id) {
        learningRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Learning> getEnrollmentsByInstructorId(UUID instructorId) {
        return learningRepository.findByInstructorId(instructorId);
    }

    @Transactional(readOnly = true)
    public long getStudentCountByInstructorId(UUID instructorId) {
        return learningRepository.countStudentsByInstructorId(instructorId);
    }

    @Transactional(readOnly = true)
    public List<Learning> getRecentEnrollmentsByInstructorId(UUID instructorId, int limit) {
        List<Learning> enrollments = learningRepository.findRecentByInstructorId(instructorId);
        return enrollments.stream().limit(limit).toList();
    }

    @Transactional(readOnly = true)
    public List<Learning> getEnrollmentsByCourseId(UUID courseId) {
        return learningRepository.findByCourseId(courseId);
    }
}