package automation.configurations;

import com.google.inject.AbstractModule;

public class MobileGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        // התקנת המודול של הדרייבר
        install(new DriverModule());
    }
}