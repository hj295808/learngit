package TestCases;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class TestNGRetry implements IRetryAnalyzer {
	// public LogUtil log = new LogUtil(this.getClass());
	private int retryCount = 0;
	private int maxRetryCount = 5;

	public boolean retry(ITestResult result) {
		if (retryCount < maxRetryCount) {
			String message = "running retry for  '" + result.getName() + "' on class " + 
					this.getClass().getName()
					+ " Retrying " + retryCount + " times";
			System.out.println(message);
			retryCount++;
			return true;
		}
		return false;
	}
}
