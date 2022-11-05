package xifin.availity;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Base {
  protected static WebDriver driver;
  
  protected static String BASE_DIR = Paths.get(".", new String[0]).toAbsolutePath().normalize().toString();
  
  protected static String inputFilePath;
  
  protected static boolean osFlag = (System.getProperty("os.name").contains("linux") || 
    System.getProperty("os.name").contains("Linux"));
  
  static FileReader reader;
  
  public Base() {
    if (osFlag) {
     System.setProperty("webdriver.chrome.driver", BASE_DIR + "/chromedriver");
      inputFilePath = BASE_DIR + "/input_images/";
    } else {
      System.setProperty("webdriver.chrome.driver", BASE_DIR + "\\chromedriver.exe");
      inputFilePath = BASE_DIR + "\\input_images\\";
    } 
    ChromeOptions options = new ChromeOptions();
    options.addArguments(new String[] { "--no-sandbox" });
    options.addArguments(new String[] { "--disable-dev-shm-usage" });
    driver = (WebDriver)new ChromeDriver(options);
    driver.manage().window().maximize();
  }
  
  public static void open(String url) {
    driver.get(url);
  }
  
  public static String getPropertyValue(String property) throws IOException {
    if (osFlag) {
      reader = new FileReader(BASE_DIR + "/resources/env.properties");
    } else {
      reader = new FileReader(BASE_DIR + "\\resources\\env.properties");
    } 
    Properties p = new Properties();
    p.load(reader);
    return p.getProperty(property);
  }
}
