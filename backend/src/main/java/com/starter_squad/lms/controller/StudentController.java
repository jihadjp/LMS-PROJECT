package com.starter_squad.lms.controller;

import com.starter_squad.lms.dto.EnrollRequest;
import com.starter_squad.lms.entity.Course;
import com.starter_squad.lms.entity.Course.CourseStatus;
import com.starter_squad.lms.entity.Learning;
import com.starter_squad.lms.entity.Progress;
import com.starter_squad.lms.entity.User;
import com.starter_squad.lms.repository.LearningRepository;
import com.starter_squad.lms.repository.ProgressRepository;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Student Controller for Thymeleaf Views
 * Handles Course Catalog, Enrollment, and Learning Dashboard
 */
@Controller
@RequiredArgsConstructor
public class StudentController {

    private final UserService userService;
    private final CourseService courseService;
    private final LearningService learningService;
    private final LearningRepository learningRepository;
    private final ProgressRepository progressRepository;

    // ==========================================
    // PUBLIC COURSE CATALOG (Available to all)
    // ==========================================

    @GetMapping("/courses")
    public String courseCatalog(Model model,
                               @RequestParam(required = false) String search,
                               @RequestParam(required = false) String category,
                               @RequestParam(required = false) String sort) {

        List<Course> courses;

        if (search != null && !search.isEmpty()) {
            courses = courseService.searchPublishedCourses(search);
            model.addAttribute("search", search);
        } else if (category != null && !category.isEmpty()) {
            courses = courseService.getCoursesByCategory(category);
            model.addAttribute("category", category);
        } else {
            courses = courseService.getPublishedCourses();
        }

        model.addAttribute("courses", courses);
        model.addAttribute("categories", courseService.getAllCategories());
        model.addAttribute("totalCourses", courseService.getPublishedCoursesCount());

        return "student/catalog";
    }

    @GetMapping("/courses/{id}")
    public String courseDetail(@PathVariable UUID id, 
                              Model model,
                              @AuthenticationPrincipal UserPrincipal principal) {
        Course course = courseService.getCourseById(id);

        if (course == null || course.getStatus() != CourseStatus.PUBLISHED) {
            return "redirect:/courses?error=Course not found";
        }

        model.addAttribute("course", course);

        // Check if user is enrolled (if logged in)
        boolean isEnrolled = false;
        if (principal != null) {
            List<Course> enrolledCourses = learningService.getLearningCourses(principal.getId());
            isEnrolled = enrolledCourses != null && 
                        enrolledCourses.stream().anyMatch(c -> c.getCourse_id().equals(id));
        }
        model.addAttribute("isEnrolled", isEnrolled);

        return "student/course-detail";
    }

    // ==========================================
    // STUDENT DASHBOARD (Authenticated users)
    // ==========================================

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('USER', 'STUDENT')")
    public String studentDashboard(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        User user = userService.getUserById(principal.getId());
        model.addAttribute("user", user);

        // Get enrolled courses (Learning objects with progress)
        List<Learning> enrolledCourses = learningRepository.findByUserId(principal.getId());
        model.addAttribute("enrolledCourses", enrolledCourses);
        
        // Calculate statistics
        int totalEnrolled = enrolledCourses != null ? enrolledCourses.size() : 0;
        long completedCount = enrolledCourses != null ? 
            enrolledCourses.stream().filter(Learning::isCompleted).count() : 0;
        long inProgressCount = totalEnrolled - completedCount;
        
        model.addAttribute("totalCourses", totalEnrolled);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("inProgressCount", inProgressCount);
        model.addAttribute("certificateCount", completedCount); // Certificates = completed courses
        
        // Get recommended courses (published courses user hasn't enrolled in)
        List<Course> allPublished = courseService.getPublishedCourses();
        List<UUID> enrolledCourseIds = enrolledCourses != null ? 
            enrolledCourses.stream().map(l -> l.getCourse().getCourse_id()).collect(Collectors.toList()) : 
            List.of();
        List<Course> recommendedCourses = allPublished.stream()
            .filter(c -> !enrolledCourseIds.contains(c.getCourse_id()))
            .limit(4)
            .collect(Collectors.toList());
        model.addAttribute("recommendedCourses", recommendedCourses);

        return "student/dashboard";
    }

