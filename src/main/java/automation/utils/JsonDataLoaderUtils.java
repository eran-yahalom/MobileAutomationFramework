package automation.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.testdata.UserDetailsData;
import models.testdata.UserDetailsData;

import java.io.File;
import java.io.IOException;
import java.util.List;
public class JsonDataLoaderUtils {
    public static List<UserDetailsData> getSearchData(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String filePath = "src/test/resources/data/" + fileName;
            return mapper.readValue(new File(filePath), new TypeReference<List<UserDetailsData>>(){});
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file: " + fileName, e);
        }
    }
}
