package com.openuniversity.springjwt.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(	name = "users", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email") 
		})
@EntityListeners(AuditingEntityListener.class)
public class User extends Auditable<String>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank
	@Size(max = 20)
	@Column(name = "username")
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	@Column(name = "email")
	private String email;

	public InitialApplicant getInitialApplicant() {
		return initialApplicant;
	}

	public void setInitialApplicant(InitialApplicant initialApplicant) {
		this.initialApplicant = initialApplicant;
	}

	@NotBlank
	@Size(max = 120)
	@Column(name = "password")
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@OneToOne(cascade = CascadeType.ALL)
	InitialApplicant initialApplicant;

	public User() {
	}

	public User(String username, String email, String password, InitialApplicant initialApplicant) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.initialApplicant = initialApplicant;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}
