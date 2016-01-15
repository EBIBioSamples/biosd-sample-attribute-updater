package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class Main {

    static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        Properties properties                   = new Properties();
        List<AccessionDatabaseRow> sampleList   = null;
        FieldsParser parser                     = null;
        InputStream configInput                 = null;
        DatabaseUpdater updater                 = null;
        Path filePath                           = Paths.get(args[0]);

        logger.info("Update process started");

        try {

            configInput = new FileInputStream("config.properties");
            properties.load(configInput);

            boolean validProperties = PropertiesChecker.checkProperties(properties);


            if (validProperties) {


                parser = new FieldsParser(filePath);

                try {

                    sampleList = parser.execute();
                    updater = new DatabaseUpdater(sampleList, properties);
                    updater.execute();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                throw new IOException("The property file does not contain the required " +
                        "properties: 'connectionString', 'databaseUser', 'databasePassword'");
            }

        } catch (IOException ex) {

            ex.printStackTrace();

        } finally {
            if ( configInput != null ) {
                try {
                    configInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        logger.info("Update process finished");

    }
}
