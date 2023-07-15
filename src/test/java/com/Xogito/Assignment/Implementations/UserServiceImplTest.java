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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import com.Xogito.Assignment.Models.User;
import com.Xogito.Assignment.Repositories.UserRepository;

/**
 * {@link com.Xogito.Assignment.Implementations.UserServiceImpl UserServiceImpl.class}
 * testing class.
 */
class UserServiceImplTest {

	private UserRepository uRepo = Mockito.mock(UserRepository.class);
	private UserServiceImpl uSrv;

	@BeforeEach
	void initServiceImpl() {
		uSrv = new UserServiceImpl(uRepo);
	}

	/**
	 * Tests {@link com.Xogito.Assignment.Implementations.UserServiceImpl#findAll findAll()} method 
	 * resolution when the repository doesn't retrieve any users.
	 */
	@Test
	void findAllNoUsers() {
		when(uRepo.findAll(PageRequest.of(0, 3))).thenReturn(new PageImpl<User>(List.of()));
		assertThrows(ResponseStatusException.class, () -> {
			uSrv.findAll(0, 3);
		});
	}

	/**
	 * Tests {@link com.Xogito.Assignment.Implementations.UserServiceImpl#findAll
	 * findAll()} method resolution when the repository does retrieve at least one
	 * user.
	 */
	@Test
	void findAllSucess() {
		List<User> mockU = List.of(new User(Long.valueOf(1), "Mock User I", "mock1@mail.com"),
				new User(Long.valueOf(2), "Mock User II", "mock2@mail.com"),
				new User(Long.valueOf(3), "Mock User III", "mock3@mail.com"));
		when(uRepo.findAll(PageRequest.of(0, 3))).thenReturn(new PageImpl<User>(mockU));
		assertEquals(mockU, uSrv.findAll(0, 3));
	}

	/**
	 * Tests
	 * {@link com.Xogito.Assignment.Implementations.UserServiceImpl#findAllByName
	 * findAllByName()} method resolution when the repository doesn't find any
	 * matching users with the given string.
	 */
	@Test
	void findAllByNameNoMatch() {
		String mockName = "ImpossibleTo MatchName";
		Page<User> pagedResponse = new PageImpl<User>(List.of());
		when(uRepo.findByNameContainingIgnoreCase(mockName, PageRequest.of(0, 10))).thenReturn(pagedResponse);
		assertThrows(ResponseStatusException.class, () -> uSrv.findAllByName(mockName, 0, 10));
	}

	/**
	 * Tests
	 * {@link com.Xogito.Assignment.Implementations.UserServiceImpl#findAllByName
	 * findAllByName()} method resolution when the repository finds a user via
	 * complete match.
	 */
	@Test
	void findAllByNamePerfectMatch() {
		String mockName = "Mock User III";
		List<User> mockUList = List.of(new User(Long.valueOf(3), mockName, "mock3@mail.com"));
		when(uRepo.findByNameContainingIgnoreCase(mockName, PageRequest.of(0, 3)))
				.thenReturn(new PageImpl<User>(mockUList));
		assertEquals(mockUList, uSrv.findAllByName(mockName, 0, 3));
	}

	/**
	 * Tests
	 * {@link com.Xogito.Assignment.Implementations.UserServiceImpl#findAllByName
	 * findAllByName()} method resolution when the repository finds a user via
	 * partial match.
	 */
	@Test
	void findAllByNamePartialMatch() {
		String mockName = "ck User III";
		List<User> mockUList = List.of(new User(Long.valueOf(3), "Mock User III", "mock3@mail.com"));
		when(uRepo.findByNameContainingIgnoreCase(mockName, PageRequest.of(0, 3)))
				.thenReturn(new PageImpl<User>(mockUList));
		assertEquals(mockUList, uSrv.findAllByName(mockName, 0, 3));
	}

	/**
	 * Tests
	 * {@link com.Xogito.Assignment.Implementations.UserServiceImpl#findAllByName
	 * findAllByName()} method resolution when the repository finds multiple users
	 * via partial match.
	 */
	@Test
	void findAllByNameMultiplePartialMatches() {
		String mockName = "User II";
		List<User> mockUList = new ArrayList<User>(List.of(new User(Long.valueOf(2), "Mock User II", "mock2@mail.com"),
				new User(Long.valueOf(3), "Mock User III", "mock3@mail.com")));
		when(uRepo.findByNameContainingIgnoreCase(mockName, PageRequest.of(0, 3)))
				.thenReturn(new PageImpl<User>(mockUList));
		assertEquals(mockUList, uSrv.findAllByName(mockName, 0, 3));
	}

	/**
	 * Tests
	 * {@link com.Xogito.Assignment.Implementations.UserServiceImpl#findById
	 * findById()} method resolution when the repository doesn't find any user with the given id.
	 */
	@Test
	void findByIdNotFound() {
		when(uRepo.findById(Long.valueOf(4))).thenReturn(Optional.empty());
		assertThrows(ResponseStatusException.class, () -> {
			uSrv.findById(Long.valueOf(4));
		});
	}

