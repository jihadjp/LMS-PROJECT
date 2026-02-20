package com.starter_squad.lms.service;

import com.starter_squad.lms.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    // ✅ @Transactional — learningCourses lazy collection এর জন্য
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(UserRole.USER);
        }
        user.setIsActive(true);
        return userRepository.save(user);
    }

    @Transactional
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

    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Deprecated
    public User authenticateUser(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Transactional
    public void updateUserProfile(UUID userId, String fullName, String email, MultipartFile image) throws IOException {
        User user = getUserById(userId);
        user.setUsername(fullName);
        user.setEmail(email);
        if (image != null && !image.isEmpty()) {
            user.setProfileImage(image.getBytes());
        }
        userRepository.save(user);
    }

    @Transactional
    public void updateUserProfile(MultipartFile file, UUID id) throws IOException {
        User user = getUserById(id);
        if (user == null) return;
        user.setProfileImage(file.getBytes());
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        User user = getUserById(userId);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void updateUserBasicInfo(UUID userId, User updatedUser) {
        User user = getUserById(userId);
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setMobileNumber(updatedUser.getMobileNumber());
        user.setProfession(updatedUser.getProfession());
        user.setLocation(updatedUser.getLocation());
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> searchUsers(String keyword) {
        return userRepository.findByUsernameContainingOrEmailContaining(keyword, keyword);
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByRole(String roleName) {
        try {
            UserRole role = UserRole.valueOf(roleName.toUpperCase());
            return userRepository.findByRole(role);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    @Transactional(readOnly = true)
    public List<User> getActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public long getTotalUsersCount() {
        return userRepository.count();
    }

    @Transactional(readOnly = true)
    public List<User> getRecentUsers(int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        return userRepository.findAll(pageRequest).getContent();
    }

    @Transactional(readOnly = true)
    public long getActiveUsersCount() {
        return userRepository.countActiveUsers();
    }

    @Transactional(readOnly = true)
    public long getUsersCountByRole(String roleName) {
        try {
            UserRole role = UserRole.valueOf(roleName.toUpperCase());
            return userRepository.countByRole(role);
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }

    @Transactional(readOnly = true)
    public long getUsersCountByRole(UserRole role) {
        return userRepository.countByRole(role);
    }

    @Transactional
    public void toggleUserStatus(UUID userId) {
        User user = getUserById(userId);
        user.setIsActive(!user.getIsActive());
        userRepository.save(user);
    }

    @Transactional
    public void activateUser(UUID userId) {
        User user = getUserById(userId);
        user.setIsActive(true);
        userRepository.save(user);
    }

    @Transactional
    public void deactivateUser(UUID userId) {
        User user = getUserById(userId);
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> getPendingInstructors() {
        return userRepository.findByRoleAndIsActive(UserRole.INSTRUCTOR, false);
    }

    @Transactional(readOnly = true)
    public long getPendingInstructorsCount() {
        return userRepository.countByRoleAndIsActive(UserRole.INSTRUCTOR, false);
    }

    @Transactional
    public void approveInstructor(UUID userId) {
        User user = getUserById(userId);
        if (user.getRole() != UserRole.INSTRUCTOR) {
            throw new IllegalArgumentException("User is not an instructor");
        }
        user.setIsActive(true);
        userRepository.save(user);
    }

    @Transactional
    public void rejectInstructor(UUID userId) {
        User user = getUserById(userId);
        user.setRole(UserRole.USER);
        user.setIsActive(true);
        userRepository.save(user);
    }
}