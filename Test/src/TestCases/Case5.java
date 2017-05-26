package TestCases;

import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

@Listeners({TestNGListener.class})
public class Case5 {
	AndroidDriver<WebElement> driver;
	
	@Test(retryAnalyzer = TestNGRetry.class)
	public void f() throws InterruptedException {
		// 进行加计算
		// driver.findElement(By.name("删除")).click();//点击清除按钮
		driver.findElement(By.name("8")).click();// 点击数字8的按钮
		driver.findElement(By.name("+")).click();// 点击加号按钮
		driver.findElement(By.name("4")).click();// 点击数字4的按钮
		driver.findElement(By.name("=")).click();// 点击等于号的按钮
		String sum = driver.findElement(By.className("android.widget.EditText")).getText();// 从编辑框中取出8+4的值
		Assert.assertEquals(sum, "10");// 判断计算器的值是否是12
		Thread.sleep(1000);
	}
	 @Test
	 public void Demo3(){
	 }
	@BeforeClass
	public void beforeClass() throws MalformedURLException {
		DesiredCapabilities capa = new DesiredCapabilities();
		capa.setCapability("deviceName", "");
		capa.setCapability("platformName", "android");
		capa.setCapability("platformVersion", "4.2");
		capa.setCapability("appPackage", "com.android.calculator2");
		capa.setCapability("appActivity", ".Calculator");
		driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capa);
	}

	@AfterClass
	public void afterClass() {
		driver.quit();
	}

}
