package com.Xogito.Assignment.Implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Xogito.Assignment.Models.User;
import com.Xogito.Assignment.Repositories.UserRepository;
import com.Xogito.Assignment.Services.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of the {@link com.Xogito.Assignment.Services.UserService
 * UserService} interface with the task of resolving clients's HTTP requests for
 * the {@link com.Xogito.Assignment.Models.User User} entity
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository uRepo;

	/**
	 * Retrieves a chunk of the total users in the database.
	 * 
	 * @param page The current page number from where the users will be retrieved.
	 * @param size The size of the current page.
	 * 
	 * @return List Retrieved page of users.
	 * 
	 * @throws ResponseStatusException When there are no users to retrieve.
	 */
	public List<User> findAll(int page, int size) throws ResponseStatusException {
		Page<User> users = uRepo.findAll(PageRequest.of(page, size));
		if (users.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no results to show.");
		}
		return users.getContent();
	}

	/**
	 * Retrieves a chunk of the total users in the database with a matching name
	 * value.
	 * 
	 * @param name The name value which will be used to retrieve users.
	 * @param page The current page number from where the users will be retrieved.
	 * @param size The size of the current page.
	 * 
	 * @return List Retrieved page of matching users.
	 * 
	 * @throws ResponseStatusException When there are no matching users to retrieve.
	 */
	public List<User> findAllByName(String name, int page, int size) throws ResponseStatusException {
		Page<User> users = uRepo.findByNameContainingIgnoreCase(name, PageRequest.of(page, size));
		if (users.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"There are no results to show with the value: '" + name + "'.");
		}
		return users.getContent();
	}

	/**
	 * Retrieves a single user with a matching Id value.
	 * 
	 * @param id The value which will be used to retrieve the user.
	 *
	 * @return List Retrieved page of matching users.
	 * 
	 * @throws ResponseStatusException When there is no matching user to retrieve.
	 */
	public User findById(Long id) throws ResponseStatusException {
		Optional<User> u = uRepo.findById(id);
		return u.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"There are no users with the id: '" + id + "'."));
	}

	/**
	 * Retrieves a single user with a matching email value.
	 * 
	 * @param email The value which will be used to retrieve the user.
	 *
	 * @return List Retrieved page of matching users.
	 * 
	 * @throws ResponseStatusException When there is no matching user to retrieve.
	 */
	public User findByEmail(String email) throws ResponseStatusException {
		Optional<User> u = uRepo.findByEmail(email);
		return u.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"There are no users with the email: '" + email + "'."));
	}

	/**
	 * saves a given user in the database.
	 * 
	 * @param u The user to be saved in the database.
	 * 
	 * @return Project The saved user.
	 * 
	 * @throws ResponseStatusException When the new user's name is already being
	 *                                 used by another user in the database.
	 */
	public User create(User u) throws ResponseStatusException {
		try {
			uRepo.save(u);
			return u;
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"The email address '" + u.getEmail() + "' is not available.");
		}
	}

	/**
	 * Finds and updates a user in the database.
	 * 
	 * @param id The user's id.
	 * @param u  The user's new property values.
	 * 
	 * @return List Retrieved page of matching users.
	 * 
	 * @throws ResponseStatusException When there are no changes to make or when a
	 *                                 user with the given id doesn't exists.
	 */
	public User update(Long id, User u) throws ResponseStatusException {
		User ogU = findById(id);
		u.setId(id);
		if (u.equals(ogU)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There are no changes to make on this user");
		}
		try {
			uRepo.save(u);
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this email address is not available");
		}
		return u;
	}

	/**
	 * removes a user in the database.
	 * 
	 * @param id The user's id.
	 * 
	 * @return The removed user.
	 * 
	 * @throws ResponseStatusException When the selected user is already assigned to
	 *                                 the user or when a user with the given id
	 *                                 doesn't exists
	 */
	public User remove(Long id) throws ResponseStatusException {
		User u = findById(id);
		uRepo.delete(u);
		return u;
	}

}
