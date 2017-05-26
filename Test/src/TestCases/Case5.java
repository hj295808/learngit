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
		// ���мӼ���
		// driver.findElement(By.name("ɾ��")).click();//��������ť
		driver.findElement(By.name("8")).click();// �������8�İ�ť
		driver.findElement(By.name("+")).click();// ����ӺŰ�ť
		driver.findElement(By.name("4")).click();// �������4�İ�ť
		driver.findElement(By.name("=")).click();// ������ںŵİ�ť
		String sum = driver.findElement(By.className("android.widget.EditText")).getText();// �ӱ༭����ȡ��8+4��ֵ
		Assert.assertEquals(sum, "10");// �жϼ�������ֵ�Ƿ���12
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
