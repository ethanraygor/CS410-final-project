import java.sql.*;

public class Database {
    public static Connection getDatabaseConnection() throws SQLException {
        int databasePort = 52705;//???
        String databasePassword = "sql";
        try {
            System.out.println("jdbc:mysql://localhost:"+databasePort+"/test?verifyServerCertificate=false&useSSL=true");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:"+databasePort+"/test?verifyServerCertificate=false&useSSL=true&serverTimezone=UTC", "msandbox",
                    databasePassword);
            // Do something with the Connection
            System.out.println("Database [test db] connection succeeded!");
            System.out.println();
            return con;
        } catch (SQLException sqlException) {
            System.out.println("SQLException was thrown while trying to connection to database: \njdbc:mysql://localhost:"+databasePort+"/test?verifyServerCertificate=false&useSSL=true");
            System.out.println(sqlException.getMessage());
            throw sqlException;
        }

    }
}