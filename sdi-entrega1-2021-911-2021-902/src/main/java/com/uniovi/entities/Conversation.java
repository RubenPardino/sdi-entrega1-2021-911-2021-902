package com.uniovi.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

//@Entity
//public class Conversation {
//	@Id
//	@GeneratedValue
//	private Long id;
//	
//	@ManyToMany(cascade = CascadeType.ALL)
//	@JoinTable(name = "conversations_users",
//	joinColumns = { @JoinColumn(name = "conversation_id") },
//	inverseJoinColumns = { @JoinColumn(name = "user_id") }
//	)
//	private List<User> usuarios = new ArrayList<User>();
//	
//	@ElementCollection
//	private List<Message> mensajes = new ArrayList<Message>();
//	
//	public Conversation(User usuario1, User usuario2) {
//		usuarios.add(usuario1);
//		usuarios.add(usuario2);
//	}
//	
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public void addMessage(User user, String mensaje) {
//		mensajes.add(new Message(user,mensaje,LocalDateTime.now()));
//	}
//
//}
