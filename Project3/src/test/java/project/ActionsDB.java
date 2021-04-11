package project;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


/**
 * includes methods to use working with DB
 */
public class ActionsDB {
    private static final String DATABASE_NAME = "e7NoAUYZEw";
    private static Connection con;


    /**
     * creates table on DB by connection.
     * @param con connection to DB
     * @param columns SQL String of creating table
     * @throws SQLException
     */
    public static void createTable(Connection con, String columns) throws SQLException {
        String statementToExecute = "CREATE TABLE IF NOT EXISTS " + DATABASE_NAME + columns ;
        con.createStatement().execute(statementToExecute);
    }

    /**
     * creates table on DB by connection.
     * @param con connection to DB
     * @param input SQL String of inserting to table
     * @throws SQLException
     */
    public static void insertToTable(Connection con, String input) throws SQLException {
        String statementToExecute = "INSERT IGNORE INTO " + DATABASE_NAME + input;
        con.createStatement().execute(statementToExecute);
    }

    /**
     * creates unique config table
     * @throws Exception
     */
    public void createConfigTable() throws Exception {
        con = ConnectionSingleton.getConnectionInstance();
        createTable(con, ".`config`(`config_id` INT NOT NULL, `config_name` VARCHAR(45) NOT NULL, `config_data` VARCHAR(100) NOT NULL, PRIMARY KEY (`config_id`));");
        String table = ".config (`config_id`, `config_name`, `config_data`) VALUES (";
        insertToTable(con, table + "'1', 'URL' , 'https://buyme.co.il');");
        insertToTable(con, table + "'2', 'BROWSER' , 'Chrome');");
        insertToTable(con, table + "'3', 'BROWSER' , 'FireFox');");
        insertToTable(con, table + "'4', 'BROWSER' , 'Edge');");
    }

    /**
     * creates unique history table
     * @throws Exception
     */
    public void createHistoryTable() throws Exception {
            con = ConnectionSingleton.getConnectionInstance();
            createTable(con,".`history`(`test_id` INT NOT NULL AUTO_INCREMENT, `test_date` VARCHAR(25) NOT NULL, PRIMARY KEY (`test_id`));" );
    }

    /**
     * gets current date and time stamp
     * @return current date and time
     */
    public String getCurrentDateAndTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now).toString();

    }

    /**
     * insert current date and time to history table, by auto increasing id column
     * @throws SQLException
     */
    public void insertToHistoryTable() throws SQLException {
        String timeNow= getCurrentDateAndTime();
        insertToTable(con,".history (`test_date`) VALUES ('"+timeNow +"');");
    }

    /**
     * gets config data by its unique id
     * @param con connection to DB
     * @param id number of line to get data from
     * @return data
     * @throws SQLException
     */
    public String getConfigDataById(Connection con, int id) throws SQLException {
        String statementToExecute = "SELECT * FROM " + DATABASE_NAME + ".config;";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(statementToExecute);
        while (rs.next()){
            if (rs.getInt("config_id") == id){
                String data = rs.getString("config_data");
                rs.close();
                return data;
            }
        }
        rs.close();
        return null;
    }

    /**
     * write current date and time to txt file
     * @throws IOException
     */
    public void testDateToTXT () throws IOException {
        try {
            FileWriter resultsFile = new FileWriter("/Users/noamheller/Documents/NOAM/WORK/Automation" +
                    " Course/Project3/src/test/java/project/results.txt", true);
            resultsFile.write("\n" + getCurrentDateAndTime());
            resultsFile.flush();
            resultsFile.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
