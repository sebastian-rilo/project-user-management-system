package com.Xogito.Assignment.Implementations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import com.Xogito.Assignment.Models.Project;
import com.Xogito.Assignment.Models.User;
import com.Xogito.Assignment.Repositories.ProjectRepository;
import com.Xogito.Assignment.Repositories.UserRepository;

/**
 * {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl
 * ProjectServiceImpl.class} testing class.
 */
class ProjectServiceImplTest {

	private ProjectRepository pRepo = Mockito.mock(ProjectRepository.class);
	private ProjectServiceImpl pSrv;

	private UserRepository uRepo = Mockito.mock(UserRepository.class);
	private UserServiceImpl uSrv;

	@BeforeEach
	void initServiceImpl() {
		uSrv = new UserServiceImpl(uRepo);
		pSrv = new ProjectServiceImpl(pRepo, uSrv);
	}

	/**
	 * Tests {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#findAll findAll()} method 
	 * resolution when the repository doesn't retrieve any projects.
	 */
	@Test
	void findAllNoProjects() {
		when(pRepo.findAll(PageRequest.of(0, 3))).thenReturn(new PageImpl<Project>(List.of()));
		assertThrows(ResponseStatusException.class, () -> {
			pSrv.findAll(0, 3);
		});
	}

	/**
	 * Tests {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#findAll
	 * findAll()} method resolution when the repository does retrieve at least one
	 * project.
	 */
	@Test
	void findAllSucess() {
		List<Project> mockP = List.of(
				new Project(Long.valueOf(1), "Mock Project I", "description of mock project I", null),
				new Project(Long.valueOf(1), "Mock Project II", "description of mock project II", null),
				new Project(Long.valueOf(1), "Mock Project III", "description of mock project III", null));
		when(pRepo.findAll(PageRequest.of(0, 3))).thenReturn(new PageImpl<Project>(mockP));
		assertEquals(mockP, pSrv.findAll(0, 3));
	}

	/**
	 * Tests
	 * {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#findAllByName
	 * findAllByName()} method resolution when the repository doesn't find any
	 * matching projects with the given string.
	 */
	@Test
	void findAllByNameNoMatch() {
		String mockName = "Impossible To Match Name";
		when(pRepo.findByNameContainingIgnoreCase(mockName, PageRequest.of(0, 10)))
				.thenReturn(new PageImpl<Project>(List.of()));
		assertThrows(ResponseStatusException.class, () -> pSrv.findAllByName(mockName, 0, 10));
	}

	/**
	 * Tests
	 * {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#findAllByName
	 * findAllByName()} method resolution when the repository finds a project via
	 * complete match.
	 */
	@Test
	void findAllByNamePerfectMatch() {
		String mockName = "Mock Project III";
		List<Project> mockUList = List
				.of(new Project(Long.valueOf(3), mockName, "description of mock project III", null));
		when(pRepo.findByNameContainingIgnoreCase(mockName, PageRequest.of(0, 3)))
				.thenReturn(new PageImpl<Project>(mockUList));
		assertEquals(mockUList, pSrv.findAllByName(mockName, 0, 3));
	}

	/**
	 * Tests
	 * {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#findAllByName
	 * findAllByName()} method resolution when the repository finds a project via
	 * partial match.
	 */
	@Test
	void findAllByNamePartialMatch() {
		String mockName = "ck Project III";
		List<Project> mockUList = List
				.of(new Project(Long.valueOf(3), "Mock Project III", "description of mock project III", null));
		when(pRepo.findByNameContainingIgnoreCase(mockName, PageRequest.of(0, 3)))
				.thenReturn(new PageImpl<Project>(mockUList));
		assertEquals(mockUList, pSrv.findAllByName(mockName, 0, 3));
	}

	/**
	 * Tests
	 * {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#findAllByName
	 * findAllByName()} method resolution when the repository finds multiple
	 * projects via partial match.
	 */
	@Test
	void findAllByNameMultiplePartialMatches() {
		String mockName = "Project II";
		List<Project> mockUList = new ArrayList<Project>(
				List.of(new Project(Long.valueOf(2), "Mock Project II", "description of mock project II", null),
						new Project(Long.valueOf(3), "Mock Project III", "description of mock project III", null)));
		when(pRepo.findByNameContainingIgnoreCase(mockName, PageRequest.of(0, 3)))
				.thenReturn(new PageImpl<Project>(mockUList));
		assertEquals(mockUList, pSrv.findAllByName(mockName, 0, 3));
	}

	/**
	 * Tests
	 * {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#findById
	 * findById()} method resolution when the repository doesn't find any project with the given id.
	 */
	@Test
	void findByIdNotFound() {
		when(pRepo.findById(Long.valueOf(4))).thenReturn(Optional.empty());
		assertThrows(ResponseStatusException.class, () -> {
			pSrv.findById(Long.valueOf(4));
		});
	}

