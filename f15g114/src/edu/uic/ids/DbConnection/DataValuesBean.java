/**
 * 
 */
package edu.uic.ids.DbConnection;

import java.sql.Connection;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * @author Nitheen
 *
 */

public class DataValuesBean {

	String userName;
	String password;
	String dbms;
	String dbmsHost;
	String schema;

	@PostConstruct
	public void init() {
		userName = "f15g114";
		schema = "f15g114";
		dbmsHost = "localhost";
		password = "f15g114RQcdW";
		
//		userName = "root";
//		schema = "test";
//		dbmsHost = "localhost";
//		password = "root";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbms() {
		return dbms;
	}

	public void setDbms(String dbms) {
		this.dbms = dbms;
	}

	public String getDbmsHost() {
		return dbmsHost;
	}

	public void setDbmsHost(String dbmsHost) {
		this.dbmsHost = dbmsHost;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String checkDbValues() {
		Connection conn = null;
		DbConnection.UserName = userName;
		DbConnection.Password = password;

		if (dbms.equals("mysql")) {
			DbConnection.className = "com.mysql.jdbc.Driver";
			DbConnection.url = "jdbc:mysql://" + dbmsHost + ":3306/" + schema;
		} else if (dbms.equals("oracle")) {
			DbConnection.className = "oracle.jdbc.driver.OracleDriver";
			DbConnection.url = "jdbc:oracle:thin:@" + dbmsHost + ":1521:" + schema;
		} else {
			DbConnection.className = "COM.ibm.db2.jdbc.app.DB2Driver";
			DbConnection.url = "jdbc:db2://" + dbmsHost + ":5021/" + schema;
		}

		conn = DbConnection.getConnection();

		if (conn != null)
			return "success";
		else {
			FacesMessage msg = new FacesMessage(
					"Unable to connect to the database. Please check the database credentials!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "fail";
		}
	}
}
