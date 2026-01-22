package com.starter_squad.lms.entity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "course_id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID course_id;

    @JsonProperty("course_name")
    private String course_name;

    private int price;

    private String instructor;

    // Instructor reference (for linking courses to instructor users)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    @JsonIgnore
    private User instructorUser;

    private String description;

    private String p_link;

    private String y_link;

    private String category;

    // Course status: DRAFT, PENDING, PUBLISHED, REJECTED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status = CourseStatus.DRAFT;

    // Rejection reason (if admin rejects the course)
    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Feedback> feedbacks;
    
    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<Questions> questions;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<Learning> enrollments;

    // Helper method to get enrollment count
    public int getEnrollmentCount() {
        return enrollments != null ? enrollments.size() : 0;
    }

    // Course Status Enum
    public enum CourseStatus {
        DRAFT,      // Instructor is still editing
        PENDING,    // Submitted for admin approval
        PUBLISHED,  // Approved and visible to students
        REJECTED    // Rejected by admin
    }
}
