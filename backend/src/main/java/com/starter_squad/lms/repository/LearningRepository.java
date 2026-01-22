package com.starter_squad.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.starter_squad.lms.entity.Course;
import com.starter_squad.lms.entity.Learning;
import com.starter_squad.lms.entity.User;

import java.util.List;
import java.util.UUID;

public interface LearningRepository extends JpaRepository<Learning, UUID> {

	Learning findByUserAndCourse(User user, Course course);
	
	@Query("SELECT l FROM Learning l WHERE l.user.id = :userId")
	List<Learning> findByUserId(@Param("userId") UUID userId);
	
	@Query("SELECT l FROM Learning l WHERE l.user.id = :userId AND l.completed = :completed")
	List<Learning> findByUserIdAndCompleted(@Param("userId") UUID userId, @Param("completed") boolean completed);
	
	@Query("SELECT l FROM Learning l WHERE l.course.course_id = :courseId")
	List<Learning> findByCourseId(@Param("courseId") UUID courseId);
	
	@Query("SELECT COUNT(l) FROM Learning l WHERE l.user.id = :userId")
	long countByUserId(@Param("userId") UUID userId);
	
	@Query("SELECT COUNT(l) FROM Learning l WHERE l.user.id = :userId AND l.completed = :completed")
	long countByUserIdAndCompleted(@Param("userId") UUID userId, @Param("completed") boolean completed);
	
	// Get enrollments for instructor's courses
	@Query("SELECT l FROM Learning l WHERE l.course.instructorUser.id = :instructorId ORDER BY l.enrollmentDate DESC")
	List<Learning> findByInstructorId(@Param("instructorId") UUID instructorId);
	
	// Count students enrolled in instructor's courses
	@Query("SELECT COUNT(DISTINCT l.user.id) FROM Learning l WHERE l.course.instructorUser.id = :instructorId")
	long countStudentsByInstructorId(@Param("instructorId") UUID instructorId);
	
	// Get recent enrollments for instructor's courses
	@Query("SELECT l FROM Learning l WHERE l.course.instructorUser.id = :instructorId ORDER BY l.enrollmentDate DESC")
	List<Learning> findRecentByInstructorId(@Param("instructorId") UUID instructorId);
}