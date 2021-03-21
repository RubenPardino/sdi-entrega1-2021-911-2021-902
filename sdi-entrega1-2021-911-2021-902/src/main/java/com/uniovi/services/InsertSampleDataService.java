package com.uniovi.services;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uniovi.entities.Product;
import com.uniovi.entities.User;
import com.uniovi.repositories.ProductsRepository;

@Service
public class InsertSampleDataService {
	@Autowired
	private UsersService usersService;

	@Autowired
	private RolesService rolesService;
	
	@Autowired
	private ProductsRepository ProductsRepository;

	@PostConstruct
	public void init() {
		User user1 = new User("a@gmail.com", "Pedro", "Díaz");
		user1.setPassword("123456");
		user1.setRole(rolesService.getRoles()[0]);
		
		User user2 = new User("admin@email.com", "", "");
		user2.setPassword("admin");
		user2.setRole(rolesService.getRoles()[1]);
		
		User user3 = new User("b@gmail.com", "Marta", "Fernandez");
		user3.setPassword("123456");
		user3.setRole(rolesService.getRoles()[0]);
		
		User user4 = new User("c@gmail.com", "Gloria", "Lopez");
		user4.setPassword("123456");
		user4.setRole(rolesService.getRoles()[0]);
		
		User user5 = new User("d@gmail.com", "Domingo", "Sanchez");
		user5.setPassword("123456");
		user5.setRole(rolesService.getRoles()[0]);
		
		Product prueba = new Product("Helado", 4.0, "comida", user1);
		Product coche = new Product("Coche radiocontrol", 100.0, "Juguete", user1);
		Product calendario = new Product("Calendario", 3.0, "año 2021", user1);
		Product muñeca = new Product("Muñeca de juguete", 3.0, "Material porcelana", user1);
		
		Product a = new Product("Figura de Playmobil", 150.0, "Juguete de plástico", user3);
		Product b = new Product("Figura de Lego", 18.0, "Juguete de plástico", user3);
		Product c = new Product("Pantalón", 34.0, "Talla M", user3);
		
		Product d = new Product("Radio", 100.0, "am y fm", user4);
		Product e = new Product("Guitarra", 180.0, "madera de pino", user4);
		Product f = new Product("Estantería", 56.0, "Hecha de plastico", user4);
		
		Product g = new Product("Mancuerna", 80.0, "5 Kg", user5);
		Product h = new Product("Piano", 200.0, "Color blanco", user5);
		Product i = new Product("Reloj de bolsillo", 100.0, "Artesanal", user5);
		
	

		Set user1Products = new HashSet<Product>() {
			{
				add(prueba);
				add(coche);
				add(calendario);
				add(muñeca);

			}
		};
		user1.setProducts(user1Products);

		

		
		Set user3Products = new HashSet<Product>() {
			{
				add(a);
				add(b);
				add(c);
				

			}
		};
		user3.setProducts(user3Products);

		

		
	
		Set user4Products = new HashSet<Product>() {
			{
				add(d);
				add(e);
				add(f);

			}
		};
		user4.setProducts(user4Products);

		

		Set user5Products = new HashSet<Product>() {
			{
				add(g);
				add(h);
				add(i);
			}
		};
		user5.setProducts(user5Products);
		user5.setMoney(1.0);
		
		
		usersService.addUser(user1);	
		usersService.addUser(user2);
		usersService.addUser(user3);
		usersService.addUser(user4);
		usersService.addUser(user5);
		
		
		ProductsRepository.updateVendido(true, a.getId(), user1.getId());
		ProductsRepository.updateVendido(true, b.getId(), user1.getId());
		
		ProductsRepository.updateVendido(true, muñeca.getId(), user3.getId());
		ProductsRepository.updateVendido(true, coche.getId(), user3.getId());
		
		ProductsRepository.updateVendido(true, c.getId(), user4.getId());
		ProductsRepository.updateVendido(true, calendario.getId(), user4.getId());
		
		ProductsRepository.updateVendido(true, d.getId(), user5.getId());
		ProductsRepository.updateVendido(true, e.getId(), user5.getId());
		
		
		

	}
}