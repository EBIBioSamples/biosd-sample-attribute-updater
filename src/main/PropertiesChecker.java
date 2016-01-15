package main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Created by lucacherubin on 14/01/2016.
 */
public class PropertiesChecker {

    private static final Set<String> requiredProperties;

    static {
        requiredProperties = new HashSet<>();

        requiredProperties.add("connectionString");
        requiredProperties.add("databaseUser");
        requiredProperties.add("databasePassword");
    }

    public static boolean checkProperties(Properties prop) {


        Set<String> availableProperties = prop.stringPropertyNames();
        return availableProperties.containsAll(requiredProperties);

    }
}
