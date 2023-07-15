package com.Xogito.Assignment.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Xogito.Assignment.Implementations.UserServiceImpl;
import com.Xogito.Assignment.Models.User;
import com.Xogito.Assignment.Utilities.ResponseHandler;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for {@link com.Xogito.Assignment.Models.User User} related
 * endpoints.
 */
@RestController
@RequestMapping(path = "/api/users", produces = "application/json")
@RequiredArgsConstructor
public class UserController {

	private final UserServiceImpl uSrv;

	/**
	 * Implements
	 * {@link com.Xogito.Assignment.Implementations.UserServiceImpl#findAll
	 * findAll()} method and returns a JSON response with the specified number of
	 * users.
	 * 
	 * @param page The number of the page of users to retrieve - must be greater
	 *             than 0.
	 * @param size The number of users per page to retrieve - must be equal or
	 *             greater than 1.
	 * @return A JSON response with an array of users.
	 */
	@GetMapping(path = "")
	public ResponseEntity<?> getAllUsers(
			@RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "page value must be equal or greater than 0") int page,
			@RequestParam(name = "size", defaultValue = "5") @Min(value = 1, message = "size value must be equal or greater than 1") int size) {
		return ResponseHandler.handleDataResponses(uSrv.findAll(page, size), HttpStatus.OK);
	}

	/**
	 * Implements
	 * {@link com.Xogito.Assignment.Implementations.UserServiceImpl#findById
	 * findById()} method and returns a JSON response of a user with the given
	 * id.
	 * 
	 * @param id The search parameter.
	 * @return A JSON response with the matching users.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
		return ResponseHandler.handleDataResponses(uSrv.findById(id), HttpStatus.OK);
	}

	/**
	 * Implements
	 * {@link com.Xogito.Assignment.Implementations.UserServiceImpl#findAllByName
	 * findAllByName()} method and returns a JSON response of the users with a
	 * matching name value.
	 * 
	 * @param name The search parameter - must not be left blank.
	 * @param page The number of the page of users to retrieve - must be greater
	 *             than 0.
	 * @param size The number of users per page to retrieve - must be equal or
	 *             greater than 1.
	 * @return A JSON response with an array of users.
	 */
	@GetMapping(path = "/name", params = "value")
	public ResponseEntity<?> getUsersByName(
			@RequestParam(name = "value") @NotBlank(message = "name must not be left blank") String name,
			@RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "page value must be equal or greater than 0") int page,
			@RequestParam(name = "size", defaultValue = "5") @Min(value = 1, message = "size value must be equal or greater than 1") int size) {
		return ResponseHandler.handleDataResponses(uSrv.findAllByName(name, page, size), HttpStatus.OK);
	}

	/**
	 * Implements
	 * {@link com.Xogito.Assignment.Implementations.UserServiceImpl#findByEmail
	 * findByEmail()} method and returns a JSON response of a user with the given
	 * email.
	 * 
	 * @param email The search parameter - must not be left blank.
	 * @return A JSON response with the matching user.
	 */
	@GetMapping(path = "/email", params = "value")
	public ResponseEntity<?> getUserByEmail(
			@RequestParam("value") @NotBlank(message = "email must not be left blank") String email) {
		return ResponseHandler.handleDataResponses(uSrv.findByEmail(email), HttpStatus.OK);
	}

	/**
	 * Implements
	 * {@link com.Xogito.Assignment.Implementations.UserServiceImpl#create create()}
	 * method and returns a JSON response with the new user.
	 * 
	 * @param u The new user entity.
	 * @return A JSON response with the new user.
	 */
	@PostMapping("")
	public ResponseEntity<?> createUser(@RequestBody @Valid User u) {
		return ResponseHandler.handleDataResponses(uSrv.create(u), HttpStatus.OK);
	}

	/**
	 * Implements
	 * {@link com.Xogito.Assignment.Implementations.UserServiceImpl#update update()}
	 * method and returns a JSON response with the updated user.
	 * 
	 * @param id The user id.
	 * @param u  The new user property values.
	 * @return A JSON response with the updated user.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody @Valid User u) {
		return ResponseHandler.handleDataResponses(uSrv.update(id, u), HttpStatus.OK);
	}

	/**
	 * Implements
	 * {@link com.Xogito.Assignment.Implementations.UserServiceImpl#remove remove()}
	 * method and returns a JSON response of the removed user.
	 * 
	 * @param id The user id.
	 * @return A JSON response with the removed user.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> removeUser(@PathVariable("id") Long id) {
		return ResponseHandler.handleDataResponses(uSrv.remove(id), HttpStatus.OK);
	}
}
