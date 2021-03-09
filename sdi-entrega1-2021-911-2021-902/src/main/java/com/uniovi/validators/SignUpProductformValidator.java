package com.uniovi.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.uniovi.entities.Product;

@Component
public class SignUpProductformValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Product.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Product Product = (Product) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "Error.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "score", "Error.empty");

		if (Product.getDescription().length() < 20) {
			errors.rejectValue("description", "Error.signup.description.length");
		}

		if (Product.getScore() < 0 || Product.getScore() > 10) {
			errors.rejectValue("score", "Error.score.out.of.range");
		}

	}

}