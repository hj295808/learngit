package TestCases;

import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.http.HttpClient.Factory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.functions.ExpectedCondition;
import io.appium.java_client.remote.AppiumCommandExecutor;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class AndroidDriverBase extends AndroidDriver<WebElement> {
	private AndroidDriver<WebElement> driver;
	private URL remoteAddress;
	private Capabilities desiredCapabilities;
	public AndroidDriverBase(URL remoteAddress, Capabilities desiredCapabilities) {
		super(remoteAddress, desiredCapabilities);
	}
	public AndroidDriverBase(AndroidDriver<WebElement> driver,URL remoteAddress, Capabilities desiredCapabilities){
		this(remoteAddress,desiredCapabilities);
		this.driver=driver;
	}
	//��ĳԪ���ϻ�����������Ԫ�س���
	public void swipeUtilElementAppear(By by,String direction,int duration){
		boolean flag=true;
		while(flag){
			try {
				driver.findElement(by);
				flag=false;
			} catch (Exception e) {
				this.swipe(direction,duration);
			}
		}
	}
	//����������ͨ������ʵ�ָ�����Ļ���
	private void swipe(String direction, int duration) {
		switch(direction){
		case "Up":
			this.swipeToUp(duration);
			break;
		case "Down":
			this.swipeToDown(duration);
			break;
		case "Left":
			this.swipeToLeft(duration);
			break;
		case "Right":
			//this.swipeToRight(duration);
			break;
		}
		
	}
	//���ϻ���
	private void swipeToUp(int duration) {
		int startY=this.appScreen()[1]*4/5;
		int endY=this.appScreen()[1]/5;
		int x=this.appScreen()[0]/2;
		try {
			driver.swipe(x, startY, x, endY, duration);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private int[] appScreen() {
//		driver.manage().window().getSize().height 
//		driver.manage().window().getSize().width 
		int width=driver.manage().window().getSize().width;
		int height=driver.manage().window().getSize().height;
		int[] screen={width,height};
		return screen;
	}
	//���»���
	private void swipeToDown(int duration) {
		int startY=this.appScreen()[1]/5;
		int endy=this.appScreen()[1]*4/5;
		int x=this.appScreen()[0]/2;
		try {
			driver.swipe(x, startY, x, endy, duration);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//���󻬶�
	private void swipeToLeft(int duration) {
		int startX=this.appScreen()[0]*4/5;
		int endX=this.appScreen()[0]/5;
		int y=this.appScreen()[1]/2;
		try{
			driver.swipe(startX,y,endX,y,duration);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	//���һ���
	private void swipeToRight(int duration) {
		int startX=this.appScreen()[0]/5;
		int endX=this.appScreen()[0]*4/5;
		int y=this.appScreen()[1]/2;
		try {
			driver.swipe(startX, y, endX, y, duration);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	//�ж�Ԫ���Ƿ����
	public boolean isEelementExist(By by){
		try {
			driver.findElement(by);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	//�ж���ָ����ʱ���ڣ�ĳԪ���Ƿ����
	public boolean isElementExist(By by,int timeout){
		try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	//��Ԫ���ϳ���
	public void longPress(By by){
		try {
			WebElement el=driver.findElement(by);
			TouchAction ta=new TouchAction(driver);
			ta.longPress(el).release().perform();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}















































