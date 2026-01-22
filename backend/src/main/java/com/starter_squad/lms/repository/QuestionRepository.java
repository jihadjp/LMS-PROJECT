package com.starter_squad.lms.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starter_squad.lms.entity.Course;
import com.starter_squad.lms.entity.Questions;

public interface QuestionRepository extends JpaRepository<Questions, UUID> {

	List<Questions> findByCourse(Course course); 
}
