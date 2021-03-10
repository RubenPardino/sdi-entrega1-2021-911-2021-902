package com.uniovi.services;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uniovi.entities.Product;
import com.uniovi.entities.User;

@Service
public class InsertSampleDataService {
	@Autowired
	private UsersService usersService;

	@Autowired
	private RolesService rolesService;

	@PostConstruct
	public void init() {
		User user1 = new User("a@gmail.com", "Pedro", "Díaz");
		user1.setPassword("123456");
		user1.setRole(rolesService.getRoles()[0]);

		Set user1Products = new HashSet<Product>() {
			{
				add(new Product("Nota A1", 10.0, user1));

			}
		};
		user1.setProducts(user1Products);

		usersService.addUser(user1);

		User user2 = new User("b@gmail.com", "Daniel", "Pérez");
		user2.setPassword("123456");
		user2.setRole(rolesService.getRoles()[1]);

		Set user2Products = new HashSet<Product>() {
			{
				add(new Product("Nota A2", 8.0, user2));

			}
		};
		user2.setProducts(user2Products);

		usersService.addUser(user2);

	}
}