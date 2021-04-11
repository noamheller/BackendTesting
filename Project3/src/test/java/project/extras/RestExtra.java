package project.extras;
import org.json.JSONArray;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class RestExtra {
    OkHttpClient client = new OkHttpClient();
    private static WebDriver driver;

    /**
     * perform a REST request to get URL and browser
     * @throws IOException
     */
    @BeforeClass
    private void beforeClass () throws IOException {
        Request request = new Request.Builder()
                .url("https://my-json-server.typicode.com/Dgotlieb/JSFakeServer/config")
                .build();
        Response response = client.newCall(request).execute();
        String jsonData = response.body().string();
        JSONArray jsonArray =new JSONArray(jsonData);
        String url = jsonArray.getJSONObject(0).getString("URL");
        String browser = jsonArray.getJSONObject(1).getString("driver");
        if (browser.equals("Chrome")) {
            System.setProperty("webdriver.chrome.driver", "/Users/noamheller/" +
                    "Documents/NOAM/WORK/Automation Course/chromedriver");
            driver = new ChromeDriver();
        } else if (browser.equals("FireFox")) {
            System.setProperty("webdriver.firefox.driver", "C:\\.exe");
            driver = new FirefoxDriver();
        }
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    /**
     * test example
     * @throws Exception
     */
    @Test(priority = 1)
    private void REST() throws Exception {
        driver.navigate().to("https://buyme.co.il/money/1921312?price=150");
    }
    /**
     * final test, quit driver connection
     */
    @AfterClass
    private void afterClass() throws SQLException {
        driver.quit();
    }
}


