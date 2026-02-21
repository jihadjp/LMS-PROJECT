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

    // ✅ @Transactional যোগ — Course.enrollments lazy collection এর জন্য
    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Course getCourseById(UUID id) {
        return courseRepository.findById(id).orElse(null);
    }

    @Transactional
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

    @Transactional
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

    @Transactional
    public void deleteCourse(UUID id) {
        courseRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Course> getCoursesByStatus(CourseStatus status) {
        return courseRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    @Transactional(readOnly = true)
    public List<Course> getPendingCourses() {
        return courseRepository.findByStatusOrderByCreatedAtDesc(CourseStatus.PENDING);
    }

    @Transactional(readOnly = true)
    public List<Course> getPublishedCourses() {
        return courseRepository.findPublishedCourses();
    }

    @Transactional(readOnly = true)
    public List<Course> getDraftCourses() {
        return courseRepository.findByStatusOrderByCreatedAtDesc(CourseStatus.DRAFT);
    }

    @Transactional(readOnly = true)
    public List<Course> getRejectedCourses() {
        return courseRepository.findByStatusOrderByCreatedAtDesc(CourseStatus.REJECTED);
    }

    @Transactional(readOnly = true)
    public List<Course> getCoursesByInstructor(User instructor) {
        return courseRepository.findByInstructorUserOrderByCreatedAtDesc(instructor);
    }

    @Transactional(readOnly = true)
    public List<Course> getCoursesByInstructorId(UUID instructorId) {
        return courseRepository.findByInstructorUserIdOrderByCreatedAtDesc(instructorId);
    }

    @Transactional(readOnly = true)
    public List<Course> getCoursesByInstructorAndStatus(UUID instructorId, CourseStatus status) {
        return courseRepository.findByInstructorUserIdAndStatus(instructorId, status);
    }

    @Transactional(readOnly = true)
    public long getInstructorCourseCount(UUID instructorId) {
        return courseRepository.countByInstructorUserId(instructorId);
    }

    @Transactional(readOnly = true)
    public long getInstructorCourseCountByStatus(UUID instructorId, CourseStatus status) {
        return courseRepository.countByInstructorUserIdAndStatus(instructorId, status);
    }

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

    @Transactional(readOnly = true)
    public List<Course> searchCourses(String keyword) {
        return courseRepository.searchByKeyword(keyword);
    }

    @Transactional(readOnly = true)
    public List<Course> searchPublishedCourses(String keyword) {
        return courseRepository.searchByKeywordAndStatus(keyword, CourseStatus.PUBLISHED);
    }

    @Transactional(readOnly = true)
    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findByCategoryAndStatus(category, CourseStatus.PUBLISHED);
    }

    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        return courseRepository.findAllCategories();
    }

    @Transactional(readOnly = true)
    public long getTotalCoursesCount() {
        return courseRepository.count();
    }

    @Transactional(readOnly = true)
    public long getPublishedCoursesCount() {
        return courseRepository.countPublishedCourses();
    }

    @Transactional(readOnly = true)
    public long getPendingCoursesCount() {
        return courseRepository.countPendingCourses();
    }

    @Transactional(readOnly = true)
    public long getCourseCountByStatus(CourseStatus status) {
        return courseRepository.countByStatus(status);
    }
}