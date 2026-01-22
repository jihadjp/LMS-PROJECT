package com.starter_squad.lms.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Learning {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID learning_id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    
    @Column(name = "completed")
    private boolean completed = false;
    
    @Column(name = "enrollment_date")
    private LocalDateTime enrollmentDate = LocalDateTime.now();
    
    @Column(name = "completion_date")
    private LocalDateTime completionDate;
    
    @ManyToOne
    @JoinColumn(name = "progress_id")
    private Progress progress;
    
    // Helper method to set completed and track completion date
    public void setCompleted(boolean completed) {
        this.completed = completed;
        if (completed && this.completionDate == null) {
            this.completionDate = LocalDateTime.now();
        }
    }
}
