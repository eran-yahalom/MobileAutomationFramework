package automation.di;

import automation.configurations.MobileGuiceModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import io.cucumber.guice.CucumberModules;
import io.cucumber.guice.InjectorSource;

public class TestInjectorSource implements InjectorSource {

    @Override
    public Injector getInjector() {
        return Guice.createInjector(Stage.PRODUCTION,
                CucumberModules.createScenarioModule(),
                new MobileGuiceModule()
        );
    }
}