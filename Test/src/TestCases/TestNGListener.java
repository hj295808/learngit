package TestCases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.log4testng.Logger;

public class TestNGListener extends TestListenerAdapter {
	// private static AndroidDriver<?> driver;
	//
	// //LogUtil log = new LogUtil(this.getClass());
	//
	// public static void setDriver(AndroidDriver<?> driver) {
	// TestNGListener.driver = driver;
	// }
	private static Logger logger = Logger.getLogger(TestNGListener.class);

	@Override
	public void onFinish(ITestContext testContext) {
		super.onFinish(testContext);
		// List of test results which we will delete later
		ArrayList<ITestResult> testsToBeRemoved = new ArrayList<ITestResult>();
		// collect all id's from passed test
		Set<Integer> passedTestIds = new HashSet<Integer>();
		for (ITestResult passedTest : testContext.getPassedTests().getAllResults()) {
			logger.info("PassedTests = " + passedTest.getName());
			passedTestIds.add(getId(passedTest));
		}
		Set<Integer> failedTestIds = new HashSet<Integer>();
		for (ITestResult failedTest : testContext.getFailedTests().getAllResults()) {
			logger.info("failedTest = " + failedTest.getName());
			int failedTestId = getId(failedTest);
			// if we saw this test as a failed test before we mark as to be
			// deleted
			// or delete this failed test if there is at least one passed
			// version
			if (failedTestIds.contains(failedTestId) || passedTestIds.contains(failedTestId)) {
				testsToBeRemoved.add(failedTest);
			} else {
				failedTestIds.add(failedTestId);
			}
		}
		// finally delete all tests that are marked
		for (Iterator<ITestResult> iterator = testContext.getFailedTests().getAllResults().iterator(); iterator
				.hasNext();) {
			ITestResult testResult = iterator.next();
			if (testsToBeRemoved.contains(testResult)) {
				logger.info("Remove repeat Fail Test: " + testResult.getName());
				iterator.remove();
			}
		}
		 // 第二种方式

//	        Iterator<ITestResult> listOfFailedTests = testContext.getFailedTests().getAllResults().iterator();
//	        while (listOfFailedTests.hasNext()) {
//	            ITestResult failedTest = listOfFailedTests.next();
//	            ITestNGMethod method = failedTest.getMethod();
//	            if (testContext.getFailedTests().getResults(method).size() > 1) {
//	                listOfFailedTests.remove();
//	            } else {
//	                if (testContext.getPassedTests().getResults(method).size() > 0) {
//	                    listOfFailedTests.remove();
//	                }
//	            }
//	        }

	}
	private int getId(ITestResult result) {
		int id = result.getTestClass().getName().hashCode();
		id = id + result.getMethod().getMethodName().hashCode();
		id = id + (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
		return id;
	}

	@Override
	public void onTestFailure(ITestResult tr) {
		super.onTestFailure(tr);
		System.out.println("onTestFailure");
		// Case1 ca1 = (Case1) tr.getInstance();
		// AndroidDriver driver = ca1.getInstance();
		// ScreenShot screenShot = new ScreenShot(driver);
		// screenShot.getScreenShot();
	}

	@Override
	public void onTestSkipped(ITestResult tr) {
		super.onTestSkipped(tr);
		System.out.println("onTestSkipped");
	}

	@Override
	public void onTestStart(ITestResult result) {
		super.onTestStart(result);
		System.out.println("onTestStart");
	}

	@Override
	public void onStart(ITestContext testContext) {
		System.out.println("onStart");
		super.onStart(testContext);
	}

}
