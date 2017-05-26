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
    // ����ʧ�ܽ��������·��
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
            //log.info("��ͼ�����·��:" + path);
            System.out.println("��ͼ�����·����"+path);
          //����ʵ�ְ�ͼƬ����ֱ�����������ļ��У�ͨ���ʼ����ͽ�������ֱ����ʾͼƬ
            Reporter.log("<img src=\"../" + path + "\"/>");
        } catch (Exception e) {
            //log.error("��ͼʧ��");
        	System.out.println("��ͼʧ��");
            e.printStackTrace();
        }
    }


    /**
     * ��ȡ��ǰʱ��
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
