package main;

import oracle.jdbc.pool.OracleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * Created by lucacherubin on 07/01/2016.
 */
public class DatabaseUpdater {

    static Logger logger = LoggerFactory.getLogger(DatabaseUpdater.class);

    OracleDataSource ods;
    Connection conn;
    List<AccessionDatabaseRow> charactList;
    String connectionString;
    String dbUser;
    String dbPassword;

    private final int BATCH_SIZE = 100;

    private String checkQuery = "SELECT COUNT('SAMPLE_ID') FROM BSD_ACC.ATTR_ADD " +
            "WHERE SAMPLE_ID = ? AND ATTR_KEY = ?";

    private String updateQuery = "UPDATE BSD_ACC.ATTR_ADD " +
            "SET ATTR_VALUE = ?, TERM_SOURCE_REF = ?, " +
            "TERM_SOURCE_ID = ?, TERM_SOURCE_URI = ?, " +
            "TERM_SOURCE_VERSION = ?, UNIT = ? " +
            "WHERE SAMPLE_ID = ? AND ATTR_KEY = ?";

    private String insertQuery = "INSERT INTO BSD_ACC.ATTR_ADD " +
            "(SAMPLE_ID, ATTR_KEY, ATTR_VALUE, TERM_SOURCE_REF, " +
            "TERM_SOURCE_ID, TERM_SOURCE_URI, TERM_SOURCE_VERSION, UNIT) " +
            "VALUES (?,?,?,?,?,?,?,?)";

    public DatabaseUpdater(List<AccessionDatabaseRow> updateList, Properties prop) throws SQLException {
        this.connectionString = prop.getProperty("connectionString");
        this.dbUser           = prop.getProperty("databaseUser");
        this.dbPassword       = prop.getProperty("databasePassword");
        this.charactList      = updateList;
        this.ods              = new OracleDataSource();

    }

    public void execute() throws Exception {


        try {
            ods.setURL(connectionString);
            ods.setUser(dbUser);
            ods.setPassword(dbPassword);
            conn = ods.getConnection();

            PreparedStatement sampleIsPresentStatement = conn.prepareStatement(checkQuery);
            PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
            PreparedStatement insertStatement = conn.prepareStatement(insertQuery);

            logger.info("Process started");

            for (int iRow = 0, nRow = charactList.size(); iRow < nRow; iRow++ ) {

                AccessionDatabaseRow row = charactList.get(iRow);



                sampleIsPresentStatement.clearParameters();
                sampleIsPresentStatement.setString(1,row.getSampleId());
                sampleIsPresentStatement.setString(2,row.getAttrKey());

                ResultSet results = sampleIsPresentStatement.executeQuery();
                if (results.next()) {
                    switch (results.getInt(1)) {
                        case 0: //Insert

                            logger.debug(String.format(
                                    "INSERT: %s - %s:%s", row.getSampleId(),row.getAttrKey(),row.getAttrValue()));

                            insertStatement.clearParameters();
                            insertStatement.setString(1, row.getSampleId());
                            insertStatement.setString(2, row.getAttrKey());
                            insertStatement.setString(3, row.getAttrValue());
                            insertStatement.setString(4, row.getTermSourceRef());
                            insertStatement.setString(5, row.getTermSourceId());
                            insertStatement.setString(6, row.getTermSourceUri());
                            insertStatement.setString(7, row.getTermSourceVersion());
                            insertStatement.setString(8, row.getUnit());

                            insertStatement.executeUpdate();
                            break;

                        case 1: //Update

                            logger.debug(String.format(
                                    "UPDATE: %s - %s:%s", row.getSampleId(),row.getAttrKey(),row.getAttrValue()));

                            updateStatement.clearParameters();
                            updateStatement.setString(1, row.getAttrValue());
                            updateStatement.setString(2, row.getTermSourceRef());
                            updateStatement.setString(3, row.getTermSourceId());
                            updateStatement.setString(4, row.getTermSourceUri());
                            updateStatement.setString(5, row.getTermSourceVersion());
                            updateStatement.setString(6, row.getUnit());
                            updateStatement.setString(7, row.getSampleId());
                            updateStatement.setString(8, row.getAttrKey());

                            updateStatement.executeUpdate();
                            break;

                        default:
                            if (results.getInt(1) > 1) {
                                logger.error(String.format(
                                        "ERROR: %s - %s attribute key multiple presence", row.getSampleId(), row.getAttrKey()));
                            }



                    }
                    logger.debug(row.toString() + " DONE");
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logger.info("Database update process Completed");
            if (conn != null) {

                conn.close();
                conn = null;

            }
        }

    }

}