package com.uniovi.services;

import java.security.Principal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Product;
import com.uniovi.entities.User;
import com.uniovi.repositories.ProductsRepository;

@Service
public class ProductsService {

	@Autowired
	private ProductsRepository ProductsRepository;

	@Autowired
	private UsersService usersService;

	@Autowired
	private HttpSession httpSession;

	public Page<Product> getProducts(Pageable pageable) {
		Page<Product> Products = ProductsRepository.findAll(pageable);
		return Products;
	}

	public Page<Product> getProductsMenosPropios(Pageable pageable, User user) {
		Page<Product> Products = ProductsRepository.findAllMenosPropios(pageable, user);
		return Products;
	}

	public void addProduct(Product Product) {
// Si en Id es null le asignamos el ultimo + 1 de la lista
		ProductsRepository.save(Product);
	}

	public void deleteProduct(Long id) {
		ProductsRepository.deleteById(id);
	}

	public Product getProduct(Long id) {
		Set<Product> consultedList = (Set<Product>) httpSession.getAttribute("consultedList");
		if (consultedList == null) {
			consultedList = new HashSet<Product>();
		}
		Product obtainedProduct = ProductsRepository.findById(id).get();
		consultedList.add(obtainedProduct);
		httpSession.setAttribute("consultedList", consultedList);
		return obtainedProduct;
	}

	public void setProductVendido(Long id, Long userId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User user = usersService.getUserByEmail(email);
		Product Product = ProductsRepository.findById(id).get();
		if (!Product.getUser().getEmail().equals(email)) {
			ProductsRepository.updateVendido(true, id, userId);

		}
	}

	public void updateMoney(Long id, Principal principal) {
		Product p = getProduct(id);
		Double money = p.getMoney();
		String email = principal.getName();
		User user = usersService.getUserByEmail(email);
		Double dineroCuenta = user.getMoney();

		if (dineroCuenta >= money) {
			user.setMoney(dineroCuenta - money);
			setProductVendido(id, user.getId());
		}

	}

	public boolean noPuedeComprar(Long id, Principal principal) {

		Product p = getProduct(id);
		Double money = p.getMoney();
		String email = principal.getName();
		User user = usersService.getUserByEmail(email);
		Double dineroCuenta = user.getMoney();

		return dineroCuenta < money;

	}

	public Page<Product> getProductsForUser(Pageable pageable, User user) {
		Page<Product> Products = new PageImpl<Product>(new LinkedList<Product>());
		Products = ProductsRepository.findAllByUser(pageable, user);
		return Products;
	}

	public Page<Product> searchProductsByTitle(Pageable pageable, String searchText, User user) {
		Page<Product> Products = new PageImpl<Product>(new LinkedList<Product>());
		searchText = "%" + searchText + "%";
		if (user.getRole().equals("ROLE_ESTANDAR") || user.getRole().equals("ROLE_ADMIN")) {
			Products = ProductsRepository.searchByTitle(pageable, searchText);
		}
		return Products;
	}

	public Page<Product> searchProductsByTitleMenosPropios(Pageable pageable, String searchText, User user) {
		Page<Product> Products = new PageImpl<Product>(new LinkedList<Product>());
		searchText = "%" + searchText + "%";
		if (user.getRole().equals("ROLE_ESTANDAR") || user.getRole().equals("ROLE_ADMIN")) {
			Products = ProductsRepository.searchByTitleMenosPropios(pageable, searchText, user);
		}
		return Products;
	}

	public Page<Product> searchProductsByTitleAndUser(Pageable pageable, String searchText, User user) {
		Page<Product> Products = new PageImpl<Product>(new LinkedList<Product>());
		searchText = "%" + searchText + "%";
		if (user.getRole().equals("ROLE_ESTANDAR") || user.getRole().equals("ROLE_ADMIN")) {
			Products = ProductsRepository.searchByTitleAndUser(pageable, searchText, user);
		}
		return Products;
	}

	public Page<Product> getBoughtProducts(Pageable pageable, Principal principal) {
		Page<Product> products = new PageImpl<Product>(new LinkedList<Product>());
		String email = principal.getName();
		User user = usersService.getUserByEmail(email);
		products = ProductsRepository.searchByBuyerUser(pageable, user.getId());
		return products;
	}
	
	public void highlightOffer(Long id) {
		ProductsRepository.highlightProduct(true, id);
	}
	
	public List<Product> getProductosDestacados() {
		return ProductsRepository.searchDestacados();
	}
}