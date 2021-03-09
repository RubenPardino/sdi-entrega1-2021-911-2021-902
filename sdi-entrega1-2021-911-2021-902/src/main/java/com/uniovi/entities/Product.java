package com.uniovi.entities;

import javax.persistence.*;

@Entity
public class Product {
	@Id
	@GeneratedValue
	private Long id;
	private String description;
	private Double score;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Product(Long id, String description, Double score) {
		super();
		this.id = id;
		this.description = description;
		this.score = score;
	}

	public Product(String description, Double score, User user) {
		super();
		this.description = description;
		this.score = score;
		this.user = user;
	}

	public Product() {
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", description=" + description + ", score=" + score + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


}