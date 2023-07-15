package com.Xogito.Assignment.Repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.Xogito.Assignment.Models.User;

/**
 * Repository for the {@link com.Xogito.Assignment.Models.User User} Entity
 * 
 * @extends JpaRepository   
 */
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByName(String name);

	Page<User> findByNameContainingIgnoreCase(String name, Pageable p);

	Optional<User> findByEmail(String email);
}
