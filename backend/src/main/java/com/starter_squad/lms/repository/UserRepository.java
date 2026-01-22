package com.starter_squad.lms.repository;

import com.starter_squad.lms.entity.User;
import com.starter_squad.lms.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

	// ==========================================
	// BASIC QUERIES (Original from your code)
	// ==========================================

	User findByEmail(String email);
	boolean existsByEmail(String email);
	boolean existsByRole(UserRole role);

	// ⚠️ DEPRECATED: Don't use this for authentication (password should be encrypted)
	// Use Spring Security's AuthenticationManager instead
	@Deprecated
	User findByEmailAndPassword(String email, String password);

	// ==========================================
	// ADDITIONAL BASIC QUERIES
	// ==========================================

	Optional<User> findByUsername(String username);
	Boolean existsByUsername(String username);

	// ==========================================
	// SEARCH QUERIES (Based on your User entity fields)
	// ==========================================

	List<User> findByUsernameContainingOrEmailContaining(String username, String email);

	@Query("SELECT u FROM User u WHERE " +
			"LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
			"LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<User> searchByKeyword(@Param("keyword") String keyword);

	// ==========================================
	// FILTER BY ROLE (UserRole Enum)
	// ==========================================

	List<User> findByRole(UserRole role);

	@Query("SELECT u FROM User u WHERE u.role = :role")
	List<User> findUsersByRole(@Param("role") UserRole role);

	// ==========================================
	// FILTER BY STATUS
	// ==========================================

	List<User> findByIsActiveTrue();
	List<User> findByIsActiveFalse();

	@Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
	long countActiveUsers();

	// ==========================================
	// COMBINED FILTERS
	// ==========================================

	@Query("SELECT u FROM User u WHERE u.role = :role AND u.isActive = true")
	List<User> findActiveUsersByRole(@Param("role") UserRole role);

	List<User> findByRoleAndIsActive(UserRole role, Boolean isActive);

	// ==========================================
	// SORTING & ORDERING
	// ==========================================

	@Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
	List<User> findAllOrderByCreatedAtDesc();

	@Query("SELECT u FROM User u ORDER BY u.updatedAt DESC")
	List<User> findAllOrderByUpdatedAtDesc();

	// ==========================================
	// COUNT QUERIES
	// ==========================================

	@Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
	long countByRole(@Param("role") UserRole role);

	long countByIsActive(Boolean isActive);

	long countByRoleAndIsActive(UserRole role, Boolean isActive);
}