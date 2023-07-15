package com.Xogito.Assignment.Services;

import java.util.List;
import org.springframework.web.server.ResponseStatusException;
import com.Xogito.Assignment.Models.User;

/**
 * Service interface for the {@link com.Xogito.Assignment.Models.User User} Entity 
 */
public interface UserService {

	public List<User> findAll(int page, int size) throws ResponseStatusException;

	public List<User> findAllByName(String name, int page, int size) throws ResponseStatusException;

	public User findById(Long id) throws ResponseStatusException;

	public User findByEmail(String email) throws ResponseStatusException;

	public User create(User u) throws ResponseStatusException;

	public User update(Long id, User u) throws ResponseStatusException;

	public User remove(Long id) throws ResponseStatusException;

}
