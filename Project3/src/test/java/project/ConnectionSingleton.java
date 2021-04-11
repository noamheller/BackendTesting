package project;
import java.sql.Connection;
import java.sql.DriverManager;
/**
 * a Singelton class for creating one instance of Connection to DB.
 */

public class ConnectionSingleton {
    private static Connection con;
    private static final String USER_NAME = "e7NoAUYZEw";
    private static final String PASSWORD = "sPpHdGpAeL";
    private static final String PORT = "3306";
    private static final String SERVER = "remotemysql.com";

    public static Connection getConnectionInstance() throws Exception {
        if(con == null){
            con = DriverManager.getConnection("jdbc:mysql://" + SERVER + ":" + PORT, USER_NAME, PASSWORD);
        }
        return con;
    }

}
