package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class FieldsParser {

    File file;
    static Logger logger = LoggerFactory.getLogger(FieldsParser.class);

    public FieldsParser(Path filePath) {

        this.file = filePath.toFile();

    }

    public List<AccessionDatabaseRow> execute() throws Exception{

        logger.debug("Reading attribute file");

        BufferedReader reader = new BufferedReader(new FileReader(this.file));
        reader.readLine(); //skip the first line
        List<AccessionDatabaseRow> list = new ArrayList<>();
        String line;
        while( (line = reader.readLine()) != null) {
            AccessionDatabaseRow newRow = new AccessionDatabaseRow(line);
            list.add(newRow);
        }

        logger.debug("Reading attribute finished")

        return list;

    }



}
