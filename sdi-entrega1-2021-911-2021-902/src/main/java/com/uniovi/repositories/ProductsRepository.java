package com.uniovi.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.uniovi.entities.Product;
import com.uniovi.entities.User;

public interface ProductsRepository extends CrudRepository<Product, Long> {
	@Modifying
	@Transactional
	@Query("UPDATE Product SET resend = ?1 WHERE id = ?2")
	void updateResend(Boolean resend, Long id);

	@Query("SELECT r FROM Product r WHERE r.user = ?1 ORDER BY r.id ASC ")
	Page<Product> findAllByUser(Pageable pageable, User user);

	@Query("SELECT r FROM Product r WHERE (LOWER (r.description) LIKE LOWER(?1) OR LOWER(r.user.name) LIKE LOWER(?1))")
	Page<Product> searchByDescriptionAndName(Pageable pageable, String searchtext);

	@Query("SELECT r FROM Product r WHERE (LOWER (r.description) LIKE LOWER(?1) OR LOWER(r.user.name) LIKE LOWER(?1)) AND r.user = ?2 ")
	Page<Product> searchByDescriptionNameAndUser(Pageable pageable, String searchtext, User user);

	Page<Product> findAll(Pageable pageable);
}