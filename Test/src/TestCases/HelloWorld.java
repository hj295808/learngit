package TestCases;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

public class HelloWorld {
	@Test
	public void f() {
		System.out.println("Hello World!");
	}

	@BeforeTest
	public void beforeTest() {
		System.out.println("beforeTest running!");
	}

	@AfterTest
	public void afterTest() {
		System.out.println("afterTest running!");
	}

}
