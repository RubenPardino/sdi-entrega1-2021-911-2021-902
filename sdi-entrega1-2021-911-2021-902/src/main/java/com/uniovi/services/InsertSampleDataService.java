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
		User user1 = new User("99999990A", "Pedro");
		user1.setPassword("123456");
		user1.setRole(rolesService.getRoles()[0]);
		
		

		Set user1Products = new HashSet<Product>() {
			{
				add(new Product("Nota A1", 10.0, user1));
				add(new Product("Nota A2", 9.0, user1));
				add(new Product("Nota A3", 7.0, user1));
				add(new Product("Nota A4", 6.5, user1));
			}
		};
		user1.setProducts(user1Products);
		
		usersService.addUser(user1);
	
	}
}