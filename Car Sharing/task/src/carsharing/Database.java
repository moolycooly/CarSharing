package carsharing;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    public static String dbName="carsharing";
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:file:../task/src/carsharing/db/" + dbName;
    public static  Statement statement ;
    public static  Connection connection;
    public static void createConnection(){

        try {
            Class.forName("org.h2.Driver");
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();
            connection.setAutoCommit(true);

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void closeConnection() throws SQLException {
        statement.close();
        connection.close();
    }

}
