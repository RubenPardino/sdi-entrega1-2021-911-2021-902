package com.uniovi.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.uniovi.entities.Product;

@Component
public class SignUpProductFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Product.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Product product = (Product) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "Error.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "score", "Error.empty");

		if (product.getDescription().length() < 20) {
			errors.rejectValue("description", "Error.addmark.description.length");
		}

		if (product.getScore() < 0 || product.getScore() > 10) {
			errors.rejectValue("score", "Error.addmark.score");
		}

	}

}