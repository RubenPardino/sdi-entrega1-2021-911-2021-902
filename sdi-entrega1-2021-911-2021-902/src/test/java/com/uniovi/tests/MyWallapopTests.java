package com.uniovi.tests;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.uniovi.entities.Product;
import com.uniovi.entities.User;
import com.uniovi.repositories.UsersRepository;
import com.uniovi.services.InsertSampleDataService;
import com.uniovi.services.RolesService;
import com.uniovi.services.UsersService;
import com.uniovi.tests.pageobjects.PO_AddProductView;
import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_PaginationView;
import com.uniovi.tests.pageobjects.PO_Properties;
import com.uniovi.tests.pageobjects.PO_RegisterView;
import com.uniovi.tests.pageobjects.PO_SearchView;
import com.uniovi.tests.pageobjects.PO_View;
import com.uniovi.tests.util.SeleniumUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.runners.MethodSorters;

//Ordenamos las pruebas por el nombre del métodos

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyWallapopTests {

	@Autowired
	private InsertSampleDataService isds;

	@Autowired
	private UsersRepository usersRepository;

	// En Windows (Debe ser la versión 65.0.1 y desactivar las actualizacioens
	// automáticas)):
	static String PathFirefox65 = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
	static String Geckdriver024 = "C:\\Users\\jk236\\Downloads\\PL-SDI-Sesión5-material\\PL-SDI-Sesión5-material\\geckodriver024win64.exe";
	// En MACOSX (Debe ser la versión 65.0.1
	// y desactivar las actualizacioens automáticas):
	// static String PathFirefox65 =
	// "/Applications/Firefox.app/Contents/MacOS/firefox-bin";
	// static String Geckdriver024 = "/Users/delacal/selenium/geckodriver024mac";
	// //Común aWindows y a MACOSX
	static WebDriver driver = getDriver(PathFirefox65, Geckdriver024);
	static String URL = "http://localhost:8090";

	public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckdriver);
		WebDriver driver = new FirefoxDriver();
		return driver;
	} /* Resto del código de la clase */

	// Antes de cada prueba se navega al URL home de la aplicación
	@Before
	public void setUp() {
		initDB();
		driver.navigate().to(URL);
	}

	private void initDB() {
		// Borramos todas las entidades.
		usersRepository.deleteAll();
		// Ahora las volvemos a crear
		isds.init();
	}

	// Después de cada prueba se borran las cookies del navegador
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	} // Antes de la primera prueba

	@BeforeClass
	static public void begin() {
	} // Al finalizar la última prueba

	@AfterClass
	static public void end() { // Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

	// Registro de Usuario con datos válidos
	@Test
	public void PR01() {
		initDB();
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "test@gmail.com", "Jonathan", "Barbon", "123456", "123456");
		// Comprobamos que entramos en la sección privada
		PO_View.checkElement(driver, "text", "Bienvenidos a la pagina principal");
		PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
		PO_View.checkElement(driver, "text", "Identificate");
	}

	// Registro de Usuario con datos inválidos (email vacío, nombre vacío, apellidos
	// vacíos)
	@Test
	public void PR02() {
		initDB();

		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "", "", "", "123456", "123456");
		PO_RegisterView.checkKey(driver, "Error.singup.email", PO_Properties.getSPANISH());
		PO_RegisterView.checkKey(driver, "Error.signup.name.length", PO_Properties.getSPANISH());
		PO_RegisterView.checkKey(driver, "Error.signup.lastName.length", PO_Properties.getSPANISH());

	}

	// Registro de Usuario con datos inválidos (repetición de contraseña inválida)
	@Test
	public void PR03() {
		initDB();

		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "test@gmail.com", "Jonathan", "Barbon", "123456", "123457");
		PO_RegisterView.checkKey(driver, "Error.signup.passwordConfirm.coincidence", PO_Properties.getSPANISH());

	}

	// Registro de Usuario con datos inválidos (email existente).
	@Test
	public void PR04() {
		initDB();

		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "a@gmail.com", "Jonathan", "Barbon", "123456", "123456");
		PO_RegisterView.checkKey(driver, "Error.signup.email.duplicate", PO_Properties.getSPANISH());
		// Comprobamos que entramos en la sección privada
		PO_RegisterView.checkElement(driver, "text", "Identificate");
	}

	// Inicio de sesión con datos válidos (administrador)
	@Test
	public void PR05() {
		initDB();

		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		PO_View.checkElement(driver, "text", "Bienvenidos a la pagina principal");

	}

	// Inicio de sesión con datos válidos (usuario estándar
	@Test
	public void PR06() {
		initDB();

		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_LoginView.fillForm(driver, "a@gmail.com", "123456");
		PO_LoginView.checkElement(driver, "text", "Bienvenidos a la pagina principal");

	}

	// Inicio de sesión con datos inválidos (usuario estándar, campo email y
	// contraseña vacíos
	@Test
	public void PR07() {
		initDB();

		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_LoginView.fillForm(driver, "", "");
		PO_RegisterView.checkKey(driver, "Error.login", PO_Properties.getSPANISH());

	}

	// Inicio de sesión con datos válidos (usuario estándar, email existente, pero
	// contraseña incorrecta).
	@Test
	public void PR08() {
		initDB();

		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_LoginView.fillForm(driver, "a@gmail.com", "1234567");
		PO_RegisterView.checkKey(driver, "Error.login", PO_Properties.getSPANISH());

	}

	// Inicio de sesión con datos inválidos (usuario estándar, email no existente en
	// la aplicación).
	@Test
	public void PR09() {
		initDB();

		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_LoginView.fillForm(driver, "noexiste@gmail.com", "123456");
		PO_RegisterView.checkKey(driver, "Error.login", PO_Properties.getSPANISH());

	}

	// Hacer click en la opción de salir de sesión y comprobar que se redirige a la
	// página de inicio de sesión (Login)
	@Test
	public void PR10() {
		initDB();

		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_LoginView.fillForm(driver, "a@gmail.com", "123456");
		// Comprobamos que entramos en la sección privada
		PO_View.checkElement(driver, "text", "Bienvenidos a la pagina principal");
		PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
		PO_View.checkElement(driver, "text", "Identificate");

	}

	// Comprobar que el botón cerrar sesión no está visible si el usuario no está
	// autenticado.
	@Test
	public void PR11() {
		initDB();

		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_View.checkElement(driver, "text", "Identificate");

	}

	// Mostrar el listado de usuarios y comprobar que se muestran to dos los que
	// existen en el sistema.
	@Test
	public void PR12() {
		initDB();

		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id, 'users-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'user/list')]");
		elementos.get(0).click();
		PO_View.checkElement(driver, "text", "a@gmail.com");
		PO_View.checkElement(driver, "text", "b@gmail.com");

	}

	// Ir al formulario de alta de oferta, rellenarla con datos válidos y pulsar el
	// botón Submit. Comprobar que la oferta sale en el listado de ofertas de dicho
	// usuario
	@Test
	public void PR16() {
		initDB();

		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_LoginView.fillForm(driver, "a@gmail.com", "123456");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id, 'products-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'product/add')]");
		elementos.get(0).click();
		PO_AddProductView.fillForm(driver, "Camiseta", "camiseta azul de seda", "20");
		PO_View.checkElement(driver, "text", "Camiseta");

	}

	// Ir al formulario de alta de oferta, rellenarla con datos inválidos (campo
	// título vacío) y pulsar el botón Submit. Comprobar que se muestra el mensaje
	// de campo obligatorio.
	@Test
	public void PR17() {
		initDB();

		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_LoginView.fillForm(driver, "a@gmail.com", "123456");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id, 'products-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'product/add')]");
		elementos.get(0).click();
		PO_AddProductView.fillForm(driver, "Cam", "camiseta azul de seda", "20");
		PO_RegisterView.checkKey(driver, "Error.addmark.title.length", PO_Properties.getSPANISH());
		PO_AddProductView.fillForm(driver, "Camiseta", "camiseta", "20");
		PO_RegisterView.checkKey(driver, "Error.addmark.description.length", PO_Properties.getSPANISH());
		PO_AddProductView.fillForm(driver, "Camiseta", "camiseta azul de seda", "0");
		PO_RegisterView.checkKey(driver, "Error.addmark.score", PO_Properties.getSPANISH());

	}

	// Mostrar el listado de ofertas para dicho usuario y comprobar que se muestran
	// todas los que existen para este usuario.
	@Test
	public void PR18() {
		initDB();

		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_LoginView.fillForm(driver, "a@gmail.com", "123456");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id, 'products-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'product/myList')]");
		elementos.get(0).click();
		PO_View.checkElement(driver, "text", "Coche de juguete");
		PO_View.checkElement(driver, "text", "Calendario");

	}

	// Ir a la lista de ofertas, borrar la primera oferta de la lista, comprobar
	// que la lista se actualiza y que la oferta desaparece
	@Test
	public void PR19() {
		initDB();

		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_LoginView.fillForm(driver, "a@gmail.com", "123456");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id, 'products-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'product/myList')]");
		elementos.get(0).click();
		String tituloAnt = PO_View.checkElement(driver, "free", "//table/tbody/tr/td[2]").get(0).getText();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'product/delete')]");
		elementos.get(0).click();
		SeleniumUtils.EsperaCargaPaginaNoTexto(driver, tituloAnt, PO_View.getTimeout());

	}

	// Ir a la lista de ofertas, borrar la última oferta de la lista, comprobar que
	// la lista se actualiza y que la oferta desaparece
	@Test
	public void PR20() {
		initDB();

		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_LoginView.fillForm(driver, "a@gmail.com", "123456");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id, 'products-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'product/myList')]");
		elementos.get(0).click();
		PO_PaginationView.goToLastPage(driver);
		elementos = PO_View.checkElement(driver, "free", "//table/tbody/tr/td[2]");
		String tituloAnt = elementos.get(elementos.size() - 1).getText();

		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'product/delete')]");
		elementos.get(elementos.size() - 1).click();
		PO_PaginationView.goToLastPage(driver);
		SeleniumUtils.EsperaCargaPaginaNoTexto(driver, tituloAnt, PO_View.getTimeout());
	}

	// Hacer una búsqueda con el campo vacío y comprobar que se muestra la página
	// que corresponde con el listado de las ofertas existentes en el sistema
	@Test
	public void PR21() {
		initDB();

		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_LoginView.fillForm(driver, "a@gmail.com", "123456");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id, 'products-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'product/list')]");
		elementos.get(0).click();
		PO_SearchView.fillForm(driver, "");
		PO_View.checkElement(driver, "text", "Coche de juguete");
		PO_View.checkElement(driver, "text", "Calendario");
		PO_View.checkElement(driver, "text", "Figura de Playmobil");

	}

	// Hacer una búsqueda escribiendo en el campo un texto que no exista y comprobar
	// que se muestra la página que corresponde, con la lista de ofertas vacía
	@Test
	public void PR22() {
		initDB();

		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_LoginView.fillForm(driver, "a@gmail.com", "123456");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id, 'products-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'product/list')]");
		elementos.get(0).click();
		PO_SearchView.fillForm(driver, "noexiste");
		SeleniumUtils.EsperaCargaPaginaNoTexto(driver, "Coche de juguete", PO_View.getTimeout());
		SeleniumUtils.EsperaCargaPaginaNoTexto(driver, "Calendario", PO_View.getTimeout());
		SeleniumUtils.EsperaCargaPaginaNoTexto(driver, "Figura de Playmobil", PO_View.getTimeout());
	}

	// Registro de Usuario con datos válidos
	@Test
	public void PR28() {
		initDB();

		// Intentar acceder sin estar autenticado a la opción de listado de usuarios del
		// administrador. Se deberá volver al formulario de login

		driver.navigate().to("http://localhost:8090/users/list");
		PO_View.checkElement(driver, "text", "Identificate");

	}

	// Intentar acceder sin estar autenticado a la opción de listado de ofertas
	// propias de un usuario estándar. Se deberá volver al formulario de login
	@Test
	public void PR29() {
		initDB();

		// Vamos al formulario de registro
		driver.navigate().to("http://localhost:8090/product/myList");
		PO_View.checkElement(driver, "text", "Identificate");

	}

	// Estando autenticado como usuario estándar intentar acceder a la opción de
	// listado de usuarios del administrador. Se deberá indicar un mensaje de acción
	// prohibida
	@Test
	public void PR30() {
		initDB();

		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_LoginView.fillForm(driver, "a@gmail.com", "123456");
		// Comprobamos que entramos en la sección privada
		PO_View.checkElement(driver, "text", "Bienvenidos a la pagina principal");
		driver.navigate().to("http://localhost:8090/user/list");

		PO_View.checkElement(driver, "free", "//h1[contains(text(),'HTTP Status 403 – Forbidden')]");

	}
}