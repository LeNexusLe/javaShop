package project.project;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionUtil {
    Connection con;
    public static Connection conDB(){
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/kp_online_shop",
                    "root",
                    ""
            );
            return con;
        }catch (Exception ex){
            return null;
        }
    }
}
