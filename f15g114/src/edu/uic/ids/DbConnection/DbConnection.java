package edu.uic.ids.DbConnection;

/**
 * @author Nitheen
 *
 */

import java.sql.Connection;
import java.sql.DriverManager;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class DbConnection {

	static String url;
	static String className;
	static String UserName;
	static String Password;

	public static Connection getConnection() {

		Connection conn = null;

		try {
			Class.forName(className);
			// System.out.println(url + " :: " + UserName + "::: "+Password);
			conn = DriverManager.getConnection(url, UserName, Password);
			// System.out.println("DB Conncection Opened : " + conn);
			conn.setAutoCommit(false);
		} catch (ClassNotFoundException e) {
			FacesMessage msg = new FacesMessage("Class drivers not found.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// System.out.println("dbconn exception " + e + " : " +
			// e.getMessage());
		} catch (Exception e) {
			// System.out.println("dbconn exception " + e + " : " +
			// e.getMessage());
		}
		return conn;
	}

	public static boolean closeConnection(Connection conn) {
		try {
			// System.out.println("Connection Closed");
			if (!conn.isClosed() && conn != null)
				conn.close();
		} catch (Exception e) {
		}
		return true;
	}
}
