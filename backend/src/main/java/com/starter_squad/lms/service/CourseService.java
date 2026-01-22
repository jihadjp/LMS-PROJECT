package com.starter_squad.lms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starter_squad.lms.entity.Course;
import com.starter_squad.lms.entity.Course.CourseStatus;
import com.starter_squad.lms.entity.User;
import com.starter_squad.lms.repository.CourseRepository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;

    // ==========================================
    // BASIC CRUD OPERATIONS
    // ==========================================

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(UUID id) {
        return courseRepository.findById(id).orElse(null);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Transactional
    public Course createCourse(Course course, User instructor) {
        course.setInstructorUser(instructor);
        course.setInstructor(instructor.getUsername());
        course.setStatus(CourseStatus.DRAFT);
        return courseRepository.save(course);
    }

    public Course updateCourse(UUID id, Course updatedCourse) {
        Course existingCourse = courseRepository.findById(id).orElse(null);
        if (existingCourse != null) {
            existingCourse.setCourse_name(updatedCourse.getCourse_name());
            existingCourse.setDescription(updatedCourse.getDescription());
            existingCourse.setP_link(updatedCourse.getP_link());
            existingCourse.setPrice(updatedCourse.getPrice());
            existingCourse.setInstructor(updatedCourse.getInstructor());
            existingCourse.setY_link(updatedCourse.getY_link());
            existingCourse.setCategory(updatedCourse.getCategory());
            return courseRepository.save(existingCourse);
        }
        return null;
    }

    public void deleteCourse(UUID id) {
        courseRepository.deleteById(id);
    }

    // ==========================================
    // STATUS-BASED QUERIES
    // ==========================================

    public List<Course> getCoursesByStatus(CourseStatus status) {
        return courseRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    public List<Course> getPendingCourses() {
        return courseRepository.findByStatusOrderByCreatedAtDesc(CourseStatus.PENDING);
    }

    public List<Course> getPublishedCourses() {
        return courseRepository.findPublishedCourses();
    }

    public List<Course> getDraftCourses() {
        return courseRepository.findByStatusOrderByCreatedAtDesc(CourseStatus.DRAFT);
    }

    public List<Course> getRejectedCourses() {
        return courseRepository.findByStatusOrderByCreatedAtDesc(CourseStatus.REJECTED);
    }

    // ==========================================
    // INSTRUCTOR-SPECIFIC QUERIES
    // ==========================================

    public List<Course> getCoursesByInstructor(User instructor) {
        return courseRepository.findByInstructorUserOrderByCreatedAtDesc(instructor);
    }

    public List<Course> getCoursesByInstructorId(UUID instructorId) {
        return courseRepository.findByInstructorUserIdOrderByCreatedAtDesc(instructorId);
    }

    public List<Course> getCoursesByInstructorAndStatus(UUID instructorId, CourseStatus status) {
        return courseRepository.findByInstructorUserIdAndStatus(instructorId, status);
    }

    public long getInstructorCourseCount(UUID instructorId) {
        return courseRepository.countByInstructorUserId(instructorId);
    }

    public long getInstructorCourseCountByStatus(UUID instructorId, CourseStatus status) {
        return courseRepository.countByInstructorUserIdAndStatus(instructorId, status);
    }

    // ==========================================
    // COURSE APPROVAL WORKFLOW
    // ==========================================

    @Transactional
    public Course submitForApproval(UUID courseId) {
        Course course = getCourseById(courseId);
        if (course != null && course.getStatus() == CourseStatus.DRAFT) {
            course.setStatus(CourseStatus.PENDING);
            return courseRepository.save(course);
        }
        return null;
    }

    @Transactional
    public Course approveCourse(UUID courseId) {
        Course course = getCourseById(courseId);
        if (course != null && course.getStatus() == CourseStatus.PENDING) {
            course.setStatus(CourseStatus.PUBLISHED);
            course.setRejectionReason(null);
            return courseRepository.save(course);
        }
        return null;
    }

    @Transactional
    public Course rejectCourse(UUID courseId, String reason) {
        Course course = getCourseById(courseId);
        if (course != null && course.getStatus() == CourseStatus.PENDING) {
            course.setStatus(CourseStatus.REJECTED);
            course.setRejectionReason(reason);
            return courseRepository.save(course);
        }
        return null;
    }

    @Transactional
    public Course resubmitCourse(UUID courseId) {
        Course course = getCourseById(courseId);
        if (course != null && course.getStatus() == CourseStatus.REJECTED) {
            course.setStatus(CourseStatus.PENDING);
            course.setRejectionReason(null);
            return courseRepository.save(course);
        }
        return null;
    }

    // ==========================================
    // SEARCH & FILTER
    // ==========================================

    public List<Course> searchCourses(String keyword) {
        return courseRepository.searchByKeyword(keyword);
    }

    public List<Course> searchPublishedCourses(String keyword) {
        return courseRepository.searchByKeywordAndStatus(keyword, CourseStatus.PUBLISHED);
    }

    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findByCategoryAndStatus(category, CourseStatus.PUBLISHED);
    }

    public List<String> getAllCategories() {
        return courseRepository.findAllCategories();
    }

    // ==========================================
    // STATISTICS
    // ==========================================

    public long getTotalCoursesCount() {
        return courseRepository.count();
    }

    public long getPublishedCoursesCount() {
        return courseRepository.countPublishedCourses();
    }

    public long getPendingCoursesCount() {
        return courseRepository.countPendingCourses();
    }

    public long getCourseCountByStatus(CourseStatus status) {
        return courseRepository.countByStatus(status);
    }
}
