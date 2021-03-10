package com.uniovi.services;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Product;
import com.uniovi.entities.User;
import com.uniovi.repositories.ProductsRepository;

@Service
public class ProductsService {
	@Autowired
	private ProductsRepository ProductsRepository;

	@Autowired
	private HttpSession httpSession;

	public Page<Product> getProducts(Pageable pageable) {
		Page<Product> Products = ProductsRepository.findAll(pageable);
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

//	public void setProductResend(boolean revised, Long id) {
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		String dni = auth.getName();
//		Product Product = ProductsRepository.findById(id).get();
//		if (Product.getUser().getDni().equals(dni)) {
//			ProductsRepository.updateResend(revised, id);
//		}
//	}

	public Page<Product> getProductsForUser(Pageable pageable, User user) {
		Page<Product> Products = new PageImpl<Product>(new LinkedList<Product>());
		Products = ProductsRepository.findAllByUser(pageable, user);
		return Products;
	}

	public Page<Product> searchProductsByDescriptionAndNameForUser(Pageable pageable, String searchText, User user) {
		Page<Product> Products = new PageImpl<Product>(new LinkedList<Product>());
		searchText = "%" + searchText + "%";
		if (user.getRole().equals("ROLE_STUDENT")) {
			Products = ProductsRepository.searchByDescriptionNameAndUser(pageable, searchText, user);
		}
		if (user.getRole().equals("ROLE_PROFESSOR")) {
			Products = ProductsRepository.searchByDescriptionAndName(pageable, searchText);
		}
		return Products;
	}
}