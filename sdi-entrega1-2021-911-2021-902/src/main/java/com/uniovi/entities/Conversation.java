package com.uniovi.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Conversation {
	@Id
	@GeneratedValue
	private Long id;
	private User usuario1;
	private User usuario2;
	
	@ElementCollection
	private List<Message> mensajes = new ArrayList<Message>();
	
	public Conversation(User usuario1, User usuario2) {
		this.usuario1 = usuario1;
		this.usuario2 = usuario2;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUsuario1() {
		return usuario1;
	}

	public void setUsuario1(User usuario1) {
		this.usuario1 = usuario1;
	}

	public User getUsuario2() {
		return usuario2;
	}

	public void setUsuario2(User usuario2) {
		this.usuario2 = usuario2;
	}

	public void addMessage(User user, String mensaje) {
		mensajes.add(new Message(user,mensaje,LocalDateTime.now()));
	}

}
