
package xifin.availity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sikuli.script.FindFailed;

public class AetnaAvailMain {
  public static DataBaseUtility db = new DataBaseUtility();
  
  public static AetnaAvailityOperations excelTest = new AetnaAvailityOperations();
  
  private static String firstName = "";
  
  private static String lastName = "";
  
  private static String PSID = "";
  
  private static String AID = "";
  
  private static String DOS = "";
  
  private static String ERRNOTE = "";
  
  private static int bottomCount = 0;
  
  private static final Logger logger = LogManager.getLogger(AetnaAvailMain.class);
  
  static Random random = new Random();
  
  static FileOutputStream failedLog = null;
  
  public static void main(String[] args) throws FindFailed, InterruptedException, IOException {
    logger.info("Started!!!" + Base.getPropertyValue("logPath"));
    String processId = args[0];
    String taskId = args[1];
    String vdiUsername = args[2];
    String vdiPassword = args[3];
    String xifinUsername = args[4];
    String xifinPassword = args[5];
    String insurancePortalUsername = args[6];
    String insurancePortalPassword = args[7];
    int successCounter = 0;
    int failureCounter = 0;
    Format f = new SimpleDateFormat("MMddyyyy");
    String strDate = f.format(new Date());
    int rand_seed = random.nextInt(99);
    String csvFilePath = "\\\\horizon01\\hzuem\\UEMUsers\\" + vdiUsername + "\\Documents\\" + vdiUsername + "-" + rand_seed + "-" + strDate + ".csv";
    try {
      File inputFile;
      if (Base.osFlag) {
        inputFile = new File(Base.BASE_DIR + "/input/input.xlsx");
      } else {
        inputFile = new File(Base.BASE_DIR + "\\input\\input.xlsx");
      } 
      FileInputStream file = new FileInputStream(inputFile);
      XSSFWorkbook workbook = new XSSFWorkbook(file);
      XSSFSheet sheet = workbook.getSheetAt(0);
      int topCount = sheet.getPhysicalNumberOfRows();
      FileOutputStream cleanLogFile = new FileOutputStream(Base.getPropertyValue("logPath"));
      cleanLogFile.close();
      excelTest.openXifin(vdiUsername, vdiPassword, xifinUsername, xifinPassword);
      excelTest.availityLoginPatient(insurancePortalUsername, insurancePortalPassword);
      System.out.println("Logged in to Availity and Xifin.");
      Iterator<Row> rowIterator = sheet.iterator();
      int count = 0;
      int headerAvoid = 0;
      while (rowIterator.hasNext()) {
        bottomCount++;
        if (headerAvoid == 0)
          rowIterator.next(); 
        headerAvoid = 1;
        Row row = rowIterator.next();
        db.updateTask(taskId, topCount - 1, bottomCount);
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
          Cell cell = cellIterator.next();
          cell.setCellValue(1);
          if (cell.getNumericCellValue() == 1) {
            count++;
            if (count == 1) {
              firstName = cell.getStringCellValue();
              continue;
            } 
            if (count == 2) {
              lastName = cell.getStringCellValue();
              continue;
            } 
            if (count == 3) {
              PSID = cell.getStringCellValue();
              continue;
            } 
            if (count == 4) {	
              AID = cell.getStringCellValue();
              continue;
            } 
            if (count == 5)
              try {
                DOS = cell.getStringCellValue();
                if (DOS.contains("/")) {
                  System.out.println("Date in correct format");
                  System.err.println("Date Main:" + DOS);
                  continue;
                } 
                throw new Exception("Date is in incorrect format.");
              } catch (Exception e) {
                Date javaDate = DateUtil.getJavaDate(Double.parseDouble(cell.getStringCellValue()));
                DOS = (new SimpleDateFormat("MM/dd/yyyy")).format(javaDate);
                continue;
              }  
            if (count == 6) {
              ERRNOTE = cell.getStringCellValue();
              count = 0;
              continue;
            } 
            count = 0;
          } 
        } 
        try {
          System.out.println("firstName as per excel - " + firstName);
          System.out.println("lastName as per excel - " + lastName);
          System.out.println("PSID - " + PSID);
          System.out.println("AID - " + AID);
          System.out.println("DOS - " + DOS);
          System.err.println("\n\n\n\n\n");
          sleep(2000);
          String DOB = excelTest.fromAccessionGetDOB(AID);
          sleep(2000);
          PatientDetails patientDetails = excelTest.availityDetailsProvider(DOS, PSID, DOB, AID);
          String firstNameAvaility = patientDetails.getFirstName();
          String lastNameAvaility = patientDetails.getLastName();
          String relationAvaility = patientDetails.getRelation();
          sleep(2000);
          try {
            System.err.println("******" + firstNameAvaility);
            System.err.println("*******" + lastNameAvaility);
          } catch (Exception exception) {}
          excelTest.updateNameInXifin(firstNameAvaility, lastNameAvaility, firstName, lastName, AID, PSID, relationAvaility, vdiPassword);
          sleep(2000);
          System.err.println("success");
          logger.info("Accession ID: {}, {},  \"Updated patient name {}, {} to {}, {} as per website\"", AID, "Completed", lastName, firstName, lastNameAvaility, firstNameAvaility);
          successCounter++;
          ERRNOTE = String.format("Accession ID: %s, \"Updated patient name %s, %s to %s, %s as per website\"", new Object[] { AID, lastName, firstName, lastNameAvaility, firstNameAvaility });
          db.insert(false, "", AID, PSID, ERRNOTE, vdiUsername, "", processId, taskId);
          db.updateNewTask(taskId, topCount + "", (successCounter + failureCounter) + "", successCounter + "", failureCounter + "");
          sleep(5000);
        } catch (Exception e) {
          logger.error("Accession ID: {} ,{}, \"There are errors for this, subscriber ID : {}\"", AID, "Manual", PSID);
          System.out.println("Exception occurred : " + e.getMessage());
          failureCounter++;
          db.updateNewTask(taskId, (topCount - 1) + "", (successCounter + failureCounter) + "", successCounter + "", failureCounter + "");
        } 
      } 
      file.close();
    } catch (Exception e) {
      e.printStackTrace();
    } 
    excelTest.terminate();
    excelTest.exportLogToCSV(Base.getPropertyValue("logPath"), csvFilePath);
  }
  
  public static void sleep(int sleepTime) throws InterruptedException {
    Thread.sleep(sleepTime);
  }
}
