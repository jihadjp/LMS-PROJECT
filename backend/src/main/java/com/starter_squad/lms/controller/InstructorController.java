package com.starter_squad.lms.controller;

import com.starter_squad.lms.entity.Course;
import com.starter_squad.lms.entity.Course.CourseStatus;
import com.starter_squad.lms.entity.Learning;
import com.starter_squad.lms.entity.User;
import com.starter_squad.lms.security.UserPrincipal;
import com.starter_squad.lms.service.CourseService;
import com.starter_squad.lms.service.LearningService;
import com.starter_squad.lms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Instructor Controller for Thymeleaf Views
 * Handles Course Creation, Course Management, and Instructor Dashboard
 */
@Controller
@RequestMapping("/instructor")
@PreAuthorize("hasRole('INSTRUCTOR')")
@RequiredArgsConstructor
public class InstructorController {

    private final UserService userService;
    private final CourseService courseService;
    private final LearningService learningService;

    // ==========================================
    // INSTRUCTOR DASHBOARD
    // ==========================================

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        User user = userService.getUserById(principal.getId());
        model.addAttribute("user", user);

        // Course statistics for instructor
        UUID instructorId = principal.getId();
        long totalCourses = courseService.getInstructorCourseCount(instructorId);
        long publishedCourses = courseService.getInstructorCourseCountByStatus(instructorId, CourseStatus.PUBLISHED);
        long pendingCourses = courseService.getInstructorCourseCountByStatus(instructorId, CourseStatus.PENDING);
        long draftCourses = courseService.getInstructorCourseCountByStatus(instructorId, CourseStatus.DRAFT);
        long rejectedCourses = courseService.getInstructorCourseCountByStatus(instructorId, CourseStatus.REJECTED);

        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("publishedCourses", publishedCourses);
        model.addAttribute("pendingCourses", pendingCourses);
        model.addAttribute("draftCourses", draftCourses);
        model.addAttribute("rejectedCourses", rejectedCourses);

        // Recent courses
        List<Course> recentCourses = courseService.getCoursesByInstructorId(instructorId);
        model.addAttribute("recentCourses", recentCourses.stream().limit(5).toList());

        // Real student count and enrollment data
        long totalStudents = learningService.getStudentCountByInstructorId(instructorId);
        model.addAttribute("totalStudents", totalStudents);
        
        // Get recent enrollments/sales
        var recentEnrollments = learningService.getRecentEnrollmentsByInstructorId(instructorId, 10);
        model.addAttribute("recentSales", recentEnrollments);
        
        // Calculate earnings from enrollments
        long monthlyEarnings = recentEnrollments.stream()
                .filter(e -> e.getCourse() != null)
                .mapToLong(e -> e.getCourse().getPrice())
                .sum();
        model.addAttribute("monthlyEarnings", monthlyEarnings);
        
        // Average rating (placeholder for now)
        model.addAttribute("averageRating", "0.0");

