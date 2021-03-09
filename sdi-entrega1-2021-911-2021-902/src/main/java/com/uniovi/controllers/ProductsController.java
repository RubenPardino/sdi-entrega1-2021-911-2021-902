package com.uniovi.controllers;

import java.security.Principal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.uniovi.entities.Product;
import com.uniovi.entities.User;
import com.uniovi.services.*;
import com.uniovi.validators.SignUpFormValidator;

@Controller
public class ProductsController {

	@Autowired
	private HttpSession httpSession;

	@Autowired // Inyectar el servicio
	private ProductsService ProductsService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private SignUpFormValidator signUpFormValidator;

	@RequestMapping("/Product/list")
	public String getList(Model model, Pageable pageable, Principal principal,
			@RequestParam(value = "", required = false) String searchText) {
		String dni = principal.getName(); // DNI es el name de la autenticación
		User user = usersService.getUserByDni(dni);
		Page<Product> Products = new PageImpl<Product>(new LinkedList<Product>());

		if (searchText != null && !searchText.isEmpty()) {
			Products = ProductsService.searchProductsByDescriptionAndNameForUser(pageable, searchText, user);
		} else {
			Products = ProductsService.getProductsForUser(pageable, user);
		}
		model.addAttribute("ProductList", Products.getContent());
		model.addAttribute("page", Products);
		return "Product/list";
	}

	@RequestMapping(value = "/Product/add")
	public String getProduct(Model model) {
		model.addAttribute("usersList", usersService.getUsers());
		model.addAttribute("Product", new Product());
		return "Product/add";
	}

	@RequestMapping(value = "/Product/add", method = RequestMethod.POST)
	public String setProduct(@Validated Product Product, BindingResult result, Model model) {
		signUpFormValidator.validate(Product, result);
		if (result.hasErrors()) {
			model.addAttribute("usersList", usersService.getUsers());
			return "Product/add";
		}
		ProductsService.addProduct(Product);
		return "redirect:/Product/list";
	}

	@RequestMapping("/Product/details/{id}")
	public String getDetail(Model model, @PathVariable Long id) {
		model.addAttribute("Product", ProductsService.getProduct(id));
		return "Product/details";
	}

	@RequestMapping("/Product/delete/{id}")
	public String deleteProduct(@PathVariable Long id) {
		ProductsService.deleteProduct(id);
		return "redirect:/Product/list";
	}

	@RequestMapping(value = "/Product/edit/{id}")
	public String getEdit(Model model, @PathVariable Long id) {
		model.addAttribute("Product", ProductsService.getProduct(id));
		model.addAttribute("usersList", usersService.getUsers());
		return "Product/edit";
	}

	@RequestMapping(value = "/Product/edit/{id}", method = RequestMethod.POST)
	public String setEdit(Model model, @PathVariable Long id, @ModelAttribute Product Product) {
		Product original = ProductsService.getProduct(id);
		// modificar solo score y description
		original.setScore(Product.getScore());
		original.setDescription(Product.getDescription());
		ProductsService.addProduct(original);
		return "redirect:/Product/details/" + id;
	}

	@RequestMapping("/Product/list/update")
	public String updateList(Model model, Pageable pageable, Principal principal) {
		String dni = principal.getName(); // DNI es el name de la autenticación
		User user = usersService.getUserByDni(dni);
		Page<Product> Products = ProductsService.getProductsForUser(pageable, user);
		model.addAttribute("ProductList", Products.getContent());
		return "Product/list :: tableProducts";
	}

	@RequestMapping(value = "/Product/{id}/resend", method = RequestMethod.GET)
	public String setResendTrue(Model model, @PathVariable Long id) {
		ProductsService.setProductResend(true, id);
		return "redirect:/Product/list";
	}

	@RequestMapping(value = "/Product/{id}/noresend", method = RequestMethod.GET)
	public String setResendFalse(Model model, @PathVariable Long id) {
		ProductsService.setProductResend(false, id);
		return "redirect:/Product/list";
	}
}
