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
		// 创建50个文件夹
		for (int i = 0; i < 50; i++) {
			driver.findElementByName("新建").click();
			Thread.sleep(1000);
			driver.findElementByClassName("android.widget.EditText").sendKeys(getRandomString(10));
			Thread.sleep(1000);
			driver.findElementByName("保存").click();
			Thread.sleep(1000);
			System.out.println("创建"+(i+1)+"个文件夹");
		}
		// 删除50个文件夹
		for (int i = 0; i < 50; i++) {
			List<WebElement> linList = driver.findElementsByClassName("android.widget.LinearLayout");
			TouchAction ta = new TouchAction(driver);
			ta.longPress(linList.get(8)).release().perform();// 长按某一项
			Thread.sleep(1000);
			List<WebElement> framList = driver.findElementsByClassName("android.widget.FrameLayout");
			if(i%2==0){
				framList.get(8).click();// 选中某一项
			}else if(i%2==1){
				framList.get(11).click();// 选中某一项
			}
			Thread.sleep(500);
			driver.findElement(By.name("删除")).click();
			Thread.sleep(500);
			driver.findElement(By.name("确定")).click();
			Thread.sleep(1000);
			System.out.println("删除"+(i+1)+"个文件夹");
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

	public static String getRandomString(int length) { // length表示生成字符串的长度
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