	/**
	 * Tests {@link com.Xogito.Assignment.Implementations.UserServiceImpl#findById
	 * findById()} method resolution when the repository does find a user with the
	 * given id.
	 */
	@Test
	void findByIdFound() {
		User mockU = new User(Long.valueOf(2), "Mock User II", "mock2@mail.com");
		when(uRepo.findById(Long.valueOf(2))).thenReturn(Optional.of(mockU));
		assertEquals(mockU, uSrv.findById(Long.valueOf(2)));
	}

	/**
	 * Tests
	 * {@link com.Xogito.Assignment.Implementations.UserServiceImpl#findByEmail
	 * findByEmail()} method resolution when the repository doesn't find any user
	 * with the given email.
	 */
	@Test
	void findByEmailNotFound() {
		String email = "notfound@email.com";
		when(uRepo.findByEmail(email)).thenReturn(Optional.empty());
		assertThrows(ResponseStatusException.class, () -> {
			uSrv.findByEmail(email);
		});
	}

	/**
	 * Tests
	 * {@link com.Xogito.Assignment.Implementations.UserServiceImpl#findByEmail
	 * findByEmail()} method resolution when the repository does find a user with
	 * the given email.
	 */
	@Test
	void findByEmailFound() {
		String email = "mock3@mail.com";
		User mockU = new User(Long.valueOf(3), "Mock User III", "mock3@mail.com");
		when(uRepo.findByEmail(email)).thenReturn(Optional.of(mockU));
		assertEquals(mockU, uSrv.findByEmail(email));
	}

	/**
	 * Tests {@link com.Xogito.Assignment.Implementations.UserServiceImpl#create
	 * create()} method resolution when the new user's email is already in use.
	 */
	@Test
	void createUserWithUsedEmail() {
		User newMockUser = new User(Long.valueOf(4), "Mock User IV", "mock4@mail.com");
		when(uRepo.save(newMockUser)).thenThrow(DataIntegrityViolationException.class);
		assertThrows(ResponseStatusException.class, () -> uSrv.create(newMockUser));
	}

	/**
	 * Tests {@link com.Xogito.Assignment.Implementations.UserServiceImpl#create
	 * create()} method resolution when the new user is supposed to be saved.
	 */
	@Test
	void createUserSuccess() {
		User newMockUser = new User(Long.valueOf(4), "Mock User IV", "mock4@mail.com");
		assertEquals(newMockUser, uSrv.create(newMockUser));
	}

	/**
	 * Tests {@link com.Xogito.Assignment.Implementations.UserServiceImpl#update
	 * update()} method resolution when the updated user's new email is already in
	 * use.
	 */
	@Test
	void updateUserUsedEmail() {
		User updatedMockUser = new User(Long.valueOf(4), "Mock User IV", "mock4@mail.com");
		when(uRepo.save(updatedMockUser)).thenThrow(DataIntegrityViolationException.class);
		assertThrows(ResponseStatusException.class, () -> uSrv.update(Long.valueOf(4), updatedMockUser));
	}

	/**
	 * Tests {@link com.Xogito.Assignment.Implementations.UserServiceImpl#update
	 * update()} method resolution when the updated user has no changes to its
	 * properties.
	 */
	@Test
	void updateUserNoChanges() {
		User ogMockUser = new User(Long.valueOf(4), "Mock User IV", "mock4@mail.com");
		when(uRepo.findById(Long.valueOf(4))).thenReturn(Optional.of(ogMockUser));
		assertThrows(ResponseStatusException.class, () -> uSrv.update(Long.valueOf(4), ogMockUser));
	}

	/**
	 * Tests {@link com.Xogito.Assignment.Implementations.UserServiceImpl#update
	 * update()} method resolution when the updated user is supposed to successfully
	 * replace the old one.
	 */
	@Test
	void updateUserSuccess() {
		User ogMockUser = new User(Long.valueOf(4), "Mock User IV", "mock4@mail.com");
		User updtMockUser = new User(null, "Updated Mock User IV", "updtmock4@mail.com");
		when(uRepo.findById(Long.valueOf(4))).thenReturn(Optional.of(ogMockUser));
		assertEquals(updtMockUser, uSrv.update(Long.valueOf(4), updtMockUser));
	}

	/**
	 * Tests {@link com.Xogito.Assignment.Implementations.UserServiceImpl#remove
	 * remove()} method resolution when the user is successfully removed from the
	 * database.
	 */
	@Test
	void removeUserSuccess() {
		User mockU = new User(Long.valueOf(3), "Mock User III", "mock3@mail.com");
		when(uRepo.findById(Long.valueOf(3))).thenReturn(Optional.of(mockU));
		assertEquals(mockU, uSrv.remove(Long.valueOf(3)));
	}
}
