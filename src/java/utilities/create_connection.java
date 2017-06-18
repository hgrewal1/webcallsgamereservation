/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.sql.*;





public class create_connection {


  Statement stmt = null;
    //JDBC driver name and database URL
     final String driverName = "oracle.jdbc.OracleDriver";
     final String url = "jdbc:oracle:thin:@144.217.163.57:1521:XE" ;
    
    // Database credentials
     final String username = "proj2";
     final String password = "project2pw";
private  Connection con;
  public  Connection getConnection() throws ClassNotFoundException, SQLException {
     
            Class.forName(driverName);
          
                con = DriverManager.getConnection(url, username, password);
       
        return con;
    }
   public  void closeConnection() throws SQLException {
      con.close();
  }
  
  public  void closeStmt() throws SQLException {
      stmt.close();
  }
     public  ResultSet grewal(String S) throws SQLException {
         
stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
return stmt.executeQuery(S);
}
}