package jdbcdemo;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Savepoint;
public class JDBCdemo {

    public static void main(String[] args) {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/myd123", "root", "root");
            
            conn.commit();
            conn.setAutoCommit(false); // begining of a transaction
            
            
            
            PreparedStatement preStatement = conn.prepareStatement("insert into T1 values (? , ? )");

            preStatement.setInt(1, 1);
            preStatement.setInt(2, 2);
            preStatement.execute();
            
            showTable(conn, "T1");
            preStatement.setInt(1, 3);
            preStatement.setInt(2, 4);
            preStatement.execute();
            
            
            
            showTable(conn, "T1");
            preStatement.setInt(1, 5);
            preStatement.setInt(2, 6);
            preStatement.execute();
            
            Savepoint p1 = conn.setSavepoint("savePoint1");
            System.out.println("\n\n\nprinting the state of table at savepoint 1");
            showTable(conn, "T1");
            preStatement.setInt(1, 7);
            preStatement.setInt(2, 8);
            preStatement.execute();
            
            showTable(conn, "T1");
            preStatement.setInt(1, 9);
            preStatement.setInt(2, 10);
            preStatement.execute();
            
            showTable(conn, "T1");
            
            conn.rollback(p1);
            System.out.println("Rolling back");
            
            conn.commit();
            
            System.out.println("\n\n\nprinting the state of table after commiting at save point 1");
            showTable(conn, "T1");
            
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (ClassNotFoundException cnf) {
            System.out.println("Class not found");
        }
    }
    
    static void deleteOperation(Connection conn , String tableName , String condition)
    {
        try{
            String query = "delete from "+tableName+" where "+condition;
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }
        catch(Exception ew)
        {
            System.out.println(ew);
        }
    }
    
    static void showTable(Connection conn , String tableName)
    {
        System.out.println("\n\n");
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from "+tableName);
            
            ResultSetMetaData rsmd = rs.getMetaData();
            
            for(int count = 1 ; count<=rsmd.getColumnCount();count++)
            {
                System.out.print(rsmd.getColumnName(count)+"\t");
            }
            System.out.println("");
            while(rs.next())
            {
                for(int count = 1 ; count<=rsmd.getColumnCount();count++)
                {
                    System.out.print(rs.getObject(count)+"\t");
                }
                System.out.println("");
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}

