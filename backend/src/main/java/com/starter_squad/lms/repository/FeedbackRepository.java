package com.starter_squad.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starter_squad.lms.entity.Feedback;

import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
}
