package TestCases;

import org.testng.annotations.Test;

import io.appium.java_client.TouchAction;

import org.testng.annotations.BeforeClass;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;

public class Case4 {
	AndroidDriverBase driver;

	@Test
	public void f() throws InterruptedException {
		List<WebElement> linearLayoutList = driver.findElementsByClassName("android.widget.LinearLayout");
		linearLayoutList.get(8).click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		// boolean flag=driver.isEelementExist(By.name("LKLLM"));
		// System.out.println(flag);
		// ����50���ļ���
		for (int i = 0; i < 50; i++) {
			driver.findElementByName("�½�").click();
			Thread.sleep(1000);
			driver.findElementByClassName("android.widget.EditText").sendKeys(getRandomString(10));
			Thread.sleep(1000);
			driver.findElementByName("����").click();
			Thread.sleep(1000);
			System.out.println("����"+(i+1)+"���ļ���");
		}
		// ɾ��50���ļ���
		for (int i = 0; i < 50; i++) {
			List<WebElement> linList = driver.findElementsByClassName("android.widget.LinearLayout");
			TouchAction ta = new TouchAction(driver);
			ta.longPress(linList.get(8)).release().perform();// ����ĳһ��
			Thread.sleep(1000);
			List<WebElement> framList = driver.findElementsByClassName("android.widget.FrameLayout");
			if(i%2==0){
				framList.get(8).click();// ѡ��ĳһ��
			}else if(i%2==1){
				framList.get(11).click();// ѡ��ĳһ��
			}
			Thread.sleep(500);
			driver.findElement(By.name("ɾ��")).click();
			Thread.sleep(500);
			driver.findElement(By.name("ȷ��")).click();
			Thread.sleep(1000);
			System.out.println("ɾ��"+(i+1)+"���ļ���");
		}

	}

	@BeforeClass
	public void beforeClass() throws MalformedURLException {
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability("deviceName", "0123456789ABCDEF");
		cap.setCapability("platformName", "android");
		cap.setCapability("platformVersion", "4.2");
		cap.setCapability("appPackage", "com.jsbd.jsbd_filemanager");
		cap.setCapability("appActivity", ".MainActivity");
		driver = new AndroidDriverBase(new URL("http://127.0.0.1:4723/wd/hub"), cap);
	}

	@AfterClass
	public void afterClass() {
		driver.quit();
	}

	public static String getRandomString(int length) { // length��ʾ�����ַ����ĳ���
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
}
