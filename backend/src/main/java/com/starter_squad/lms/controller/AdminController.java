package com.starter_squad.lms.controller;

import com.starter_squad.lms.entity.Course;
import com.starter_squad.lms.entity.Course.CourseStatus;
import com.starter_squad.lms.entity.User;
import com.starter_squad.lms.enums.UserRole;
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
import java.util.UUID;

/**
 * Admin Controller for Thymeleaf Views
 * Handles User Management, Course Approval, and Admin Dashboard
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final CourseService courseService;
    private final LearningService learningService;

    // ==========================================
    // ADMIN DASHBOARD
    // ==========================================

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        // User statistics
        model.addAttribute("totalUsers", userService.getTotalUsersCount());
        model.addAttribute("activeUsers", userService.getActiveUsersCount());
        model.addAttribute("totalInstructors", userService.getUsersCountByRole(UserRole.INSTRUCTOR));
        model.addAttribute("totalStudents", userService.getUsersCountByRole(UserRole.USER));

        // Course statistics
        model.addAttribute("totalCourses", courseService.getTotalCoursesCount());
        model.addAttribute("publishedCourses", courseService.getPublishedCoursesCount());
        model.addAttribute("pendingCoursesCount", courseService.getPendingCoursesCount());
        model.addAttribute("draftCourses", courseService.getCourseCountByStatus(CourseStatus.DRAFT));
        model.addAttribute("rejectedCourses", courseService.getCourseCountByStatus(CourseStatus.REJECTED));

        // Recent data for dashboard widgets
        model.addAttribute("pendingCourses", courseService.getPendingCourses());
        model.addAttribute("recentUsers", userService.getRecentUsers(5));

        // Enrollment statistics and revenue
        var enrollments = learningService.getEnrollments();
        model.addAttribute("totalEnrollments", enrollments.size());
        
        // Calculate total revenue
        long totalRevenue = enrollments.stream()
            .mapToLong(e -> e.getCourse() != null ? e.getCourse().getPrice() : 0)
            .sum();
        model.addAttribute("totalRevenue", "$" + totalRevenue);

        return "admin/dashboard";
    }

    // ==========================================
    // USER MANAGEMENT
    // ==========================================

    @GetMapping("/users")
    public String listUsers(Model model,
                           @RequestParam(required = false) String keyword,
                           @RequestParam(required = false) String role,
                           @RequestParam(required = false) String status) {

        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("users", userService.searchUsers(keyword));
            model.addAttribute("searchKeyword", keyword);
        } else if (role != null && !role.isEmpty()) {
            model.addAttribute("users", userService.getUsersByRole(role));
            model.addAttribute("filterRole", role);
        } else {
            model.addAttribute("users", userService.getAllUsers());
        }

        // Statistics for the page header
        model.addAttribute("totalUsers", userService.getTotalUsersCount());
        model.addAttribute("activeUsers", userService.getActiveUsersCount());
        model.addAttribute("totalInstructors", userService.getUsersCountByRole(UserRole.INSTRUCTOR));
        model.addAttribute("totalStudents", userService.getUsersCountByRole(UserRole.USER));

        return "admin/users";
    }

    @GetMapping("/users/instructors")
    public String listInstructors(Model model) {
        model.addAttribute("users", userService.getUsersByRole(UserRole.INSTRUCTOR));
        model.addAttribute("pageTitle", "Instructors");
        model.addAttribute("filterRole", "INSTRUCTOR");
        model.addAttribute("totalUsers", userService.getUsersCountByRole(UserRole.INSTRUCTOR));
        return "admin/users";
    }

    @GetMapping("/users/students")
    public String listStudents(Model model) {
        model.addAttribute("users", userService.getUsersByRole(UserRole.USER));
        model.addAttribute("pageTitle", "Students");
        model.addAttribute("filterRole", "USER");
        model.addAttribute("totalUsers", userService.getUsersCountByRole(UserRole.USER));
        return "admin/users";
    }

    @PostMapping("/users/toggle-status/{id}")
    public String toggleUserStatus(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            userService.toggleUserStatus(id);
            redirectAttributes.addFlashAttribute("success", "User status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update user status: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/activate/{id}")
    public String activateUser(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            userService.activateUser(id);
            redirectAttributes.addFlashAttribute("success", "User activated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to activate user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/deactivate/{id}")
    public String deactivateUser(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            userService.deactivateUser(id);
            redirectAttributes.addFlashAttribute("success", "User deactivated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to deactivate user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/change-role/{id}")
    public String changeUserRole(@PathVariable UUID id,
                                @RequestParam String newRole,
                                RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(id);
            UserRole role = UserRole.valueOf(newRole.toUpperCase());
            user.setRole(role);
            userService.updateUser(id, user);
            redirectAttributes.addFlashAttribute("success", "User role changed to " + role + " successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to change user role: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // ==========================================
    // COURSE MANAGEMENT & APPROVAL
    // ==========================================

    @GetMapping("/courses")
    public String listCourses(Model model,
                             @RequestParam(required = false) String keyword,
                             @RequestParam(required = false) String category,
                             @RequestParam(required = false) String status) {

        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("courses", courseService.searchCourses(keyword));
        } else if (status != null && !status.isEmpty()) {
            CourseStatus courseStatus = CourseStatus.valueOf(status.toUpperCase());
            model.addAttribute("courses", courseService.getCoursesByStatus(courseStatus));
        } else {
            model.addAttribute("courses", courseService.getAllCourses());
        }

        // Statistics
        model.addAttribute("totalCourses", courseService.getTotalCoursesCount());
        model.addAttribute("publishedCourses", courseService.getPublishedCoursesCount());
        model.addAttribute("pendingCourses", courseService.getPendingCoursesCount());
        model.addAttribute("draftCourses", courseService.getCourseCountByStatus(CourseStatus.DRAFT));
        model.addAttribute("rejectedCourses", courseService.getCourseCountByStatus(CourseStatus.REJECTED));

        // Categories for filter
        model.addAttribute("categories", courseService.getAllCategories());

        return "admin/courses";
    }

    @GetMapping("/courses/pending")
    public String pendingCourses(Model model) {
        model.addAttribute("pendingCourses", courseService.getPendingCourses());
        model.addAttribute("pendingCount", courseService.getPendingCoursesCount());
        return "admin/pending-courses";
    }

    @GetMapping("/courses/published")
    public String publishedCourses(Model model) {
        model.addAttribute("courses", courseService.getPublishedCourses());
        model.addAttribute("pageTitle", "Published Courses");
        model.addAttribute("totalCourses", courseService.getPublishedCoursesCount());
        return "admin/courses";
    }

    @GetMapping("/courses/rejected")
    public String rejectedCourses(Model model) {
        model.addAttribute("courses", courseService.getRejectedCourses());
        model.addAttribute("pageTitle", "Rejected Courses");
        model.addAttribute("totalCourses", courseService.getCourseCountByStatus(CourseStatus.REJECTED));
        return "admin/courses";
    }

    @GetMapping("/courses/view/{id}")
    public String viewCourse(@PathVariable UUID id, Model model) {
        Course course = courseService.getCourseById(id);
        if (course == null) {
            return "redirect:/admin/courses?error=Course not found";
        }
        model.addAttribute("course", course);
        return "admin/course-detail";
    }

    @PostMapping("/courses/approve/{id}")
    public String approveCourse(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            Course course = courseService.approveCourse(id);
            if (course != null) {
                redirectAttributes.addFlashAttribute("success", 
                    "Course '" + course.getCourse_name() + "' has been approved and published!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Course not found or cannot be approved.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to approve course: " + e.getMessage());
        }
        return "redirect:/admin/courses/pending";
    }

    @PostMapping("/courses/reject/{id}")
    public String rejectCourse(@PathVariable UUID id,
                              @RequestParam(required = false) String reason,
                              RedirectAttributes redirectAttributes) {
        try {
            String rejectionReason = (reason != null && !reason.isEmpty()) ? reason : "Course does not meet quality standards.";
            Course course = courseService.rejectCourse(id, rejectionReason);
            if (course != null) {
                redirectAttributes.addFlashAttribute("success", 
                    "Course '" + course.getCourse_name() + "' has been rejected.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Course not found or cannot be rejected.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to reject course: " + e.getMessage());
        }
        return "redirect:/admin/courses/pending";
    }

    @PostMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("success", "Course deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete course: " + e.getMessage());
        }
        return "redirect:/admin/courses";
    }

    // ==========================================
    // ADMIN PROFILE & SETTINGS
    // ==========================================

    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        User user = userService.getUserById(principal.getId());
        model.addAttribute("user", user);
        return "admin/profile";
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
        return "redirect:/admin/profile";
    }

    @GetMapping("/settings")
    public String settings() {
        return "admin/settings";
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
        return "redirect:/admin/settings";
    }

    @GetMapping("/profile/change-password")
    public String changePasswordPage(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        User user = userService.getUserById(principal.getId());
        model.addAttribute("user", user);
        return "admin/change-password";
    }

    // ==========================================
    // COURSE PREVIEW
    // ==========================================

    @GetMapping("/courses/preview/{id}")
    public String previewCourse(@PathVariable UUID id, Model model) {
        Course course = courseService.getCourseById(id);
        if (course == null) {
            return "redirect:/admin/courses?error=Course not found";
        }
        model.addAttribute("course", course);
        // Get enrolled students count
        model.addAttribute("enrolledCount", course.getEnrollmentCount());
        return "admin/course-preview";
    }

    // ==========================================
    // INSTRUCTOR MANAGEMENT
    // ==========================================

    @GetMapping("/instructors/pending")
    public String pendingInstructors(Model model) {
        // Get users who have requested to become instructors (pending approval)
        model.addAttribute("pendingInstructors", userService.getPendingInstructors());
        model.addAttribute("totalPending", userService.getPendingInstructorsCount());
        return "admin/pending-instructors";
    }

    @PostMapping("/instructors/approve/{id}")
    public String approveInstructor(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            userService.approveInstructor(id);
            redirectAttributes.addFlashAttribute("success", "Instructor approved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to approve instructor: " + e.getMessage());
        }
        return "redirect:/admin/instructors/pending";
    }

    @PostMapping("/instructors/reject/{id}")
    public String rejectInstructor(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            userService.rejectInstructor(id);
            redirectAttributes.addFlashAttribute("success", "Instructor request rejected.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to reject instructor: " + e.getMessage());
        }
        return "redirect:/admin/instructors/pending";
    }

    // ==========================================
    // PAYMENTS MANAGEMENT
    // ==========================================

    @GetMapping("/payments/transactions")
    public String paymentTransactions(Model model) {
        // For now, showing enrollment-based transactions
        model.addAttribute("transactions", learningService.getEnrollments());
        model.addAttribute("totalTransactions", learningService.getEnrollments().size());
        return "admin/transactions";
    }

    @GetMapping("/payments/verification")
    public String paymentVerification(Model model) {
        // Placeholder for payment verification
        model.addAttribute("pendingPayments", learningService.getEnrollments());
        return "admin/payment-verification";
    }

    // ==========================================
    // REPORTS
    // ==========================================

    @GetMapping("/reports/users")
    public String userReports(Model model) {
        // User statistics
        model.addAttribute("totalUsers", userService.getTotalUsersCount());
        model.addAttribute("activeUsers", userService.getActiveUsersCount());
        model.addAttribute("totalInstructors", userService.getUsersCountByRole(UserRole.INSTRUCTOR));
        model.addAttribute("totalStudents", userService.getUsersCountByRole(UserRole.USER));
        model.addAttribute("totalAdmins", userService.getUsersCountByRole(UserRole.ADMIN));
        
        // Recent users
        model.addAttribute("recentUsers", userService.getRecentUsers(10));
        
        // All users for table
        model.addAttribute("users", userService.getAllUsers());
        
        return "admin/reports-users";
    }

    @GetMapping("/reports/revenue")
    public String revenueReports(Model model) {
        // Course statistics
        model.addAttribute("totalCourses", courseService.getTotalCoursesCount());
        model.addAttribute("publishedCourses", courseService.getPublishedCoursesCount());
        
        // Enrollment statistics
        model.addAttribute("totalEnrollments", learningService.getEnrollments().size());
        
        // Calculate revenue (simplified - sum of course prices for enrolled courses)
        long totalRevenue = learningService.getEnrollments().stream()
            .mapToLong(e -> e.getCourse() != null ? e.getCourse().getPrice() : 0)
            .sum();
        model.addAttribute("totalRevenue", totalRevenue);
        
        // Recent enrollments
        model.addAttribute("recentEnrollments", learningService.getEnrollments());
        
        // All courses for table
        model.addAttribute("courses", courseService.getPublishedCourses());
        
        return "admin/reports-revenue";
    }
}