    // ==========================================
    // LEARNING DASHBOARD (My Courses)
    // ==========================================

    @GetMapping("/my-courses")
    @PreAuthorize("hasAnyRole('USER', 'STUDENT')")
    public String myLearning(Model model, 
                            @AuthenticationPrincipal UserPrincipal principal,
                            @RequestParam(required = false) String filter) {
        User user = userService.getUserById(principal.getId());
        model.addAttribute("user", user);

        // Get enrolled courses (Learning objects with progress)
        List<Learning> allEnrolled = learningRepository.findByUserId(principal.getId());
        
        // Calculate statistics
        int totalCourses = allEnrolled != null ? allEnrolled.size() : 0;
        long completedCount = allEnrolled != null ? 
            allEnrolled.stream().filter(Learning::isCompleted).count() : 0;
        long inProgressCount = totalCourses - completedCount;
        
        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("inProgressCount", inProgressCount);
        model.addAttribute("certificateCount", completedCount);
        model.addAttribute("filter", filter);
        
        // Filter courses based on filter parameter
        List<Learning> enrolledCourses;
        if ("completed".equals(filter)) {
            enrolledCourses = allEnrolled.stream()
                .filter(Learning::isCompleted)
                .collect(Collectors.toList());
        } else if ("in-progress".equals(filter)) {
            enrolledCourses = allEnrolled.stream()
                .filter(l -> !l.isCompleted())
                .collect(Collectors.toList());
        } else {
            enrolledCourses = allEnrolled;
        }
        model.addAttribute("enrolledCourses", enrolledCourses);

        return "student/my-courses";
    }

    // ==========================================
    // ENROLLMENT
    // ==========================================

