package com.starter_squad.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starter_squad.lms.entity.Course;
import com.starter_squad.lms.entity.Progress;
import com.starter_squad.lms.entity.User;

import java.util.UUID;

public interface ProgressRepository extends JpaRepository<Progress, UUID> {

	Progress findByUserAndCourse(User user, Course course);
}
