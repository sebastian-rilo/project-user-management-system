package com.Xogito.Assignment.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.Xogito.Assignment.Implementations.ProjectServiceImpl;
import com.Xogito.Assignment.Models.Project;
import com.Xogito.Assignment.Utilities.ResponseHandler;
import com.Xogito.Assignment.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for {@link com.Xogito.Assignment.Models.Project Project}
 * related endpoints.
 */
@RestController
@Validated
@RequestMapping(path = "/api/projects", produces = "application/json")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectServiceImpl pSrv;

	/**
	 * Implements
	 * {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#findAll
	 * findAll()} method and returns a JSON response with the specified number of
	 * projects.
	 * 
	 * @param page The number of the page of projects to retrieve - must be greater than 0.
	 * @param size The number of projects per page to retrieve - must be greater than 1.
	 * @return A JSON response with an array of projects.
	 */
	@GetMapping(path = "")
	@JsonView(Views.coreDataView.class)
	public ResponseEntity<?> getAllProjects(@RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "page value must be equal or greater than 0") int page,
			@RequestParam(name = "size", defaultValue = "5") @Min(value = 1, message = "size value must be equal or greater than 1") int size, UriComponentsBuilder uriBuilder,
			  HttpServletResponse response) {
		return ResponseHandler.handleDataResponses(pSrv.findAll(page, size), HttpStatus.OK);
	}

	/**
	 * Implements
	 * {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#findById
	 * findById()} method and returns a JSON response of a project with the given id.
	 * 
	 * @param id The search parameter.
	 * @return A JSON response with the matching projects.
	 */
	@GetMapping("/{id}")
	@JsonView(Views.fullDataView.class)
	public ResponseEntity<?> getProjectById(@PathVariable("id") Long id) {
		return ResponseHandler.handleDataResponses(pSrv.findById(id), HttpStatus.OK);
	}

	/**
	 * Implements
	 * {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#findAllByName
	 * findAllByName()} method and returns a JSON response of the projects with a matching name value.
	 * 
	 * @param name The search parameter - must not be left blank.
	 * @param page The number of the page of projects to retrieve - must be greater than 0.
	 * @param size The number of projects per page to retrieve - must be greater than 0.
	 * @return A JSON response with an array of projects.
	 */
	@GetMapping(path = "/name", params = "value")
	@JsonView(Views.coreDataView.class)
	public ResponseEntity<?> getProjectsByName(
			@RequestParam(name = "value") @NotBlank(message = "name must not be left blank") String name,
			@RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "page value must be equal or greater than 0") int page,
			@RequestParam(name = "size", defaultValue = "5") @Min(value = 1, message = "size value must be equal or greater than 1") int size) {
		return ResponseHandler.handleDataResponses(pSrv.findAllByName(name, page, size), HttpStatus.OK);
	}

	/**
	 * Implements
	 * {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#create
	 * create()} method and returns a JSON response with the new project.
	 * 
	 * @param p The new project entity.
	 * @return A JSON response with the new project.
	 */
	@PostMapping("")
	public ResponseEntity<?> createProject(@RequestBody @Valid Project p) {
		return ResponseHandler.handleDataResponses(pSrv.create(p), HttpStatus.OK);
	}

	/**
	 * Implements
	 * {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#update
	 * update()} method and returns a JSON response with the updated project.
	 * 
	 * @param id The project id.
	 * @param p The new project property values.
	 * @return A JSON response with the updated project.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateProject(@PathVariable("id") Long id, @RequestBody @Valid Project p) {
		return ResponseHandler.handleDataResponses(pSrv.update(id, p), HttpStatus.OK);
	}

	/**
	 * Implements
	 * {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#assignUser
	 * assignUser()} method and returns a JSON response with the assigned project.
	 * 
	 * @param projectId The project id.
	 * @param email The user's email.
	 * @return A JSON response with the assigned project.
	 */
	@PatchMapping("/{id}/assign-user/{email}")
	public ResponseEntity<?> assignUserToProject(@PathVariable("id") Long projectId,
			@PathVariable("email") @NotBlank(message = "email must not be left blank") String email) {
		return ResponseHandler.handleDataResponses(pSrv.assignUser(projectId, email), HttpStatus.OK);
	}

	/**
	 * Implements
	 * {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#remove
	 * remove()} method and returns a JSON response of the removed project.
	 * 
	 * @param id The project id.
	 * @return A JSON response with the removed project.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> removeProject(@PathVariable("id") Long id) {
		return ResponseHandler.handleDataResponses(pSrv.remove(id), HttpStatus.OK);
	}

}
