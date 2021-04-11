package project.extras;
import org.openqa.selenium.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import project.ActionsDB;
import project.ConnectionSingleton;
import project.Const;
import project.DriverSingleton;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.concurrent.TimeUnit;

/**
 * extra challenges on the project
 */
public class Extra {
    private static WebDriver driver;
    private static Connection con;
    private static ActionsDB actionsDB= new ActionsDB();
    private static final String DATABASE_NAME = "e7NoAUYZEw";
    private static final By UPLOAD_PIC_LOCATOR = By.className("bm-media-upload");
    private static final By PIC_PARENT_LOCATOR = By.className("gift-card-img");
    private static final By PIC_LOCATOR = By.tagName("img");

    /**
     * before class test, gets url from configExtra table in DB, opens browser
     * configExtra created with prepared statements
     * historyExtra with DATETIME column
     * In case there is no DB connection writing results into a CSV file
     * @throws Exception
     */
    @BeforeClass
    private void beforeClass() throws Exception {

        try {
            con = ConnectionSingleton.getConnectionInstance();
            createConfigExtraTable();
            actionsDB.createTable(con,".`historyExtra`(`test_id` INT NOT NULL AUTO_INCREMENT, `test_date` DATETIME NOT NULL, PRIMARY KEY (`test_id`));" );
            actionsDB.insertToTable(con,".historyExtra (`test_date`) VALUES ('"+ actionsDB.getCurrentDateAndTime()+"');");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Connection to DB failed. using XML");
            testDateToCSV();
        }

        try {
            driver = DriverExtraSingleton.getDriverExtraInstance();
            String myURL;
            try {
                myURL = getConfigExtraDataById(con, 1);
            } catch (Exception e) {
                e.printStackTrace();
                myURL = DriverSingleton.getData("URL");
            }
            driver.get(myURL);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.manage().window().maximize();
        }catch (Exception e) {
        e.printStackTrace();
        }
    }

    /**
     *  download an image from the web and upload it from code (sender and receiver screen)
     * @throws Exception
     */
    @Test(priority = 1)
    private void picture() throws Exception {
        driver.navigate().to("https://buyme.co.il/money/1921312?price=150");
        uploadPic();
    }
    /**
     * final test, quit driver and connection
     */
    @AfterClass
    private void afterClass() throws SQLException {
        driver.quit();
        con.close();
    }

    private void createConfigExtraTable() throws Exception {
        con = ConnectionSingleton.getConnectionInstance();
        ActionsDB.createTable(con, ".`configExtra`(`config_id` INT NOT NULL , `config_name` VARCHAR(45) NOT NULL, `config_data` VARCHAR(100) NOT NULL, PRIMARY KEY (`config_id`));");
        try {
            String sql = "INSERT IGNORE INTO "+ DATABASE_NAME +".configExtra VALUES (?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "URL");
            preparedStatement.setString(3, "https://buyme.co.il");
            preparedStatement.executeUpdate();
            preparedStatement.setInt(1, 2);
            preparedStatement.setString(2, "BROWSER");
            preparedStatement.setString(3, "Chrome");
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        /**
         * gets configExtra data by its unique id
         * @param conn connection to DB
         * @param id number of line to get data from
         * @return data
         * @throws SQLException
         */
        String getConfigExtraDataById(Connection conn, int id) throws SQLException {
            String statementToExecute = "SELECT * FROM " + DATABASE_NAME + ".configExtra;";
            Statement stmt = conn.createStatement();
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
     * download and upload a picture to form
     * @throws Exception
     */
    private void uploadPic() throws Exception {
        WebElement picture = driver.findElement(PIC_PARENT_LOCATOR).findElement(PIC_LOCATOR);
        String pictureSRC = picture.getAttribute("src");
        URL imageURL = new URL(pictureSRC);
        BufferedImage saveImage = ImageIO.read(imageURL);
        ImageIO.write(saveImage, "png", new File("/Users/noamheller/Documents/NOAM/WORK/Automation Course/Project3/src/test/java/project/extras/upload-image.png"));
        driver.findElement(UPLOAD_PIC_LOCATOR).findElement(Const.INPUT_LOCATOR).sendKeys("/Users/noamheller/Documents/NOAM/WORK/Automation Course/Project3/src/test/java/project/extras/upload-image.png");
    }

    public void testDateToCSV () throws IOException {
        try {
            FileWriter resultsFile = new FileWriter("/Users/noamheller/Documents/NOAM/WORK/Automation " +
                    "Course/Project3/src/test/java/project/extras/resultsExtra.csv", true);
            resultsFile.write("\n" + actionsDB.getCurrentDateAndTime());
            resultsFile.flush();
            resultsFile.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}

