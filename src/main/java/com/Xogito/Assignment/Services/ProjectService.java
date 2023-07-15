package com.Xogito.Assignment.Services;

import java.util.List;

import org.springframework.web.server.ResponseStatusException;

import com.Xogito.Assignment.Models.Project;

/**
 * Service interface for the {@link com.Xogito.Assignment.Models.Project Project} Entity 
 */
public interface ProjectService {

	public List<Project> findAll(int page, int size) throws ResponseStatusException;

	public List<Project> findAllByName(String name, int page, int size) throws ResponseStatusException;

	public Project findById(Long id) throws ResponseStatusException;

	public Project create(Project p) throws ResponseStatusException;

	public Project update(Long id, Project p) throws ResponseStatusException;

	public Project assignUser(Long pId, String email) throws ResponseStatusException;

	public Project remove(Long id) throws ResponseStatusException;
}
