package com.starter_squad.lms.service;

import com.starter_squad.lms.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.starter_squad.lms.entity.User;
import com.starter_squad.lms.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ==========================================
    // BASIC CRUD OPERATIONS
    // ==========================================

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    public User createUser(User user) {
        // ১. Check if email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }

        // ২. পাসওয়ার্ড এনকোড করা
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // ৩. ডিফল্ট রোল সেট করা (যদি ফর্ম থেকে রোল না আসে)
        if (user.getRole() == null) {
            user.setRole(UserRole.USER); // ডিফল্টভাবে ROLE_USER হবে
        }

        // ৪. একাউন্ট একটিভ রাখা
        user.setIsActive(true);

        return userRepository.save(user);
    }

    public User updateUser(UUID id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setDob(updatedUser.getDob());
        existingUser.setMobileNumber(updatedUser.getMobileNumber());
        existingUser.setGender(updatedUser.getGender());
        existingUser.setLocation(updatedUser.getLocation());
        existingUser.setProfession(updatedUser.getProfession());
        existingUser.setLinkedin_url(updatedUser.getLinkedin_url());
        existingUser.setGithub_url(updatedUser.getGithub_url());

        return userRepository.save(existingUser);
    }

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }

    // ==========================================
    // AUTHENTICATION & AUTHORIZATION
    // ==========================================

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Deprecated
    public User authenticateUser(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    // ==========================================
    // PROFILE UPDATE WITH IMAGE (Using profileImage byte[])
    // ==========================================

    public void updateUserProfile(UUID userId, String fullName, String email, MultipartFile image) throws IOException {
        User user = getUserById(userId);

        // Update username (since User entity uses username, not fullName)
        user.setUsername(fullName);
        user.setEmail(email);

        // Handle image upload if provided
        if (image != null && !image.isEmpty()) {
            user.setProfileImage(image.getBytes());
        }

        userRepository.save(user);
    }

    // Original method from your code
    public void updateUserProfile(MultipartFile file, UUID id) throws IOException {
        User user = getUserById(id);
        if (user == null) return;
        user.setProfileImage(file.getBytes());
        userRepository.save(user);
    }

    // ==========================================
    // PASSWORD MANAGEMENT
    // ==========================================

    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        User user = getUserById(userId);

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Validate new password
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // Update user basic info without image
    public void updateUserBasicInfo(UUID userId, User updatedUser) {
        User user = getUserById(userId);
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setMobileNumber(updatedUser.getMobileNumber());
        user.setProfession(updatedUser.getProfession());
        user.setLocation(updatedUser.getLocation());
        userRepository.save(user);
    }

    // ==========================================
    // SEARCH & FILTER
    // ==========================================

    public List<User> searchUsers(String keyword) {
        return userRepository.findByUsernameContainingOrEmailContaining(keyword, keyword);
    }

    public List<User> getUsersByRole(String roleName) {
        try {
            // Convert String to UserRole enum
            UserRole role = UserRole.valueOf(roleName.toUpperCase());
            return userRepository.findByRole(role);
        } catch (IllegalArgumentException e) {
            // If invalid role name, return empty list
            return List.of();
        }
    }

    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public List<User> getActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }

    // ==========================================
    // STATISTICS
    // ==========================================

    public long getTotalUsersCount() {
        return userRepository.count();
    }

    public List<User> getRecentUsers(int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        return userRepository.findAll(pageRequest).getContent();
    }

    public long getActiveUsersCount() {
        return userRepository.countActiveUsers();
    }

    public long getUsersCountByRole(String roleName) {
        try {
            UserRole role = UserRole.valueOf(roleName.toUpperCase());
            return userRepository.countByRole(role);
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }

    public long getUsersCountByRole(UserRole role) {
        return userRepository.countByRole(role);
    }

    // ==========================================
    // USER STATUS MANAGEMENT
    // ==========================================

    public void toggleUserStatus(UUID userId) {
        User user = getUserById(userId);
        user.setIsActive(!user.getIsActive());
        userRepository.save(user);
    }

    public void activateUser(UUID userId) {
        User user = getUserById(userId);
        user.setIsActive(true);
        userRepository.save(user);
    }

    public void deactivateUser(UUID userId) {
        User user = getUserById(userId);
        user.setIsActive(false);
        userRepository.save(user);
    }

    // ==========================================
    // INSTRUCTOR MANAGEMENT
    // ==========================================

    /**
     * Get users who have requested to become instructors (pending approval)
     * For simplicity, this returns instructors with isActive = false (pending)
     */
    public List<User> getPendingInstructors() {
        return userRepository.findByRoleAndIsActive(UserRole.INSTRUCTOR, false);
    }

    public long getPendingInstructorsCount() {
        return userRepository.countByRoleAndIsActive(UserRole.INSTRUCTOR, false);
    }

    public void approveInstructor(UUID userId) {
        User user = getUserById(userId);
        if (user.getRole() != UserRole.INSTRUCTOR) {
            throw new IllegalArgumentException("User is not an instructor");
        }
        user.setIsActive(true);
        userRepository.save(user);
    }

    public void rejectInstructor(UUID userId) {
        User user = getUserById(userId);
        // Change role back to USER and keep active
        user.setRole(UserRole.USER);
        user.setIsActive(true);
        userRepository.save(user);
    }
}