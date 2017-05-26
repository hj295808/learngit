package TestCases;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;

@Listeners({TestNGListener.class})
public class Case1 {
	AndroidDriver<WebElement> driver;
	@BeforeClass
	public void setup() throws IOException {
//		String cmd="cmd /c \"appium\"";
//		Runtime.getRuntime().exec(cmd);
		DesiredCapabilities capa = new DesiredCapabilities();
		capa.setCapability("deviceName", "");
		capa.setCapability("platformVersion", "4.2");
		capa.setCapability("platformName", "android");
		capa.setCapability("appPackage", "com.android.calculator2");
		capa.setCapability("appActivity", ".Calculator");
		driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capa);
	}

	@Test
	public void open() {
		System.out.println("open pass");
		int width=driver.manage().window().getSize().width;
		int height=driver.manage().window().getSize().height;
		System.out.println("width:"+width+",height:"+height);
		// Reporter.log("²âÊÔ");
	}

	@Test
	public void open1() {
		// System.out.println("²âÊÔ2Í¨¹ý");
		throw new RuntimeException("failed!");
	}

	@Test
	public void open2() {
		System.out.println("open2 pass");
	}

	public void sendCmd(String packName, String appName) throws IOException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		String cmd = "cmd /c \"adb shell \"logcat -v time | grep " + packName + "\" > " + appName
				+ df.format(new Date()) + ".txt\"";
		Runtime.getRuntime().exec(cmd);
	}

	@Test // (expectedExceptions = RuntimeException.class)
	public void open3() throws InterruptedException, IOException {

		// String appName="appName";
		// String pacName="com.bd.appandroidemo";
		sendCmd("com.bd.appandroidemo", "beijing");
		

		driver.startActivity("com.bd.appandroidemo", ".MainActivity");
		Thread.sleep(1000);
		WebElement el = driver.findElementByClassName("android.widget.Button");
		el.click();
	}

	@Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "NullPoint")
	public void open4() {
		Reporter.log("finished -->");
		throw new IllegalArgumentException("NullPoint");
	}

	// @Test(retryAnalyzer = Retry.class)
	public void Demo() {
		Assert.fail();
	}

	@AfterClass
	public void quit() {
		driver.quit();
	}
}
