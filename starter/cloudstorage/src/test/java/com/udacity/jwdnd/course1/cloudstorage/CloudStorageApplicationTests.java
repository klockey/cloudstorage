package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void testLoginAuthorization(){
		driver.get("http://localhost:" + this.port + "/login");
		driver.findElement(By.id("username")).sendKeys("b");
		driver.findElement(By.id("password")).sendKeys("b");
		driver.findElement(By.id("submit-button")).click();
		Assertions.assertEquals("Invalid username or password",
				By.id("error-msg").findElement(driver).getText());
	}

	@Test
	public void testSignupAuthorization(){
		driver.get("http://localhost:" + this.port + "/signup");
		driver.findElement(By.id("inputLastName")).sendKeys("C");
		driver.findElement(By.id("inputUsername")).sendKeys("C");
		driver.findElement(By.id("inputFirstName")).sendKeys("C");
		driver.findElement(By.id("inputPassword")).sendKeys("C");
		driver.findElement(By.id("submit-input")).click();
		Assertions.assertEquals(   "You successfully signed up! Please continue to the login page.",
				By.id("success-msg").findElement(driver).getText());
	}

	@Test
	public void testNotes(){
		driver.get("http://localhost:" + this.port + "/home");
		driver.findElement(By.id("submit-button")).click();
		boolean a = driver.findElement(By.name("noteModal")).isDisplayed();
		Assertions.assertEquals(true, a);
	}



	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());

	}

	@Test
	public void getHomePage() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Home", driver.getTitle());

	}

}
