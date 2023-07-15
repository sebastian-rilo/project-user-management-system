package com.Xogito.Assignment.Models;

import java.util.ArrayList;
import java.util.List;

import com.Xogito.Assignment.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The project entity Model
 */
@Entity
@Table(indexes = @Index(columnList = "name", unique = true))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, updatable = false, nullable = false)
	@JsonView(Views.coreDataView.class)
	private Long id;
	
	@NotBlank(message = "name must not be null nor empty")
	@JsonView(Views.coreDataView.class)
	private String name;
	
	@JsonView(Views.coreDataView.class)
	private String description;
	
	@JsonView(Views.fullDataView.class)
	@ManyToMany
	private List<@NotNull(message = "user must not be null") User> users = new ArrayList<>();
	
}