        return "instructor/dashboard";
    }

    // ==========================================
    // COURSE MANAGEMENT
    // ==========================================

    @GetMapping("/courses")
    public String listCourses(Model model,
                             @AuthenticationPrincipal UserPrincipal principal,
                             @RequestParam(required = false) String status) {
        UUID instructorId = principal.getId();

        if (status != null && !status.isEmpty()) {
            CourseStatus courseStatus = CourseStatus.valueOf(status.toUpperCase());
            model.addAttribute("courses", courseService.getCoursesByInstructorAndStatus(instructorId, courseStatus));
            model.addAttribute("filterStatus", status);
        } else {
            model.addAttribute("courses", courseService.getCoursesByInstructorId(instructorId));
        }

        // Statistics
        model.addAttribute("totalCourses", courseService.getInstructorCourseCount(instructorId));
        model.addAttribute("publishedCount", courseService.getInstructorCourseCountByStatus(instructorId, CourseStatus.PUBLISHED));
        model.addAttribute("pendingCount", courseService.getInstructorCourseCountByStatus(instructorId, CourseStatus.PENDING));
        model.addAttribute("draftCount", courseService.getInstructorCourseCountByStatus(instructorId, CourseStatus.DRAFT));
        model.addAttribute("rejectedCount", courseService.getInstructorCourseCountByStatus(instructorId, CourseStatus.REJECTED));

        return "instructor/courses";
    }

    @GetMapping("/courses/draft")
    public String draftCourses(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        UUID instructorId = principal.getId();
        model.addAttribute("courses", courseService.getCoursesByInstructorAndStatus(instructorId, CourseStatus.DRAFT));
        model.addAttribute("filterStatus", "DRAFT");
        model.addAttribute("pageTitle", "Draft Courses");
        addCourseStatistics(model, instructorId);
        return "instructor/courses";
    }

    @GetMapping("/courses/pending")
    public String pendingCourses(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        UUID instructorId = principal.getId();
        model.addAttribute("courses", courseService.getCoursesByInstructorAndStatus(instructorId, CourseStatus.PENDING));
        model.addAttribute("filterStatus", "PENDING");
        model.addAttribute("pageTitle", "Pending Approval");
        addCourseStatistics(model, instructorId);
        return "instructor/courses";
    }

    @GetMapping("/courses/published")
    public String publishedCourses(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        UUID instructorId = principal.getId();
        model.addAttribute("courses", courseService.getCoursesByInstructorAndStatus(instructorId, CourseStatus.PUBLISHED));
        model.addAttribute("filterStatus", "PUBLISHED");
        model.addAttribute("pageTitle", "Published Courses");
        addCourseStatistics(model, instructorId);
        return "instructor/courses";
    }

    @GetMapping("/courses/rejected")
    public String rejectedCourses(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        UUID instructorId = principal.getId();
        model.addAttribute("courses", courseService.getCoursesByInstructorAndStatus(instructorId, CourseStatus.REJECTED));
        model.addAttribute("filterStatus", "REJECTED");
        model.addAttribute("pageTitle", "Rejected Courses");
        addCourseStatistics(model, instructorId);
        return "instructor/courses";
    }

    private void addCourseStatistics(Model model, UUID instructorId) {
        model.addAttribute("totalCourses", courseService.getInstructorCourseCount(instructorId));
        model.addAttribute("publishedCount", courseService.getInstructorCourseCountByStatus(instructorId, CourseStatus.PUBLISHED));
        model.addAttribute("pendingCount", courseService.getInstructorCourseCountByStatus(instructorId, CourseStatus.PENDING));
        model.addAttribute("draftCount", courseService.getInstructorCourseCountByStatus(instructorId, CourseStatus.DRAFT));
        model.addAttribute("rejectedCount", courseService.getInstructorCourseCountByStatus(instructorId, CourseStatus.REJECTED));
    }

    // ==========================================
    // COURSE CREATION
    // ==========================================

    @GetMapping("/courses/create")
    public String createCoursePage(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("categories", courseService.getAllCategories());
        return "instructor/create-course";
    }

    @PostMapping("/courses/create")
    public String createCourse(@AuthenticationPrincipal UserPrincipal principal,
                              @RequestParam("course_name") String courseName,
                              @RequestParam("description") String description,
                              @RequestParam(value = "category", required = false) String category,
                              @RequestParam(defaultValue = "0") int price,
                              @RequestParam(value = "p_link", required = false) String pLink,
                              @RequestParam(value = "y_link", required = false) String yLink,
                              @RequestParam(value = "action", defaultValue = "draft") String action,
                              RedirectAttributes redirectAttributes) {
        try {
            User instructor = userService.getUserById(principal.getId());

            Course course = new Course();
            course.setCourse_name(courseName);
            course.setDescription(description);
            course.setCategory(category);
            course.setPrice(price);
            course.setP_link(pLink);
            course.setY_link(yLink);

            // Create course with instructor reference
            Course savedCourse = courseService.createCourse(course, instructor);

            // If "submit for approval" action, change status to PENDING
            if ("submit".equals(action)) {
                courseService.submitForApproval(savedCourse.getCourse_id());
                redirectAttributes.addFlashAttribute("success", 
                    "Course '" + courseName + "' created and submitted for approval!");
            } else {
                redirectAttributes.addFlashAttribute("success", 
                    "Course '" + courseName + "' saved as draft successfully!");
            }

            return "redirect:/instructor/courses";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create course: " + e.getMessage());
            return "redirect:/instructor/courses/create";
        }
    }

    // ==========================================
    // COURSE EDITING
    // ==========================================

    @GetMapping("/courses/edit/{id}")
    public String editCoursePage(@PathVariable UUID id, 
                                Model model,
                                @AuthenticationPrincipal UserPrincipal principal,
                                RedirectAttributes redirectAttributes) {
        Course course = courseService.getCourseById(id);

        // Verify ownership
        if (course == null || course.getInstructorUser() == null || 
            !course.getInstructorUser().getId().equals(principal.getId())) {
            redirectAttributes.addFlashAttribute("error", "Course not found or access denied.");
            return "redirect:/instructor/courses";
        }

        // Only allow editing DRAFT courses
        if (course.getStatus() != CourseStatus.DRAFT) {
            redirectAttributes.addFlashAttribute("error", "Only draft courses can be edited.");
            return "redirect:/instructor/courses";
        }

        model.addAttribute("course", course);
        model.addAttribute("categories", courseService.getAllCategories());
        return "instructor/edit-course";
    }

    @PostMapping("/courses/edit/{id}")
    public String updateCourse(@PathVariable UUID id,
                              @AuthenticationPrincipal UserPrincipal principal,
                              @RequestParam("course_name") String courseName,
                              @RequestParam("description") String description,
                              @RequestParam(value = "category", required = false) String category,
                              @RequestParam(defaultValue = "0") int price,
                              @RequestParam(value = "p_link", required = false) String pLink,
                              @RequestParam(value = "y_link", required = false) String yLink,
                              @RequestParam(value = "action", defaultValue = "save") String action,
                              RedirectAttributes redirectAttributes) {
        try {
            Course existingCourse = courseService.getCourseById(id);

            // Verify ownership
            if (existingCourse == null || existingCourse.getInstructorUser() == null ||
                !existingCourse.getInstructorUser().getId().equals(principal.getId())) {
                redirectAttributes.addFlashAttribute("error", "Course not found or access denied.");
                return "redirect:/instructor/courses";
            }

            // Update fields
            existingCourse.setCourse_name(courseName);
            existingCourse.setDescription(description);
            existingCourse.setCategory(category);
            existingCourse.setPrice(price);
            existingCourse.setP_link(pLink);
            existingCourse.setY_link(yLink);

            courseService.updateCourse(id, existingCourse);

            // If "submit for approval" action
            if ("submit".equals(action) && existingCourse.getStatus() == CourseStatus.DRAFT) {
                courseService.submitForApproval(id);
                redirectAttributes.addFlashAttribute("success", 
                    "Course updated and submitted for approval!");
            } else {
                redirectAttributes.addFlashAttribute("success", "Course updated successfully!");
            }

            return "redirect:/instructor/courses";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update course: " + e.getMessage());
            return "redirect:/instructor/courses/edit/" + id;
        }
    }

    @GetMapping("/courses/view/{id}")
    public String viewCourse(@PathVariable UUID id, 
                            Model model,
                            @AuthenticationPrincipal UserPrincipal principal,
                            RedirectAttributes redirectAttributes) {
        Course course = courseService.getCourseById(id);

        // Verify ownership
        if (course == null || course.getInstructorUser() == null ||
            !course.getInstructorUser().getId().equals(principal.getId())) {
            redirectAttributes.addFlashAttribute("error", "Course not found or access denied.");
            return "redirect:/instructor/courses";
        }

        model.addAttribute("course", course);
        return "instructor/course-detail";
    }

    // ==========================================
    // COURSE ACTIONS
    // ==========================================

    @PostMapping("/courses/submit/{id}")
    public String submitForApproval(@PathVariable UUID id,
                                   @AuthenticationPrincipal UserPrincipal principal,
                                   RedirectAttributes redirectAttributes) {
        try {
            Course course = courseService.getCourseById(id);

            // Verify ownership
            if (course == null || course.getInstructorUser() == null ||
                !course.getInstructorUser().getId().equals(principal.getId())) {
                redirectAttributes.addFlashAttribute("error", "Course not found or access denied.");
                return "redirect:/instructor/courses";
            }

            Course submitted = courseService.submitForApproval(id);
            if (submitted != null) {
                redirectAttributes.addFlashAttribute("success", 
                    "Course '" + submitted.getCourse_name() + "' submitted for approval!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Could not submit course. Only draft courses can be submitted.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to submit course: " + e.getMessage());
        }
        return "redirect:/instructor/courses";
    }

    @PostMapping("/courses/resubmit/{id}")
    public String resubmitCourse(@PathVariable UUID id,
                                @AuthenticationPrincipal UserPrincipal principal,
                                RedirectAttributes redirectAttributes) {
        try {
            Course course = courseService.getCourseById(id);

            // Verify ownership
            if (course == null || course.getInstructorUser() == null ||
                !course.getInstructorUser().getId().equals(principal.getId())) {
                redirectAttributes.addFlashAttribute("error", "Course not found or access denied.");
                return "redirect:/instructor/courses";
            }

            Course resubmitted = courseService.resubmitCourse(id);
            if (resubmitted != null) {
                redirectAttributes.addFlashAttribute("success", 
                    "Course '" + resubmitted.getCourse_name() + "' resubmitted for approval!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Could not resubmit course. Only rejected courses can be resubmitted.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to resubmit course: " + e.getMessage());
        }
        return "redirect:/instructor/courses";
    }

    @PostMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable UUID id,
                              @AuthenticationPrincipal UserPrincipal principal,
                              RedirectAttributes redirectAttributes) {
        try {
            Course course = courseService.getCourseById(id);

            // Verify ownership
            if (course == null || course.getInstructorUser() == null ||
                !course.getInstructorUser().getId().equals(principal.getId())) {
                redirectAttributes.addFlashAttribute("error", "Course not found or access denied.");
                return "redirect:/instructor/courses";
            }

            // Only allow deleting DRAFT courses
            if (course.getStatus() != CourseStatus.DRAFT) {
                redirectAttributes.addFlashAttribute("error", "Only draft courses can be deleted.");
                return "redirect:/instructor/courses";
            }

            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("success", "Course deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete course: " + e.getMessage());
        }
        return "redirect:/instructor/courses";
    }

    // ==========================================
    // ENROLLED STUDENTS
    // ==========================================

    @GetMapping("/students")
    public String enrolledStudents(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        UUID instructorId = principal.getId();
        
        // Get all enrollments for instructor's courses
        var enrollments = learningService.getEnrollmentsByInstructorId(instructorId);
        model.addAttribute("enrollments", enrollments);
        
        // Get unique students count
        long totalStudents = learningService.getStudentCountByInstructorId(instructorId);
        model.addAttribute("totalStudents", totalStudents);
        
        // Count completed and in-progress
        long completedCount = enrollments.stream().filter(Learning::isCompleted).count();
        long inProgressCount = enrollments.size() - completedCount;
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("inProgressCount", inProgressCount);
        
        return "instructor/students";
    }

    // ==========================================
    // EARNINGS
    // ==========================================

    @GetMapping("/earnings")
    public String earnings(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        // TODO: Implement earnings view when payment tracking is added
        model.addAttribute("totalEarnings", "৳0");
        model.addAttribute("monthlyEarnings", "৳0");
        model.addAttribute("transactions", java.util.Collections.emptyList());
        return "instructor/earnings";
    }

    // ==========================================
    // PROFILE & SETTINGS
    // ==========================================

    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        User user = userService.getUserById(principal.getId());
        model.addAttribute("user", user);
        model.addAttribute("totalCourses", courseService.getInstructorCourseCount(principal.getId()));
        model.addAttribute("publishedCourses", courseService.getInstructorCourseCountByStatus(principal.getId(), CourseStatus.PUBLISHED));
        model.addAttribute("totalStudents", learningService.getStudentCountByInstructorId(principal.getId()));
        return "instructor/profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@AuthenticationPrincipal UserPrincipal principal,
                               @RequestParam("fullName") String fullName,
                               @RequestParam("email") String email,
                               @RequestParam(value = "image", required = false) MultipartFile image,
                               RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserProfile(principal.getId(), fullName, email, image);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile: " + e.getMessage());
        }
        return "redirect:/instructor/profile";
    }

    @GetMapping("/settings")
    public String settings() {
        return "instructor/settings";
    }

    @GetMapping("/profile/change-password")
    public String changePasswordPage() {
        return "instructor/settings";
    }

    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal UserPrincipal principal,
                                @RequestParam("currentPassword") String currentPassword,
                                @RequestParam("newPassword") String newPassword,
                                RedirectAttributes redirectAttributes) {
        try {
            userService.changePassword(principal.getId(), currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("success", "Password changed successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/instructor/settings";
    }
}
