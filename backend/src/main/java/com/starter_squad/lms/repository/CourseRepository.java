package com.starter_squad.lms.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.starter_squad.lms.entity.Course;
import com.starter_squad.lms.entity.Course.CourseStatus;
import com.starter_squad.lms.entity.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    // ==========================================
    // STATUS-BASED QUERIES
    // ==========================================
    
    List<Course> findByStatus(CourseStatus status);
    
    List<Course> findByStatusOrderByCreatedAtDesc(CourseStatus status);
    
    long countByStatus(CourseStatus status);

    // ==========================================
    // INSTRUCTOR-BASED QUERIES
    // ==========================================
    
    List<Course> findByInstructorUser(User instructor);
    
    List<Course> findByInstructorUserOrderByCreatedAtDesc(User instructor);
    
    List<Course> findByInstructorUserAndStatus(User instructor, CourseStatus status);
    
    List<Course> findByInstructorUserIdOrderByCreatedAtDesc(UUID instructorId);
    
    List<Course> findByInstructorUserIdAndStatus(UUID instructorId, CourseStatus status);
    
    long countByInstructorUser(User instructor);
    
    long countByInstructorUserId(UUID instructorId);
    
    long countByInstructorUserIdAndStatus(UUID instructorId, CourseStatus status);

    // ==========================================
    // SEARCH QUERIES
    // ==========================================
    
    @Query("SELECT c FROM Course c WHERE " +
           "LOWER(c.course_name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Course> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT c FROM Course c WHERE c.status = :status AND " +
           "(LOWER(c.course_name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Course> searchByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") CourseStatus status);

    // ==========================================
    // CATEGORY-BASED QUERIES
    // ==========================================
    
    List<Course> findByCategory(String category);
    
    List<Course> findByCategoryAndStatus(String category, CourseStatus status);
    
    @Query("SELECT DISTINCT c.category FROM Course c WHERE c.category IS NOT NULL")
    List<String> findAllCategories();

    // ==========================================
    // PUBLIC CATALOG (Published courses only)
    // ==========================================
    
    @Query("SELECT c FROM Course c WHERE c.status = 'PUBLISHED' ORDER BY c.createdAt DESC")
    List<Course> findPublishedCourses();
    
    @Query("SELECT c FROM Course c WHERE c.status = 'PUBLISHED' AND c.category = :category ORDER BY c.createdAt DESC")
    List<Course> findPublishedCoursesByCategory(@Param("category") String category);
    
    @Query("SELECT c FROM Course c WHERE c.status = 'PUBLISHED' ORDER BY c.price ASC")
    List<Course> findPublishedCoursesOrderByPriceAsc();
    
    @Query("SELECT c FROM Course c WHERE c.status = 'PUBLISHED' ORDER BY c.price DESC")
    List<Course> findPublishedCoursesOrderByPriceDesc();

    // ==========================================
    // COUNT QUERIES
    // ==========================================
    
    @Query("SELECT COUNT(c) FROM Course c WHERE c.status = 'PUBLISHED'")
    long countPublishedCourses();
    
    @Query("SELECT COUNT(c) FROM Course c WHERE c.status = 'PENDING'")
    long countPendingCourses();
}