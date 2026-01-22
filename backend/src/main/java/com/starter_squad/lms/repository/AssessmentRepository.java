package com.starter_squad.lms.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starter_squad.lms.entity.Assessment;
import com.starter_squad.lms.entity.Course;
import com.starter_squad.lms.entity.User;

public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {

    List<Assessment> findByUserAndCourse(User user, Course course);

	List<Assessment> findByUser(User user);
}
