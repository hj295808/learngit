package TestCases;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

import org.junit.runner.notification.RunNotifier;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

public class Case3 {

	AndroidDriverBase driver;
	private Long time1 = 0l;

	@BeforeClass
	public void setUp() throws Exception {
		// com.android.calculator2/.Calculator 计算器
		// com.bdstart.activity/jsbd.activity.CalendarActivity 日历
		// com.jsbd.jsbd_filemanager/.MainActivity 文件管理
		// com.jsbd.lovereader/com.jsbd.mytxtreader.BookShelfActivity 阅读器
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability("deviceName", "0123456789ABCDEF");
		cap.setCapability("platformName", "android");
		cap.setCapability("platformVersion", "4.2");
		cap.setCapability("appPackage", "com.android.calculator2");
		cap.setCapability("appActivity", ".Calculator");
		driver = new AndroidDriverBase(new URL("http://127.0.0.1:4723/wd/hub"), cap);
	}

	@AfterClass
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test//(invocationCount = 5)
	public void test1() throws InterruptedException, IOException {
		String msg="calculator";
		String cmd="cmd /c \"adb shell \"logcat -v time | grep com.android.calculator2\" > "+msg+".txt\"";
		Runtime.getRuntime().exec(cmd);
		time1 = System.currentTimeMillis();
		System.out.println(time1);
		
		// time1=System.currentTimeMillis();
		driver.startActivity("com.android.calculator2", ".Calculator");
		Thread.sleep(1000);
		List<WebElement> btnList = driver.findElementsByClassName("android.widget.Button");
		btnList.get(2).click();// 输入8
		Thread.sleep(1000);
		btnList.get(18).click();
		Thread.sleep(1000);
		btnList.get(7).click();// 输入4
		Thread.sleep(1000);
		btnList.get(17).click();// 点击=号
		Thread.sleep(3000);
		WebElement elEditText = driver.findElementByClassName("android.widget.EditText");
		String result = elEditText.getText();
		assertEquals("数值不相等", "12", result);
		Thread.sleep(3000);
		

	}
	@Test
	public void test4() throws InterruptedException, IOException{
		String msg="calendar";
		String cmd="cmd /c \"adb shell \"logcat -v time | grep com.bdstart.activity\" > "+msg+".txt\"";
		Runtime.getRuntime().exec(cmd);
		driver.startActivity("com.bdstart.activity", "jsbd.activity.CalendarActivity");
		Thread.sleep(5000);
		List<WebElement> imageBtnList = driver.findElementsByClassName("android.widget.ImageButton");
		imageBtnList.get(2).click();// 加号按钮
		Thread.sleep(1000);
		WebElement el = driver.findElementByClassName("android.widget.EditText");// 内容
		el.sendKeys("afdsfgdgd");// 输入内容
		Thread.sleep(2000);
		driver.findElementByName("取消").click();// 取消
		Thread.sleep(1000);
	}
	// 进入文件管理
	@Test(groups = {"functiontest"})
	public void test2() throws InterruptedException, IOException {
		String msg="fileManager";
		String cmd="cmd /c \"adb shell \"logcat -v time | grep com.jsbd.jsbd_filemanager\" > "+msg+".txt\"";
		Runtime.getRuntime().exec(cmd);
		driver.startActivity("com.jsbd.jsbd_filemanager", ".MainActivity");
		Thread.sleep(5000);
		List<WebElement> linearLayoutList = driver.findElementsByClassName("android.widget.LinearLayout");
		linearLayoutList.get(8).click();
		Thread.sleep(2000);

		Thread.sleep(1000);

		// 新建文件夹3个
		for (int i = 0; i < 3; i++) {
			driver.findElementByName("新建").click();
			Thread.sleep(1000);
			driver.findElementByClassName("android.widget.EditText").sendKeys(getRandomString(10));
			Thread.sleep(1000);
			driver.findElementByName("保存").click();
			Thread.sleep(2000);
		}
		Thread.sleep(1000);
	}
	@Test(groups = {"systemtest"})
	public void test3() throws InterruptedException, IOException {
		String msg="reader";
		String cmd="cmd /c \"adb shell \"logcat -v time | grep com.jsbd.lovereader\" > "+msg+".txt\"";
		Runtime.getRuntime().exec(cmd);
		// 进入阅读器
		driver.startActivity("com.jsbd.lovereader", "com.jsbd.mytxtreader.BookShelfActivity");
		Thread.sleep(5000);
		driver.findElementByClassName("android.widget.TextView").click();// 点击+号
		Thread.sleep(1000);
		driver.findElementByName("sdcard").click();// 点击sdcard
		Thread.sleep(1000);
		// 向上滑
		driver.swipe(378, 442, 378, 52, 300);
		Thread.sleep(1000);
		// 向下滑
		driver.swipe(378, 52, 378, 442, 300);
		Thread.sleep(1000);

		for (int i = 0; i < 2; i++) {
			driver.pressKeyCode(AndroidKeyCode.BACK);
			Thread.sleep(1000);
		}
		Thread.sleep(3000);
		System.out.println("进入每个应用后结束");

		Thread.sleep(2000);
		long time2 = System.currentTimeMillis();
		System.out.println(getTime(time2 - time1));
	}
	@Test
	public void test5(){
		throw new RuntimeException("fail....");
	}
	@Test
	@Parameters("test1")
	public void test6(@Optional("123")String test1){
		System.out.println("this is "+test1);
	}
	
	@DataProvider(name="user")
    public Object[][] Users(){
        return new Object[][]{
                {"root","passowrd"},
                {"cnblogs.com", "tankxiao"},
                {"tank","xiao"}
        };
    }
    
    @Test(dataProvider="user")
    public void verifyUser(String userName, String password){
        System.out.println("Username: "+ userName + " Password: "+ password);
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

	public String getTime(long number) {
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		String str = sdf.format(number);
		return str;
	}

}
