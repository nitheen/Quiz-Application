package edu.uic.ids.DbConnection;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class DbConnectionManager {

	//static String url = "jdbc:mysql://131.193.209.57:3306/f15g114";
	static String url = "jdbc:mysql://localhost:3306/f15g114";

	static String UserName = "f15g114";
	static String Password = "f15g114RQcdW";

	public static Connection getConnection() {
		Connection conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, UserName, Password);
			conn.setAutoCommit(false);
		} catch (Exception e) {
			//StringWriter sw = new StringWriter();
			//PrintWriter pw = new PrintWriter(sw);
			//e.printStackTrace();
			//String trace = sw.toString();
			FacesMessage msg = new FacesMessage("Error in establishing DB connection");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//System.out.println("dbconn  exception "+e+"   :  "+e.getMessage());	
		}
		return conn;
	}

	public static boolean closeConnection(Connection conn) {
		try {

			if (!conn.isClosed() && conn!=null)
				conn.close();
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			//String trace = sw.toString();
			FacesMessage msg = new FacesMessage(sw.toString());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
return true;
	}
}

