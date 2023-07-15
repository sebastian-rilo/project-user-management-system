package com.Xogito.Assignment.Implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Xogito.Assignment.Models.Project;
import com.Xogito.Assignment.Models.User;
import com.Xogito.Assignment.Repositories.ProjectRepository;
import com.Xogito.Assignment.Services.ProjectService;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of the {@link com.Xogito.Assignment.Services.ProjectService
 * ProjectService} interface with the task of resolving clients's HTTP requests
 * for the {@link com.Xogito.Assignment.Models.Project Project} entity
 */
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository pRepo;
	private final UserServiceImpl uSrv;

	/**
	 * Retrieves a chunk of the total projects in the database.
	 * 
	 * @param page The current page number from where the projects will be
	 *             retrieved.
	 * @param size The size of the current page.
	 * 
	 * @return List Retrieved page of projects.
	 * 
	 * @throws ResponseStatusException When there are no projects to retrieve.
	 */
	@Override
	public List<Project> findAll(int page, int size) throws ResponseStatusException {
		Page<Project> projects = pRepo.findAll(PageRequest.of(page, size));
		if (projects.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no results to show.");
		}
		return projects.getContent();
	}

	/**
	 * Retrieves a chunk of the total projects in the database with a matching name
	 * value.
	 * 
	 * @param name The name value which will be used to retrieve projects.
	 * @param page The current page number from where the projects will be
	 *             retrieved.
	 * @param size The size of the current page.
	 * 
	 * @return List Retrieved page of matching projects.
	 * 
	 * @throws ResponseStatusException When there are no matching projects to
	 *                                 retrieve.
	 */
	@Override
	public List<Project> findAllByName(String name, int page, int size) throws ResponseStatusException {
		Page<Project> projects = pRepo.findByNameContainingIgnoreCase(name, PageRequest.of(page, size));
		if (projects.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"There are no results to show with the value: '" + name + "'.");
		}
		return projects.getContent();
	}

	/**
	 * Retrieves a single project with a matching Id value.
	 * 
	 * @param id The value which will be used to retrieve the project.
	 *
	 * @return List Retrieved page of matching projects.
	 * 
	 * @throws ResponseStatusException When there is no matching project to
	 *                                 retrieve.
	 */
	@Override
	public Project findById(Long id) throws ResponseStatusException {
		Optional<Project> p = pRepo.findById(id);
		return p.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"There are no projects with the id: '" + id + "'."));
	}

	/**
	 * saves a given project in the database.
	 * 
	 * @param p The project to be saved in the database.
	 * 
	 * @return Project The saved project.
	 * 
	 * @throws ResponseStatusException When the new project's name is already being
	 *                                 used by another project in the database.
	 */
	@Override
	public Project create(Project p) throws ResponseStatusException {
		try {
			System.out.println(p);
			pRepo.save(p);
			return p;
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"The project name '" + p.getName() + "' is not available.");
		}
	}

	/**
	 * Finds and updates a project in the database.
	 * 
	 * @param id The project's id.
	 * @param p  The project's new property values.
	 * 
	 * @return List Retrieved page of matching projects.
	 * 
	 * @throws ResponseStatusException When there are no changes to make or when a
	 *                                 project with the given id doesn't exists.
	 */
	@Override
	public Project update(Long id, Project p) throws ResponseStatusException {
		Project ogP = findById(id);
		p.setId(id);
		if (p.equals(ogP)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There are no changes to make on this project");
		}
		pRepo.save(p);
		return p;
	}

	/**
	 * Assigns a user to a project in the database.
	 * 
	 * @param pId   The project's id.
	 * @param email user's email.
	 * 
	 * @return The updated project with the assigned user.
	 * 
	 * @throws ResponseStatusException When the selected user is already assigned to
	 *                                 the project, when a project with the given id
	 *                                 doesn't exists or when a user with the given
	 *                                 email doesn't exits.
	 */
	@Override
	public Project assignUser(Long pId, String email) throws ResponseStatusException {
		Project p = findById(pId);
		User u = uSrv.findByEmail(email);
		if (p.getUsers().contains(u)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"The selected user is already assigned to this project.");
		}
		p.getUsers().add(u);
		pRepo.save(p);
		return p;
	}

	/**
	 * removes a project in the database.
	 * 
	 * @param id The project's id.
	 * 
	 * @return The removed project.
	 * 
	 * @throws ResponseStatusException When the selected user is already assigned to
	 *                                 the project or when a project with the given
	 *                                 id doesn't exists
	 */
	@Override
	public Project remove(Long id) throws ResponseStatusException {
		Project p = findById(id);
		pRepo.delete(p);
		return p;
	}
}
