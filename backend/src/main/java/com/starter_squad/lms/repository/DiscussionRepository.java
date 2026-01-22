package com.starter_squad.lms.repository;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starter_squad.lms.entity.Course;
import com.starter_squad.lms.entity.Discussion;

public interface DiscussionRepository extends JpaRepository<Discussion, UUID> {

    List<Discussion> findByCourse(Course course);
}
