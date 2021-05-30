package util;

import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonReader {
    public static String getJsonStringFrom(String fileName){
        String basePath = "src/test/resources/json/";

        try {
            return new String(Files.readAllBytes(Paths.get(basePath + fileName)));
        } catch (Exception e) {
            System.out.println("Failed to read file: " +  basePath + fileName);
            return null;
        }
    }
}
