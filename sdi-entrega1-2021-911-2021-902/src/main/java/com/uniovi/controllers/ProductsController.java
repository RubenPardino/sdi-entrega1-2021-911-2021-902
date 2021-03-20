package com.uniovi.controllers;

import java.security.Principal;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
import com.uniovi.validators.SignUpProductFormValidator;

@Controller
public class ProductsController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private HttpSession session;

	@Autowired // Inyectar el servicio
	private ProductsService ProductsService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private SignUpProductFormValidator signUpProductFormValidator;

	@RequestMapping("/product/list")
	public String getList(Model model, Pageable pageable, Principal principal,
			@RequestParam(value = "", required = false) String searchText, HttpServletRequest request) {
		String email = principal.getName();
		User user = usersService.getUserByEmail(email);
		Page<Product> Products = new PageImpl<Product>(new LinkedList<Product>());

		if (searchText != null && !searchText.isEmpty()) {
			Products = ProductsService.searchProductsByTitleMenosPropios(pageable, searchText, user);
		} else {
			Products = ProductsService.getProductsMenosPropios(pageable, user);
		}

		model.addAttribute("productList", Products.getContent());
		model.addAttribute("page", Products);

		log.info(messageSource.getMessage("log.product.list", null, LocaleContextHolder.getLocale()) + user.getId());

		return "product/list";
	}

	@RequestMapping("/product/list/compradas")
	public String getListCompradas(Model model, Pageable pageable, Principal principal,
			@RequestParam(value = "", required = false) String searchText) {
		String email = principal.getName();
		User user = usersService.getUserByEmail(email);
		Page<Product> Products = new PageImpl<Product>(new LinkedList<Product>());

		if (searchText != null && !searchText.isEmpty()) {
			Products = ProductsService.searchProductsByTitle(pageable, searchText, user);
		} else {
			Products = ProductsService.getBoughtProducts(pageable, principal);
		}

		log.info(messageSource.getMessage("log.product.listcompradas", null, LocaleContextHolder.getLocale())
				+ user.getId());

		model.addAttribute("productList", Products.getContent());
		model.addAttribute("page", Products);
		return "product/listCompradas";
	}

	@RequestMapping("/product/myList")
	public String getMyList(Model model, Pageable pageable, Principal principal,
			@RequestParam(value = "", required = false) String searchText) {
		String email = principal.getName();
		User user = usersService.getUserByEmail(email);
		Page<Product> Products = new PageImpl<Product>(new LinkedList<Product>());

		if (searchText != null && !searchText.isEmpty()) {
			Products = ProductsService.searchProductsByTitleAndUser(pageable, searchText, user);
		} else {
			Products = ProductsService.getProductsForUser(pageable, user);
		}
		model.addAttribute("productMyList", Products.getContent());
		model.addAttribute("page", Products);
		log.info(messageSource.getMessage("log.product.mylist", null, LocaleContextHolder.getLocale()) + user.getId());

		return "product/myList";
	}

	@RequestMapping(value = "/product/add")
	public String getProduct(Model model) {
		// model.addAttribute("usersList", usersService.getUsers());
		model.addAttribute("product", new Product());
		return "product/add";
	}

	@RequestMapping(value = "/product/add", method = RequestMethod.POST)
	public String setProduct(@Validated Product Product, BindingResult result, Model model, Principal principal) {
		signUpProductFormValidator.validate(Product, result);
		if (result.hasErrors()) {
			// model.addAttribute("usersList", usersService.getUsers());

			return "product/add";
		}

		String email = principal.getName();
		User user = usersService.getUserByEmail(email);
		Product.setUser(user);
		
		if (Product.getDestacado()) {
			user.setMoney(user.getMoney()-20);
			session.removeAttribute("user");
			session.setAttribute("user", user);
		}

		ProductsService.addProduct(Product);
		log.info(messageSource.getMessage("log.product.add", null, LocaleContextHolder.getLocale()));

		return "redirect:/product/myList";
	}

	@RequestMapping("/product/details/{id}")
	public String getDetail(Model model, @PathVariable Long id) {
		model.addAttribute("product", ProductsService.getProduct(id));
		log.info(messageSource.getMessage("log.product.details", null, LocaleContextHolder.getLocale()) + id);

		return "product/details";
	}

	@RequestMapping("/product/delete/{id}")
	public String deleteProduct(@PathVariable Long id) {
		ProductsService.deleteProduct(id);
		log.info(messageSource.getMessage("log.product.delete", null, LocaleContextHolder.getLocale()) + id);

		return "redirect:/product/myList";
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
		original.setMoney(Product.getMoney());
		original.setTitle(Product.getTitle());
		original.setDescription(Product.getDescription());
		ProductsService.addProduct(original);
		log.info(messageSource.getMessage("log.product.edit", null, LocaleContextHolder.getLocale()) + id);
		return "redirect:/product/details/" + id;
	}

	@RequestMapping("/product/list/update")
	public String updateList(Model model, Pageable pageable, Principal principal) {
		String email = principal.getName();
		User user = usersService.getUserByEmail(email);
		Page<Product> Products = ProductsService.getProductsMenosPropios(pageable, user);
		model.addAttribute("productList", Products.getContent());
		return "product/list :: tableProducts";
	}

	@RequestMapping("/product/myList/update")
	public String updateMyList(Model model, Pageable pageable, Principal principal) {
		String email = principal.getName();
		User user = usersService.getUserByEmail(email);
		Page<Product> Products = ProductsService.getProductsForUser(pageable, user);
		model.addAttribute("productMyList", Products.getContent());
		return "product/myList :: tableProducts";
	}

	@RequestMapping(value = "/product/{id}/vendido", method = RequestMethod.GET)
	public String setVendidoTrue(Model model, Principal principal, @PathVariable Long id, String error,
			HttpServletRequest request) {

		session = request.getSession();

		if (session.getAttribute("sinsaldo") != null) {
			session.removeAttribute("sinsaldo");
		}

		if (ProductsService.noPuedeComprar(id, principal)) {
			session.setAttribute("sinsaldo",
					messageSource.getMessage("Error.buy.no.money", null, LocaleContextHolder.getLocale()));
			log.info(messageSource.getMessage("Error.buy.no.money", null, LocaleContextHolder.getLocale()) + id);


		} else {
			ProductsService.updateMoney(id, principal);
			String email = principal.getName();
			User user = usersService.getUserByEmail(email);
			session.removeAttribute("user");
			session.setAttribute("user", user);
			log.info(messageSource.getMessage("log.product.vendido", null, LocaleContextHolder.getLocale()) + id);

		}

		return "redirect:/product/list";
	}
	
	@RequestMapping(value = "/product/highlight/{id}")
	public String setHighlight(Model model, @PathVariable Long id) {
		ProductsService.highlightOffer(id);
		User user = (User) session.getAttribute("user");
		user.setMoney(user.getMoney()-20);
		usersService.updateUserMoney(user.getId(), user.getMoney());
		session.removeAttribute("user");
		session.setAttribute("user", user);
		return "redirect:/product/myList";
	}

}
