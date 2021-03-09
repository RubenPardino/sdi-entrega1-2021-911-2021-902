package com.uniovi.entities;

import javax.persistence.*;
import java.util.Set; //A collection that contains no duplicate elements

@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue
	private long id;
	@Column(unique = true)
	private String dni;
	private String name;

	private String password;
	@Transient // propiedad que no se almacena e la tabla.
	private String passwordConfirm;

	private String role;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Product> Products;

	public User(String dni, String name) {
		super();
		this.dni = dni;
		this.name = name;
	}

	public User() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void setProducts(Set<Product> Products) {
		this.Products = Products;
	}

	public Set<Product> getProducts() {
		return Products;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}