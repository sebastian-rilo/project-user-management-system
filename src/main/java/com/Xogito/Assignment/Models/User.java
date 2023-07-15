package com.Xogito.Assignment.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * the User entity model
 */
@Entity
@Table(indexes = @Index(columnList = "email", unique = true))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, updatable = false, nullable = false)
	private Long id;

	@NotBlank(message = "name must not be null nor empty")
	private String name;

	@NotNull(message = "email must not be null")
	@Email(message = "email must be a valid email address")
	private String email;
}
