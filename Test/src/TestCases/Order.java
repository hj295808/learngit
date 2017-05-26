package TestCases;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

public class Order {
	@Test
	public void a() {
		System.out.println("a");
		Assert.assertEquals(1, 1);
	}

	@Test(enabled = false)
	public void b() {
		System.out.println("b");
		Assert.assertEquals(1, 1);
	}

	@Test
	public void c() {
		System.out.println("c");
		Assert.assertEquals(1, 2);
	}
}
