package com.uniovi.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.uniovi.entities.Product;
import com.uniovi.entities.User;

@Component
public class BuyValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
			
	}
	
	public void validate(Object product, Object user, Errors errors) {
		Product producto = (Product) product;
		User usuario = (User) user;
		if (usuario.getMoney()<producto.getMoney())
			errors.rejectValue("tableProducts", "Error.buy.no.money");
	}

}
