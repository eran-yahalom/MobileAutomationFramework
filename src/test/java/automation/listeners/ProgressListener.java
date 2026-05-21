package automation.listeners;

import lombok.extern.log4j.Log4j2;
import org.testng.ITestContext;
import org.testng.ITestListener;

@Log4j2
public class ProgressListener implements ITestListener {
    @Override
    public void onStart(ITestContext context) {
        log.info("Starting Test Execution - Preparing Progress Reports...");
    }
}
