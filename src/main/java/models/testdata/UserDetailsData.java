package models.testdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetailsData {
    private String fullName;
    private String address1;
    private String city;
    private String zipCode;
    private String country;
    private String cardNumber;
    private String cardExpiry;
    private String cardCVV;


}