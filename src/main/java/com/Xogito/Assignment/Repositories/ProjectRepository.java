package com.Xogito.Assignment.Repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.Xogito.Assignment.Models.Project;

/**
 * Repository for the {@link com.Xogito.Assignment.Models.Project Project} Entity
 * 
 * @extends JpaRepository   
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {
	
	Optional<Project> findByNameIgnoreCase(String name);
	
	Page<Project> findByNameContainingIgnoreCase(String name, Pageable p);

}
