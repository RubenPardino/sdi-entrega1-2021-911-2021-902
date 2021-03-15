package com.uniovi.entities;

import java.time.LocalDate;

import javax.persistence.*;

@Entity
public class Product {
	@Id
	@GeneratedValue
	private Long id;
	private String title;
	private LocalDate date;
	private Double money;
	private String description;
	private Boolean vendido = false;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Product(Long id, String title, Double money, String description, User user) {
		super();
		this.id = id;
		this.title = title;
		this.money = money;
		this.description = description;
		this.user = user;
		date = java.time.LocalDate.now();
	}

	public Product(String title, Double money, String description, User user) {
		super();
		this.title = title;
		this.money = money;
		this.description = description;
		this.user = user;
		date = java.time.LocalDate.now();

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Product() {
		date = java.time.LocalDate.now();
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}