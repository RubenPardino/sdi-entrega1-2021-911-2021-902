package com.uniovi.repositories;

import com.uniovi.entities.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UsersRepository extends CrudRepository<User, Long> {
	
	@Modifying
	@Transactional
	@Query("UPDATE User SET money = ?2 WHERE id = ?1")
	void updateUserMoney(Long id, double money);
	
	
	User findByEmail(String email);

	@Query("SELECT r FROM User r WHERE (LOWER (r.name) LIKE LOWER(?1) OR LOWER (r.lastName) LIKE LOWER(?1))")
	Page<User> searchUsersByNameOrSurname(Pageable pageable, String searchText);
	

	Page<User> findAll(Pageable pageable);
}