    @PostMapping("/courses/enroll/{id}")
    @PreAuthorize("hasAnyRole('USER', 'STUDENT')")
    public String enrollCourse(@PathVariable UUID id,
                              @AuthenticationPrincipal UserPrincipal principal,
                              RedirectAttributes redirectAttributes) {
        try {
            Course course = courseService.getCourseById(id);

            if (course == null || course.getStatus() != CourseStatus.PUBLISHED) {
                redirectAttributes.addFlashAttribute("error", "Course not available for enrollment.");
                return "redirect:/courses";
            }

            EnrollRequest enrollRequest = new EnrollRequest();
            enrollRequest.setUserId(principal.getId());
            enrollRequest.setCourseId(id);

            String result = learningService.enrollCourse(enrollRequest);

            if ("Enrolled successfully".equals(result)) {
                redirectAttributes.addFlashAttribute("success", 
                    "Successfully enrolled in '" + course.getCourse_name() + "'!");
                return "redirect:/my-courses";
            } else {
                redirectAttributes.addFlashAttribute("warning", result);
                return "redirect:/courses/" + id;
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to enroll: " + e.getMessage());
            return "redirect:/courses/" + id;
        }
    }

    // ==========================================
    // COURSE LEARNING PAGE
    // ==========================================

    @GetMapping("/learn/{id}")
    @PreAuthorize("hasAnyRole('USER', 'STUDENT')")
    public String learnCourse(@PathVariable UUID id,
                             Model model,
                             @AuthenticationPrincipal UserPrincipal principal,
                             RedirectAttributes redirectAttributes) {
        Course course = courseService.getCourseById(id);

        if (course == null) {
            redirectAttributes.addFlashAttribute("error", "Course not found.");
            return "redirect:/my-courses";
        }

        // Verify enrollment and get learning object
        User user = userService.getUserById(principal.getId());
        Learning learning = learningRepository.findByUserAndCourse(user, course);

        if (learning == null) {
            redirectAttributes.addFlashAttribute("error", "Please enroll in this course first.");
            return "redirect:/courses/" + id;
        }

        model.addAttribute("course", course);
        model.addAttribute("learning", learning);
        
        // Get progress
        Progress progress = progressRepository.findByUserAndCourse(user, course);
        model.addAttribute("progress", progress);

        return "student/learn";
    }
    
    // ==========================================
    // MARK COURSE AS COMPLETE
    // ==========================================
    
    @PostMapping("/learn/{id}/complete")
    @PreAuthorize("hasAnyRole('USER', 'STUDENT')")
    public String markCourseComplete(@PathVariable UUID id,
                                    @AuthenticationPrincipal UserPrincipal principal,
                                    RedirectAttributes redirectAttributes) {
        Course course = courseService.getCourseById(id);
        
        if (course == null) {
            redirectAttributes.addFlashAttribute("error", "Course not found.");
            return "redirect:/my-courses";
        }
        
        User user = userService.getUserById(principal.getId());
        Learning learning = learningRepository.findByUserAndCourse(user, course);
        
        if (learning == null) {
            redirectAttributes.addFlashAttribute("error", "You are not enrolled in this course.");
            return "redirect:/courses/" + id;
        }
        
        // Mark as completed
        learning.setCompleted(true);
        learningRepository.save(learning);
        
        // Update progress to 100%
        Progress progress = progressRepository.findByUserAndCourse(user, course);
        if (progress != null) {
            progress.setPercentage(100);
            progressRepository.save(progress);
        }
        
        redirectAttributes.addFlashAttribute("success", 
            "Congratulations! You have completed '" + course.getCourse_name() + "'!");
        return "redirect:/my-courses";
    }

    // ==========================================
    // STUDENT PROFILE
    // ==========================================

    @GetMapping("/student/profile")
    @PreAuthorize("hasAnyRole('USER', 'STUDENT')")
    public String studentProfile(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        User user = userService.getUserById(principal.getId());
        model.addAttribute("user", user);

        // Get enrolled courses (Learning objects with progress)
        List<Learning> enrolledCourses = learningRepository.findByUserId(principal.getId());
        int totalEnrolled = enrolledCourses != null ? enrolledCourses.size() : 0;
        long completedCount = enrolledCourses != null ? 
            enrolledCourses.stream().filter(Learning::isCompleted).count() : 0;

        model.addAttribute("enrolledCount", totalEnrolled);
        model.addAttribute("completedCourses", completedCount);

        return "student/profile";
    }

    @PostMapping("/student/update-profile")
    @PreAuthorize("hasAnyRole('USER', 'STUDENT')")
    public String updateStudentProfile(@AuthenticationPrincipal UserPrincipal principal,
                               @RequestParam("fullName") String fullName,
                               @RequestParam("email") String email,
                               @RequestParam(value = "mobileNumber", required = false) String mobileNumber,
                               @RequestParam(value = "profession", required = false) String profession,
                               @RequestParam(value = "location", required = false) String location,
                               @RequestParam(value = "image", required = false) MultipartFile image,
                               RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(principal.getId());
            user.setUsername(fullName);
            user.setEmail(email);
            user.setMobileNumber(mobileNumber);
            user.setProfession(profession);
            user.setLocation(location);
            
            if (image != null && !image.isEmpty()) {
                userService.updateUserProfile(principal.getId(), fullName, email, image);
            } else {
                userService.updateUserBasicInfo(principal.getId(), user);
            }
            
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile: " + e.getMessage());
        }
        return "redirect:/student/profile";
    }

    // ==========================================
    // STUDENT SETTINGS
    // ==========================================

    @GetMapping("/student/settings")
    @PreAuthorize("hasAnyRole('USER', 'STUDENT')")
    public String studentSettings(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        User user = userService.getUserById(principal.getId());
        model.addAttribute("user", user);
        return "student/settings";
    }

    @PostMapping("/student/change-password")
    @PreAuthorize("hasAnyRole('USER', 'STUDENT')")
    public String changeStudentPassword(@AuthenticationPrincipal UserPrincipal principal,
                                @RequestParam("currentPassword") String currentPassword,
                                @RequestParam("newPassword") String newPassword,
                                RedirectAttributes redirectAttributes) {
        try {
            userService.changePassword(principal.getId(), currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("success", "Password changed successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/student/settings";
    }

    // Keep old /profile endpoint for backward compatibility
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'STUDENT')")
    public String profile() {
        return "redirect:/student/profile";
    }
}
