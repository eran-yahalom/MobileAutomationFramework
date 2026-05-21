package automation.configurations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;

@Data
public class EnvConfig {
    private Map<String, EnvironmentDetails> environments;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EnvironmentDetails {
        private String url;
        private String schema;
        private String dbName;
        private String dbPass;
        private String username;
        private String password;
        private String creditCardHolderName;
        private String creditCardNumber;
        @JsonProperty("creditCardExpirationDate")
        private String creditCardExpiry;
        @JsonProperty("creditCardCVV")
        private String creditCardCvv;
    }
}