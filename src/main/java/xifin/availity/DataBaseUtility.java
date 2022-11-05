package xifin.availity;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;
import org.apache.commons.dbcp2.BasicDataSource;
import static xifin.availity.Base.getPropertyValue;

public class DataBaseUtility {
  private static BasicDataSource dataSource;
  
  private static UUID uuid = UUID.randomUUID();
  
  private static BasicDataSource getDataSource() throws IOException {
    if (dataSource == null) {
      BasicDataSource ds = new BasicDataSource();
      ds.setDriverClassName("org.postgresql.Driver");
      ds.setUrl(getPropertyValue("dburl"));
	  ds.setUsername(getPropertyValue("dbuser"));
	  ds.setPassword(getPropertyValue("dbpass"));
      ds.setMinIdle(5);
      ds.setMaxIdle(10);
      ds.setMaxOpenPreparedStatements(100);
      dataSource = ds;
    } 
    return dataSource;
  }
  
  public void insert(boolean error_any, String error_detail_notes, String accession_id, String subscriber_id, String notes, String user_processed, String insurance_carrier, String process_id, String service_id) throws SQLException, IOException {
    BasicDataSource dataSource = getDataSource();
    try(Connection connection = dataSource.getConnection(); 
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO \"xifinLasit_xifinevlasitreport\" (id, error_any, error_detail_notes, accession_id, subscriber_id, notes, user_processed, insurance_carrier, process_id, serviece_id, created_on) VALUES ('" + uuid + "', '" + error_any + "', '" + error_detail_notes + "', '" + accession_id + "', '" + subscriber_id + "', '" + notes + "', '" + user_processed + "', '" + insurance_carrier + "', '" + process_id + "', '" + service_id + "','" + new Timestamp(
            
            System.currentTimeMillis()) + "')")) {
      System.out.println("The Connection Object is of Class: " + connection.getClass());
      try {
        int i = pstmt.executeUpdate();
      } catch (Exception e) {
        System.err.println("Exception occurred!!! : " + e.getMessage());
        e.printStackTrace();
      } 
    } 
  }
  
  public void insertLogEvents(String description, String dwId, String serviceId) throws SQLException, IOException {
    BasicDataSource dataSource = getDataSource();
    try(Connection connection = dataSource.getConnection(); 
        
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO \"xifinLasit_logevents\" (id, description, dw_id, service_id, created_on) VALUES ('" + uuid + "', '" + description + "', '" + dwId + "', '" + serviceId + new Timestamp(
            
            System.currentTimeMillis()) + "')")) {
      System.out.println("The Connection Object is of Class: " + connection.getClass());
      try {
        int i = pstmt.executeUpdate();
      }  catch (Exception e) {
        System.err.println("Exception occurred!!! : " + e.getMessage());
        e.printStackTrace();
      } 
    } 
  }
  
  public void updateTask(String id, int topCount, int bottomCount) throws SQLException, IOException {
    BasicDataSource dataSource = getDataSource();
    try(Connection connection = dataSource.getConnection(); 
        PreparedStatement pstmt = connection.prepareStatement("UPDATE \"waterlabs_newtasks\" set task_status_top = '" + topCount + "', task_status_bottom = '" + bottomCount + "' where id = '" + id + "'")) {
      System.out.println("The Connection Object is of Class: " + connection.getClass());
      try {
        int i = pstmt.executeUpdate();
      } catch (Exception e) {
        System.err.println("Exception occurred!!! : " + e.getMessage());
        e.printStackTrace();
      } 
    } 
  }
  
  public void updateTaskCompleteCount(String id) throws SQLException, IOException {
    BasicDataSource dataSource = getDataSource();
    try(Connection connection = dataSource.getConnection(); 
        PreparedStatement pstmt = connection.prepareStatement("UPDATE \"waterlabs_newtasks\" set completed_count = completed_count + 1 where id = '" + id + "'")) {
      System.out.println("The Connection Object is of Class: " + connection.getClass());
      try {
        int i = pstmt.executeUpdate();
      } catch (Exception e) {
        connection.rollback();
        System.err.println("Record update failed.");
        e.printStackTrace();
      } 
    } 
  }
  
  public void updateTaskErrorCount(String id) throws SQLException, IOException {
    BasicDataSource dataSource = getDataSource();
    try(Connection connection = dataSource.getConnection(); 
        PreparedStatement pstmt = connection.prepareStatement("UPDATE \"waterlabs_newtasks\" set error_count = error_count + 1 where id = '" + id + "'")) {
      System.out.println("The Connection Object is of Class: " + connection.getClass());
      try {
        int i = pstmt.executeUpdate();
      } catch (Exception e) {
        connection.rollback();
        System.err.println("Record update failed.");
        e.printStackTrace();
      } 
    } 
  }
  
  public void updateNewTask(String id, String taskStatusTop, String taskStatusBottom, String completedCount, String errorCount) throws SQLException, IOException {
    uuid = UUID.randomUUID();
    String sqlUpdate = "UPDATE \"waterlabs_newtasks\"SET task_status_top=?,task_status_bottom=?,completed_count=?,error_count=? WHERE id::text=?";
    BasicDataSource dataSource = getDataSource();
    try(Connection connection = dataSource.getConnection(); 
        PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
      System.out.println("The Connection Object is of Class: " + connection.getClass());
      try {
        pstmt.setString(1, taskStatusTop);
        pstmt.setString(2, taskStatusBottom);
        pstmt.setString(3, completedCount);
        pstmt.setString(4, errorCount);
        pstmt.setString(5, id);
        int queryStatus = pstmt.executeUpdate();
        System.out.println(queryStatus);
      } catch (Exception e) {
        System.err.println("Exception occurred : " + e.getMessage());
        e.printStackTrace();
      } 
    } 
  }
  
  public static void main(String[] args) {
    DataBaseUtility dataBaseUtility = new DataBaseUtility();
    try {
      dataBaseUtility.insert(false, "3245234", "3245234", "3245234", "3245234", "3245234", "3245234", "3245234", "3245234");
    } catch (Exception e) {
      System.out.println(e);
    } 
  }
}
