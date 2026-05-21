package automation.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Log4j2
public class EnvManager {
    private static EnvConfig.EnvironmentDetails currentEnv;

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            EnvConfig config = mapper.readValue(new File("src/test/resources/environments.json"), EnvConfig.class);

            String envName = System.getProperty("env");

            if (envName == null || envName.isEmpty()) {
                Properties globalProps = new Properties();
                try (FileInputStream fis = new FileInputStream("src/test/resources/global.properties")) {
                    globalProps.load(fis);
                    envName = globalProps.getProperty("default.env", "qa1");
                } catch (IOException e) {
                    envName = "qa1";
                }
            }

            envName = envName.toLowerCase();
            currentEnv = config.getEnvironments().get(envName);

            if (currentEnv == null) {
                throw new RuntimeException("Environment '" + envName + "' not found in JSON!");
            }

            log.info(">>>> ACTIVE ENVIRONMENT: {} <<<<", envName.toUpperCase());

        } catch (IOException e) {
            log.error("Critical failure loading configuration {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static EnvConfig.EnvironmentDetails get() {
        return currentEnv;
    }
}