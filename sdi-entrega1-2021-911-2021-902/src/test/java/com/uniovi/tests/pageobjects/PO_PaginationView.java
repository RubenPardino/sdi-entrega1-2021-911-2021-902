package com.uniovi.tests.pageobjects;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_PaginationView extends PO_NavView {

	public static void goToLastPage(WebDriver driver) {
		List<WebElement> elementos;
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@class, 'page-link')]");
		elementos.get(2).click();
	}
}