	/**
	 * Tests
	 * {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#findById
	 * findById()} method resolution when the repository does find a project with
	 * the given id.
	 */
	@Test
	void findByIdFound() {
		Project mockP = new Project(Long.valueOf(2), "Mock Project II", "description of mock project II", null);
		when(pRepo.findById(Long.valueOf(2))).thenReturn(Optional.of(mockP));
		assertEquals(mockP, pSrv.findById(Long.valueOf(2)));
	}

	/**
	 * Tests {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#create
	 * create()} method resolution when the new project's name is already in use.
	 */
	@Test
	void createUsedName() {
		Project newMockProject = new Project(Long.valueOf(4), "Mock Project IV", "description of mock project IV",
				null);
		when(pRepo.save(newMockProject)).thenThrow(DataIntegrityViolationException.class);
		assertThrows(ResponseStatusException.class, () -> pSrv.create(newMockProject));
	}

	/**
	 * Tests {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#create
	 * create()} method resolution when the new project is supposed to be saved.
	 */
	@Test
	void createSuccess() {
		Project newMockProject = new Project(Long.valueOf(4), "Mock Project IV", "description of mock project IV",
				null);
		assertEquals(newMockProject, pSrv.create(newMockProject));
	}

	/**
	 * Tests {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#update
	 * update()} method resolution when the updated project's new name is already in
	 * use.
	 */
	@Test
	void updateUsedName() {
		Project updatedMockProject = new Project(Long.valueOf(4), "Mock Project IV", "description of mock project IV",
				null);
		when(pRepo.save(updatedMockProject)).thenThrow(DataIntegrityViolationException.class);
		assertThrows(ResponseStatusException.class, () -> pSrv.update(Long.valueOf(4), updatedMockProject));
	}

	/**
	 * Tests {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#update
	 * update()} method resolution when the updated project has no changes to its
	 * properties.
	 */
	@Test
	void updateNoChanges() {
		Project ogMockProject = new Project(Long.valueOf(4), "Mock Project IV", "description of mock project IV", null);
		when(pRepo.findById(Long.valueOf(4))).thenReturn(Optional.of(ogMockProject));
		assertThrows(ResponseStatusException.class, () -> pSrv.update(Long.valueOf(4), ogMockProject));
	}

	/**
	 * Tests {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#update
	 * update()} method resolution when the updated project is supposed to
	 * successfully replace the old one.
	 */
	@Test
	void updateSuccess() {
		Project ogMockProject = new Project(Long.valueOf(4), "Mock Project IV", "description of mock project IV", null);
		Project updtMockProject = new Project(null, "Updated Mock Project IV", "updated description of mock project IV",
				null);
		when(pRepo.findById(Long.valueOf(4))).thenReturn(Optional.of(ogMockProject));
		assertEquals(updtMockProject, pSrv.update(Long.valueOf(4), updtMockProject));
	}

	/**
	 * Tests
	 * {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#assignUser
	 * assignUser()} method resolution when the user is already assigned to the
	 * project.
	 */
	@Test
	void assignUserAlreadyAssigned() {
		String email = "mock4@mail.com";
		User mockU = new User(Long.valueOf(4), "Mock User IV", email);
		Project mockP = new Project(Long.valueOf(4), "Mock Project IV", "description of mock project IV",
				List.of(mockU));
		when(pRepo.findById(Long.valueOf(4))).thenReturn(Optional.of(mockP));
		assertThrows(ResponseStatusException.class, () -> pSrv.assignUser(Long.valueOf(4), email));
	}

	/**
	 * Tests
	 * {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#assignUser
	 * assignUser()} method resolution when the user is successfully assigned to the
	 * project.
	 */
	@Test
	void assignUserSuccess() {
		String email = "mock4@mail.com";
		User mockU = new User(Long.valueOf(4), "Mock User IV", email);
		Project mockP = new Project(Long.valueOf(4), "Mock Project IV", "description of mock project IV",
				new ArrayList<>());
		when(pRepo.findById(Long.valueOf(4))).thenReturn(Optional.of(mockP));
		when(uRepo.findByEmail(email)).thenReturn(Optional.of(mockU));
		Project returnedVal = pSrv.assignUser(Long.valueOf(4), email);
		mockP.getUsers().add(mockU);
		assertEquals(mockP, returnedVal);
	}

	/**
	 * Tests {@link com.Xogito.Assignment.Implementations.ProjectServiceImpl#remove
	 * remove()} method resolution when the project is successfully removed from the
	 * database.
	 */
	@Test
	void removeProjectSuccess() {
		Project mockP = new Project(Long.valueOf(3), "Mock Project III", "description of mock project III", null);
		when(pRepo.findById(Long.valueOf(3))).thenReturn(Optional.of(mockP));
		assertEquals(mockP, pSrv.remove(Long.valueOf(3)));
	}
}
