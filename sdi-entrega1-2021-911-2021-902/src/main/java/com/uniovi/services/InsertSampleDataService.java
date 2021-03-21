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
				add(new Product("Coche radiocontrol", 100.0, "Juguete", user1));
				add(new Product("Calendario", 3.0, "año 2021", user1));
				add(new Product("Muñeca de juguete", 3.0, "Material porcelana", user1));

			}
		};
		user1.setProducts(user1Products);

		usersService.addUser(user1);

		
		
		User user2 = new User("admin@email.com", "", "");
		user2.setPassword("admin");
		user2.setRole(rolesService.getRoles()[1]);

		usersService.addUser(user2);

		
		
		User user3 = new User("b@gmail.com", "Marta", "Fernandez");
		user3.setPassword("123456");
		user3.setRole(rolesService.getRoles()[0]);

		Set user3Products = new HashSet<Product>() {
			{
				add(new Product("Figura de Playmobil", 150.0, "Juguete de plástico", user3));
				add(new Product("Figura de Lego", 18.0, "Juguete de plástico", user3));
				add(new Product("Pantalón", 34.0, "Talla M", user3));
				

			}
		};
		user3.setProducts(user3Products);

		usersService.addUser(user3);

		
		

		User user4 = new User("c@gmail.com", "Gloria", "Lopez");
		user4.setPassword("123456");
		user4.setRole(rolesService.getRoles()[0]);

		Set user4Products = new HashSet<Product>() {
			{
				add(new Product("Radio", 100.0, "am y fm", user4));
				add(new Product("Guitarra", 180.0, "madera de pino", user4));
				add(new Product("Estantería", 56.0, "Hecha de plastico", user4));

			}
		};
		user4.setProducts(user4Products);

		usersService.addUser(user4);
		
		User user5 = new User("d@gmail.com", "Domingo", "Sanchez");
		user5.setPassword("123456");
		user5.setRole(rolesService.getRoles()[0]);
		
		Product productComprado1 = new Product("Flauta", 10.0, "Instrumento de viento", user5);
		productComprado1.setComprador(user1.getId());
		productComprado1.setVendido(true);
		Product productComprado2 = new Product("Pelota", 20.0, "Balón reglamentario", user5);
		productComprado2.setComprador(user1.getId());
		productComprado2.setVendido(true);

		Set user5Products = new HashSet<Product>() {
			{
				add(new Product("Mancuerna", 80.0, "5 Kg", user5));
				add(new Product("Piano", 200.0, "Color blanco", user5));
				add(new Product("Reloj de bolsillo", 60.0, "Artesanal", user5));
				add(productComprado1);
				add(productComprado2);
			}
		};
		user5.setProducts(user5Products);
		user5.setMoney(1.0);

		usersService.addUser(user5);
		
		

	}
}