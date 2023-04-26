import java.sql.*;

public class Database {
    public static Connection getDatabaseConnection() throws SQLException {
        int databasePort = 0000;//???
        String databaseHost = "";
        String databaseUsername = "";
        String databasePassword = "";
        String databaseName = "";
        String databaseURL = String.format(
                "jdbc:mysql://%s:%s/%s?verifyServerCertificate=false&useSSL=false&serverTimezone=UTC",
                databaseHost,
                databasePort,
                databaseName);

        try {

            return DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);
        } catch (SQLException sqlException) {
            System.out.printf("SQLException was thrown while trying to connection to database: %s%n", databaseURL);
            System.out.println(sqlException.getMessage());
            throw sqlException;
        }

    }
}