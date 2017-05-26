package TestCases;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.Reporter;

import io.appium.java_client.android.AndroidDriver;

public class ScreenShot {

    private AndroidDriver<?> driver;
    // 测试失败截屏保存的路径
    private String path;
   // public LogUtil log=new LogUtil(this.getClass());

    public ScreenShot(AndroidDriver<?> driver){
       this.driver=driver;
        path=System.getProperty("user.dir")+ "//snapshot//"+ this.getClass().getSimpleName()+"_"+getCurrentTime() + ".png";
    }

    public void getScreenShot() {
    	
        File screen = driver.getScreenshotAs(OutputType.FILE);
        File screenFile = new File(path);
        try {
            FileUtils.copyFile(screen, screenFile);
            //log.info("截图保存的路径:" + path);
            System.out.println("截图保存的路径："+path);
          //这里实现把图片链接直接输出到结果文件中，通过邮件发送结果则可以直接显示图片
            Reporter.log("<img src=\"../" + path + "\"/>");
        } catch (Exception e) {
            //log.error("截图失败");
        	System.out.println("截图失败");
            e.printStackTrace();
        }
    }


    /**
     * 获取当前时间
     */
    public String getCurrentTime(){
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String currentTime=sdf.format(date);
        return currentTime; 
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
