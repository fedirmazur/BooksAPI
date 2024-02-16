package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtils {
    public static String getProperty(String key) {
        Properties prop = new Properties();
        try (InputStream input = PropertyUtils.class.getClassLoader().getResourceAsStream("config.properties")) {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(key);
    }

    public static String getBaseUrl() {
        return PropertyUtils.getProperty("baseUrl");
    }

    public static String getPort() {
        return PropertyUtils.getProperty("port");
    }

    public static String getPath() {
        return PropertyUtils.getProperty("path");
    }

    public static String getUsername() {
        return PropertyUtils.getProperty("username");
    }

    public static String getPassword() {
        return PropertyUtils.getProperty("password");
    }


}
