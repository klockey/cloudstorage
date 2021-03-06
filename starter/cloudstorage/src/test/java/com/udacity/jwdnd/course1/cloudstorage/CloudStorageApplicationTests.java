package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.util.Assert;

import java.security.SecureRandom;
import java.util.Base64;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {


	@Autowired
	private EncryptionService encryptionService;

	@Autowired
	private CredentialsMapper credentialsMapper;



	@LocalServerPort
	private int port;

	private WebDriver driver;
	private WebDriverWait wait;



	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 10);
		System.out.println(driver.getTitle());
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	/*
	Test home, login and signup authorization without logging in
	 */

//  Write a test that verifies that an unauthorized user can only access the login and signup pages.

	@Test
	public void UnAuthorization(){
			driver.get("http://localhost:" + this.port + "/home");
		    Assertions.assertNotEquals("Home", driver.getTitle());
		    driver.get("http://localhost:" + this.port + "/login");
			Assertions.assertEquals("Login", driver.getTitle());
		    driver.get("http://localhost:" + this.port + "/signup");
		    Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	/*
         sign up user, logs in, logs out verify home is no longer accessible.
	 */

//  Write a test that signs up a new user, logs in, verifies that the home page is accessible, logs out,
//  and verifies that the home page is no longer accessible.

	@Test
	public void testSignupAuthorization() {
		driver.get("http://localhost:" + this.port + "/signup"); // unauthorized access to the signup page
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("inputFirstName"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("inputFirstName")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + "C" + "';", driver.findElement(By.id("inputFirstName")));
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("inputLastName"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("inputLastName")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + "C" + "';", driver.findElement(By.id("inputLastName")));
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("inputUsername"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("inputUsername")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + "C" + "';", driver.findElement(By.id("inputUsername")));
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("inputPassword"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("inputPassword")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + "C" + "';", driver.findElement(By.id("inputPassword")));
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("submit-input"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("submit-input")));
		Assertions.assertEquals("You successfully signed up! Please continue to the login page.",
				By.id("success-msg").findElement(driver).getText());
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("loginHref"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("loginHref")));
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("username"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("username")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + "C" + "';", driver.findElement(By.id("username")));
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("password"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("password")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + "C" + "';", driver.findElement(By.id("password")));
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("submit-button"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("submit-button")));
		Assertions.assertEquals("Home", driver.getTitle()); // home page
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("submit-logout"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("submit-logout")));
		Assertions.assertNotEquals("Home", driver.getTitle()); // title not equal
	}

	@Test
	public void testNotes(){

		/*
			Values that will be used along the test
		 */
		String firstName = "Itachi";
		String lastName = "Uchiha";
		String username = "itachi";
		String password = "uchiha";
		String title = "Hello";
		String description = "Uchiha Itachi";

		/*
		  Signup Flow
		 */
		driver.get("http://localhost:" + this.port + "/signup");
		wait.until(ExpectedConditions.titleContains("Sign Up"));

		//Click and fill field inputFirstName
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("inputFirstName"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("inputFirstName")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + firstName + "';", driver.findElement(By.id("inputFirstName")));

		//Click and fill field inputLastName
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("inputLastName"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("inputLastName")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + lastName + "';", driver.findElement(By.id("inputLastName")));

		//Click and fill field inputUsername
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("inputUsername"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("inputUsername")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + username + "';", driver.findElement(By.id("inputUsername")));

		//Click and fill field inputPassword
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("inputPassword"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("inputPassword")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + password + "';", driver.findElement(By.id("inputPassword")));

		//Click in the submit-input
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("submit-input"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("submit-input")));


		/*
		  Login Flow
		 */

		driver.get("http://localhost:" + this.port + "/login");
		wait.until(ExpectedConditions.titleContains("Login"));

		//Click and fill field username
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("username"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("username")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + username + "';", driver.findElement(By.id("username")));

		//Click and fill field password
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("password"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("password")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + password + "';", driver.findElement(By.id("password")));

		//Click in the submit-button
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("submit-button"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("submit-button")));

		/*
			Note Flow
		 */

		//Write a test that creates a note, and verifies it is displayed.

		wait.until(ExpectedConditions.titleContains("Home"));
		Assertions.assertEquals("Home",driver.getTitle());

		//Click in the field nav-notes-tab
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("nav-notes-tab"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-notes-tab")));

		//Click in the field submit-button
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("submit-button"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("submit-button")));

		//Click and fill field note-title
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("note-title"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("note-title")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + title + "';", driver.findElement(By.id("note-title")));

		//Click and fill field note-description
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("note-description"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("note-description")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + description + "';", driver.findElement(By.id("note-description")));

		//Click in the field //*[@id="noteModal"]/div/div/div[3]/button[2] (Save changes button of modal)
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"noteModal\"]/div/div/div[3]/button[2]"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.xpath("//*[@id=\"noteModal\"]/div/div/div[3]/button[2]")));

		//Click in the field nav-notes-tab
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("nav-notes-tab"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-notes-tab")));

		//Verify the inclusion of Note title
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/th"))));
		String titleNote = driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/th")).getText();
		Assertions.assertEquals(title,titleNote);

		//Verify the inclusion of Note description
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[2]"))));
		String descriptionNote = driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[2]")).getText();
		Assertions.assertEquals(description,descriptionNote);

/*
		Edits note and verifies it is edited
		 */
		//Write a test that deletes a note and verifies that the note is no longer displayed.

		wait.until(ExpectedConditions.titleContains("Home"));
		Assertions.assertEquals("Home",driver.getTitle());

		//Click in the field nav-notes-tab
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("nav-notes-tab"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-notes-tab")));

		//Click in the field edit-button
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("edit-button"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("edit-button")));

		//Click and fill field note-title
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("note-title"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("note-title")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + "A" + title + "';", driver.findElement(By.id("note-title")));

		//Click and fill field note-description
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("note-description"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("note-description")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + "A" + description + "';", driver.findElement(By.id("note-description")));

		//Click in the field //*[@id="noteModal"]/div/div/div[3]/button[2] (Save changes button of modal)
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"noteModal\"]/div/div/div[3]/button[2]"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.xpath("//*[@id=\"noteModal\"]/div/div/div[3]/button[2]")));

		//Click in the field nav-notes-tab
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("nav-notes-tab"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-notes-tab")));

		//Verify the inclusion of Note title
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/th"))));
		titleNote = driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/th")).getText();
		Assertions.assertEquals("A" + title,titleNote);

		//Verify the inclusion of Note description
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[2]"))));
		descriptionNote = driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[2]")).getText();
		Assertions.assertEquals("A" + description,descriptionNote);


		/*
		Deletes note and verifies it is deleted
		 */
		//Write a test that deletes a note and verifies that the note is no longer displayed.

		wait.until(ExpectedConditions.titleContains("Home"));
		Assertions.assertEquals("Home",driver.getTitle());

		//Click in the field nav-notes-tab
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("nav-notes-tab"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-notes-tab")));

        //Click in the field delete-button
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("delete-button"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("delete-button")));

		// check if description is present
		Boolean isPresent = driver.findElements(By.id("description-title")).size() > 0;
		Assertions.assertNotEquals(true,isPresent);

		// check if description is present
		isPresent = driver.findElements(By.id("description-note")).size() > 0;
		Assertions.assertNotEquals(true,isPresent);


	}

	@Test
	public void testCredentials(){
		/*
			Values that will be used along the test
		 */
		String firstName = "Itachi";
		String lastName = "Uchiha";
		String username = "itachi";
		String password = "uchiha";
		String title = "Hello";
		String description = "Uchiha Itachi";
		String URL = "www.google.com";
		String credentialUsername = "keith";
		String credentialPassword = "a";


		/*
		  Signup Flow
		 */

		// Write a test that creates a set of credentials, verifies that they are displayed, and verifies that the displayed password
		// is encrypted.

		driver.get("http://localhost:" + this.port + "/signup");
		wait.until(ExpectedConditions.titleContains("Sign Up"));

		//Click and fill field inputFirstName
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("inputFirstName"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("inputFirstName")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + firstName + "';", driver.findElement(By.id("inputFirstName")));

		//Click and fill field inputLastName
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("inputLastName"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("inputLastName")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + lastName + "';", driver.findElement(By.id("inputLastName")));

		//Click and fill field inputUsername
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("inputUsername"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("inputUsername")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + username + "';", driver.findElement(By.id("inputUsername")));

		//Click and fill field inputPassword
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("inputPassword"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("inputPassword")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + password + "';", driver.findElement(By.id("inputPassword")));

		//Click in the submit-input
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("submit-input"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("submit-input")));

		/*
		  Login Flow
		 */

		driver.get("http://localhost:" + this.port + "/login");
		wait.until(ExpectedConditions.titleContains("Login"));

		//Click and fill field username
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("username"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("username")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + username + "';", driver.findElement(By.id("username")));

		//Click and fill field password
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("password"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("password")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + password + "';", driver.findElement(By.id("password")));

		//Click in the submit-button
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("submit-button"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("submit-button")));


		/*
			Credential Flow
		 */
		wait.until(ExpectedConditions.titleContains("Home"));
		Assertions.assertEquals("Home",driver.getTitle());

		//Click in the field nav-notes-tab
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("nav-credentials-tab"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-credentials-tab")));

		//Click in the field submit-button
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("credential-submit-button"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("credential-submit-button")));

		//
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("credential-url"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("credential-url")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + URL + "';", driver.findElement(By.id("credential-url")));

		//
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("credential-username"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("credential-username")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + credentialUsername + "';", driver.findElement(By.id("credential-username")));

		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("credential-password"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("credential-password")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + credentialPassword + "';", driver.findElement(By.id("credential-password")));

		//Click in the field //*[@id="noteModal"]/div/div/div[3]/button[2] (Save changes button of modal)
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"credentialModal\"]/div/div/div[3]/button[2]"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.xpath("//*[@id=\"credentialModal\"]/div/div/div[3]/button[2]")));

		//Click in the field nav-credentials-tab
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("nav-credentials-tab"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-credentials-tab")));

		//Verify the inclusion of Credential URL
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/th"))));
		String URLCredential = driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/th")).getText();
		Assertions.assertEquals(URL,URLCredential);

		//Verify the inclusion of Credential Username
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[2]"))));
		String usernameCredential = driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[2]")).getText();
		Assertions.assertNotEquals(username,usernameCredential);

		System.out.println(username + " " + usernameCredential);

		//Verify the inclusion of Credential Password
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[3]"))));
		String passwordEncryptedCredential = driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[3]")).getText();


		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("edit-delete-button"))));
		WebElement credentialElement = driver.findElement(By.id("edit-delete-button"));

		String href = credentialElement.getAttribute("href");
		String [] hrefParse = href.split("/");
		System.out.println(hrefParse[0]+hrefParse[1]+hrefParse[2]+hrefParse[3]+hrefParse[4]);
		System.out.println(hrefParse[0]+" "+ hrefParse[1]+" "+hrefParse[2]+" "+hrefParse[3]+" "+hrefParse[4]+" "+hrefParse[5]);
		int credentialId = Integer.parseInt(hrefParse[5]);

		Credentials credential = credentialsMapper.getCredentialById(credentialId);
		String a = encryptionService.decryptValue(passwordEncryptedCredential, credential.getKey());
		Assertions.assertEquals(credentialPassword,a);


		System.out.println("pass " + credential.getPassword());

		/*
		existing set of credentials flow
		 */
		//Write a test that views an existing set of credentials, verifies that the viewable password is unencrypted,
		// edits the credentials, and verifies that the changes are displayed.

		//test uncrypted password

		wait.until(ExpectedConditions.titleContains("Home"));
		Assertions.assertEquals("Home",driver.getTitle());

		//Click in the field nav-notes-tab
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("nav-credentials-tab"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-credentials-tab")));

		//Click in the field edit-button
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("edit-credential-button"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("edit-credential-button")));

		String b = encryptionService.encryptValue(credentialPassword,credential.getKey());
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("credential-password"))));
		Assertions.assertNotEquals(credentialPassword,b);

		// edits credentials
		//Click and fill field credential-url
		String changedURL = "A"+ URL;
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("credential-url"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("credential-url")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='"  + changedURL+ "';", driver.findElement(By.id("credential-url")));
		Assertions.assertNotEquals(changedURL,driver.findElement(By.id("credential-url")).getText());

		//Click and fill field credential-username
		String changedUsername = "A"+ credentialUsername;
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("credential-username"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("credential-username")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + changedUsername + "';", driver.findElement(By.id("credential-username")));
		Assertions.assertNotEquals(changedUsername,driver.findElement(By.id("credential-username")).getText());

		//Click and fill field credential-password
		String changedPassword = "A"+ credentialPassword;
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("credential-password"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("credential-password")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + changedPassword + "';", driver.findElement(By.id("credential-password")));
	//	Assertions.assertNotEquals(changedPassword,driver.findElement(By.id("credential-password")).getText());

		//Click in the submit-button
		//Click in the field //*[@id="noteModal"]/div/div/div[3]/button[2] (Save changes button of modal)
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"credentialModal\"]/div/div/div[3]/button[2]"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.xpath("//*[@id=\"credentialModal\"]/div/div/div[3]/button[2]")));

		wait.until(ExpectedConditions.titleContains("Home"));
		Assertions.assertEquals("Home",driver.getTitle());

		//Click in the field nav-notes-tab
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("nav-credentials-tab"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-credentials-tab")));

		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("url-credential"))));
		String modURL=driver.findElement(By.id("url-credential")).getText();

		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("username-credential"))));
		String modUsername=driver.findElement(By.id("username-credential")).getText();

		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("password-credential"))));
		String modPassword=driver.findElement(By.id("password-credential")).getText();

		credential = credentialsMapper.getCredentialById(credentialId);
		String c = encryptionService.encryptValue(changedPassword, credential.getKey());
		Assertions.assertEquals(modPassword, c);

		/*
		delete credential
		 */
		wait.until(ExpectedConditions.titleContains("Home"));
		Assertions.assertEquals("Home",driver.getTitle());

		//Click in the field nav-notes-tab
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("nav-credentials-tab"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-credentials-tab")));

		//Click in the field delete-button
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("edit-delete-button"))));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("edit-delete-button")));

		boolean urlTest = !changedURL.equals(driver.findElement(By.id("credential-url")).getText());
		if (urlTest) System.out.println("deleted url");
		boolean usernameTest = !changedUsername.equals(driver.findElement(By.id("credential-username")).getText());
		if (usernameTest) System.out.println("deleted username");
		boolean passwordTest = !changedPassword.equals(driver.findElement(By.id("credential-password")).getText());
		if (passwordTest) System.out.println("deleted password");

		Assertions.assertNotEquals(changedURL,driver.findElement(By.id("credential-url")).getText());
		Assertions.assertNotEquals(changedUsername,driver.findElement(By.id("credential-username")).getText());
		Assertions.assertNotEquals(changedPassword,driver.findElement(By.id("credential-password")).getText());
	}
}
