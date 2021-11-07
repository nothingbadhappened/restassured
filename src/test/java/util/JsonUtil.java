package util;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Log4j2
public class JsonUtil {
    private static final String basePath = "src/test/resources/json/";

    public static String getJsonStringFrom(String fileName){
        try {
            return new String(Files.readAllBytes(Paths.get(basePath + fileName)));
        } catch (Exception e) {
            log.error("Failed to read file: " +  basePath + fileName);
            return null;
        }
    }

    public static File getJsonFileByName(String fileName){
        return new File(basePath + fileName);
    }
}
