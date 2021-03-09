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
import com.uniovi.validators.SignUpProductFormValidator;

@Controller
public class ProductsController {

	@Autowired
	private HttpSession httpSession;

	@Autowired // Inyectar el servicio
	private ProductsService ProductsService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private SignUpProductFormValidator signUpProductFormValidator;

	@RequestMapping("/product/list")
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
		model.addAttribute("productList", Products.getContent());
		model.addAttribute("page", Products);
		return "product/list";
	}

	@RequestMapping(value = "/product/add")
	public String getProduct(Model model) {
		model.addAttribute("usersList", usersService.getUsers());
		model.addAttribute("product", new Product());
		return "product/add";
	}

	@RequestMapping(value = "/product/add", method = RequestMethod.POST)
	public String setProduct(@Validated Product Product, BindingResult result, Model model) {
		signUpProductFormValidator.validate(Product, result);
		if (result.hasErrors()) {
			model.addAttribute("usersList", usersService.getUsers());
			return "product/add";
		}
		ProductsService.addProduct(Product);
		return "redirect:/product/list";
	}

	@RequestMapping("/product/details/{id}")
	public String getDetail(Model model, @PathVariable Long id) {
		model.addAttribute("product", ProductsService.getProduct(id));
		return "product/details";
	}

	@RequestMapping("/product/delete/{id}")
	public String deleteProduct(@PathVariable Long id) {
		ProductsService.deleteProduct(id);
		return "redirect:/product/list";
	}

	@RequestMapping(value = "/product/edit/{id}")
	public String getEdit(Model model, @PathVariable Long id) {
		model.addAttribute("product", ProductsService.getProduct(id));
		model.addAttribute("usersList", usersService.getUsers());
		return "product/edit";
	}

	@RequestMapping(value = "/product/edit/{id}", method = RequestMethod.POST)
	public String setEdit(Model model, @PathVariable Long id, @ModelAttribute Product Product) {
		Product original = ProductsService.getProduct(id);
		// modificar solo score y description
		original.setScore(Product.getScore());
		original.setDescription(Product.getDescription());
		ProductsService.addProduct(original);
		return "redirect:/product/details/" + id;
	}

	@RequestMapping("/product/list/update")
	public String updateList(Model model, Pageable pageable, Principal principal) {
		String dni = principal.getName(); // DNI es el name de la autenticación
		User user = usersService.getUserByDni(dni);
		Page<Product> Products = ProductsService.getProductsForUser(pageable, user);
		model.addAttribute("productList", Products.getContent());
		return "product/list :: tableProducts";
	}

	@RequestMapping(value = "/product/{id}/resend", method = RequestMethod.GET)
	public String setResendTrue(Model model, @PathVariable Long id) {
		ProductsService.setProductResend(true, id);
		return "redirect:/product/list";
	}

	@RequestMapping(value = "/product/{id}/noresend", method = RequestMethod.GET)
	public String setResendFalse(Model model, @PathVariable Long id) {
		ProductsService.setProductResend(false, id);
		return "redirect:/product/list";
	}
}