package automation.listeners;


import lombok.extern.log4j.Log4j2;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Log4j2
public class TestNGListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        log.info("Starting test: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("Test passed: {}", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.info("Test failed: {}", result.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Finished test execution for context: {}", context.getName());
    }
}
