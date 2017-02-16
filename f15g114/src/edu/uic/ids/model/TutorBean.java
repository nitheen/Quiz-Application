/**
 * 
 */
package edu.uic.ids.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

import edu.uic.ids.DbConnection.DbConnection;
import edu.uic.ids.DbConnection.SQL_Constants;

/**
 * @author Nitheen
 *
 */

@ManagedBean
@SessionScoped
public class TutorBean {

	private String firstName;
	private String lastName;
	private String netId;
	private String password;
	private String role;
	private String course;
	private String course_name;
	ArrayList<String> courseName = new ArrayList<String>();
	Connection conn = null;
	private String tableName;
	ArrayList<String> tables = new ArrayList<String>();
	boolean displayTables = false;
	private List<List<String>> dynamicList;
	ArrayList<String> dynamicHeaders = new ArrayList<>();
	private HtmlPanelGroup dynamicDataTableGroup;

	// Method to create a new database and the associated tables
	public String createTables() {
		conn = DbConnection.getConnection();
		try {
			Statement statement = (Statement) conn.createStatement();
			statement.executeUpdate(SQL_Constants.query_0);
			statement.executeUpdate(SQL_Constants.query_1);
			statement.executeUpdate(SQL_Constants.query_2);
			statement.executeUpdate(SQL_Constants.query_3);
			statement.executeUpdate(SQL_Constants.query_4);
			statement.executeUpdate(SQL_Constants.query_5);
			statement.executeUpdate(SQL_Constants.query_6);
			statement.executeUpdate(SQL_Constants.query_7);
			statement.executeUpdate(SQL_Constants.query_8);
			statement.executeUpdate(SQL_Constants.query_9);
			statement.executeUpdate(SQL_Constants.query_10);
			statement.executeUpdate(SQL_Constants.query_11);
			statement.executeUpdate(SQL_Constants.query_12);
			statement.executeUpdate(SQL_Constants.query_13);
			statement.executeUpdate(SQL_Constants.query_14);
			statement.executeUpdate(SQL_Constants.query_15);
			statement.executeUpdate(SQL_Constants.query_16);
			statement.executeUpdate(SQL_Constants.query_17);
			statement.executeUpdate(SQL_Constants.query_18);
			statement.executeUpdate(SQL_Constants.query_19);
			statement.executeUpdate(SQL_Constants.query_20);
			statement.executeUpdate(SQL_Constants.query_21);
			statement.executeUpdate(SQL_Constants.query_22);
			statement.executeUpdate(SQL_Constants.query_23);
			statement.executeUpdate(SQL_Constants.query_24);
			statement.executeUpdate(SQL_Constants.query_25);
			statement.executeUpdate(SQL_Constants.query_26);
			statement.close();
			FacesMessage msg = new FacesMessage("Tables created successfully!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			conn.commit();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in creating new tables. ");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} finally {
			DbConnection.closeConnection(conn);
		}
		return "success";
	}

	// Method to drop the selected table in the listBox
	public String dropTables() {
		conn = DbConnection.getConnection();
		try {
			String sql = "DROP TABLE " + tableName;
			Statement statement = (Statement) conn.createStatement();
			statement.executeUpdate(sql);
			statement = null;
			conn.commit();
			FacesMessage msg = new FacesMessage("Table deleted successfully!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (SQLException e) {
			FacesMessage msg = new FacesMessage("Cannot delete or update a parent row a key constraint fails");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in deleting the table!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} finally {
			DbConnection.closeConnection(conn);
		}
		viewTables();
		return "success";
	}
	
	//Display Tables
	public String displayTable(){		
		return "displayTable";
	}

	// Method to drop all the tables
	public String dropAllTables() {
		conn = DbConnection.getConnection();
		viewTables_assessments();
		String sql = "";
		try {
			Statement statement = (Statement) conn.createStatement();
			for (String table : tables) {
				sql = "DROP TABLE IF EXISTS " + table;
				statement.executeUpdate(sql);
			}

			statement.executeUpdate(SQL_Constants.query_27);
			statement.executeUpdate(SQL_Constants.query_28);
			statement.executeUpdate(SQL_Constants.query_29);
			statement.executeUpdate(SQL_Constants.query_30);
			statement.executeUpdate(SQL_Constants.query_31);
			statement.executeUpdate(SQL_Constants.query_32);
			statement.executeUpdate(SQL_Constants.query_33);
			statement.executeUpdate(SQL_Constants.query_34);
			statement.executeUpdate(SQL_Constants.query_35);
			statement.executeUpdate(SQL_Constants.query_36);
			statement.close();
			FacesMessage msg = new FacesMessage("Tables dropped successfully!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			statement = null;
			conn.commit();
		} catch (SQLException e) {
			FacesMessage msg = new FacesMessage("Error in deleting tables.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} finally {
			DbConnection.closeConnection(conn);
		}
		displayTables = false;
		return "success";
	}

	// Method to list all the tables present in the database
	public String viewTables() {
		tables.clear();
		displayTables = true;
		conn = DbConnection.getConnection();
		String sql = "SHOW TABLES";
		try {
			Statement statement = (Statement) conn.createStatement();
			ResultSet resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				//System.out.println(resultset.getString(1));
				tables.add(resultset.getString(1));
			}
			tableName = tables.get(0);
			statement.close();
			resultset = null;
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in fetching tables list from the database");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} finally {
			DbConnection.closeConnection(conn);
		}
		return "success";
	}

	// Method to list all the tables present in the database
	public String viewTables_assessments() {
		tables.clear();
		displayTables = true;
		String sql = "SELECT Assessment_Table_Name FROM f15g114_assessments ";
		try {
			Statement statement = (Statement) conn.createStatement();
			ResultSet resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				//System.out.println(resultset.getString(1));
				tables.add(resultset.getString(1));
			}
			statement.close();
			resultset = null;
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in fetching tables list from the database");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		}
		return "success";
	}

	// Method called on page load, to clear the list and hide the listbox
	public String createTableLoad() {
		tables.clear();
		displayTables = false;
		return "createTable";
	}

	// Method to register a new user
	public String registerUser() {
		conn = DbConnection.getConnection();
		int role_id = 0;
		String sql = "";
		if (role.equals("tutor"))
			role_id = 2;
		else if (role.equals("professor"))
			role_id = 1;
		try {
			sql = "insert into f15g114_user_details values('" + netId + "','" + password + "'," + role_id
					+ ",null,'0.0.0.0');";
			Statement statement = (Statement) conn.createStatement();
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in inserting values into user_details table!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		}
		try {
			if (role_id == 2) {
				sql = "insert into f15g114_TA_Details values('" + netId + "','" + firstName + "','" + lastName + "','"
						+ course + "');";

			} else if (role_id == 1) {
				sql = "insert into f15g114_professor_details values('" + netId + "','" + firstName + "','" + lastName
						+ "','" + course + "');";
			}
			Statement statement = (Statement) conn.createStatement();
			statement.executeUpdate(sql);
			sql = "insert into f15g114_Student_Details values('" + netId + "','" + firstName + "','" + lastName + "','"
					+ course + "');";
			statement.executeUpdate(sql);
			statement.close();
			FacesMessage msg = new FacesMessage("User Registered successfully!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			firstName = "";
			lastName = "";
			password = "";
			netId = "";
			conn.commit();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in inserting values into professor or TA details table!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} finally {
			DbConnection.closeConnection(conn);
		}
		
		return "registeredUser";
	}

	// Method executed while loading the new user page
	public String loadRegister() {
		courseName.clear();
		conn = DbConnection.getConnection();
		String returnmsg = null;
		String sql = "select course_id from f15g114_course_details";
		try {
			Statement statement = (Statement) conn.createStatement();
			ResultSet resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				//System.out.println(resultset.getString(1));
				courseName.add(resultset.getString(1));
			}
			if (courseName.isEmpty()) {
				FacesMessage msg = new FacesMessage(
						"No Courses present. Create a course first and then register a user.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				returnmsg = "error";
			} else {
				returnmsg = "registerUser";
			}
			course = courseName.get(0);
			statement.close();
			resultset = null;
		} catch (MySQLSyntaxErrorException e) {
			FacesMessage msg = new FacesMessage("No tables present. Create tables and proceed to registration.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in processing request!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
			// TODO: handle exception
		} finally {
			DbConnection.closeConnection(conn);
		}
		return returnmsg;
	}

	// Method to create a new course
	public String newCourse() {
		conn = DbConnection.getConnection();
		String sql = "insert into f15g114_course_details values ('" + course + "','" + course_name + "');";
		try {
			Statement statement = (Statement) conn.createStatement();
			statement.executeUpdate(sql);
			statement.close();
			course = "";
			course_name = "";
			conn.commit();
			FacesMessage msg = new FacesMessage("Course created successfully!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (MySQLIntegrityConstraintViolationException e) {
			FacesMessage msg = new FacesMessage("Could not create new course. Course already exists.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Could not create new course.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// TODO: handle exception
		}finally {
			DbConnection.closeConnection(conn);
		}
		
		return "success";
	}

	// Dynamic tables generation code
	// to diplay all tables

	public String processDynamicTables() {
		
			Statement stmt;
			ResultSet resultset = null;
			String table = tableName;
			try {
				String viewQuery = "Select * from " + table + ";";
				conn = DbConnection.getConnection();
				stmt = conn.createStatement();
				resultset = stmt.executeQuery(viewQuery);

				ResultSetMetaData metaData = resultset.getMetaData();
				int count = metaData.getColumnCount();

				for (int i = 1; i <= count; i++) {
					dynamicHeaders.add(metaData.getColumnLabel(i));
				}

				dynamicList = new ArrayList<List<String>>();
				while (resultset.next()) {
					List<String> val = new ArrayList<String>();
					for (int i = 0; i < count; i++) {
						val.add(resultset.getString(i + 1));
					}
					dynamicList.add(val);
				}
			} catch (Exception e) {
				//e.printStackTrace();
			}

			try{
			HtmlDataTable dynamicDataTable = new HtmlDataTable();
			dynamicDataTable.setValueExpression("value",
					createValueExpression("#{tutorBean.dynamicList}", List.class));
			dynamicDataTable.setVar("dynamicItem");
			dynamicDataTable.setBorder(1);
			dynamicDataTable.setCellspacing("0");
			dynamicDataTable.setCellpadding("1");
			dynamicDataTable.setColumnClasses("columnClass1 border");
			dynamicDataTable.setHeaderClass("headerClass");
			dynamicDataTable.setFooterClass("footerClass");
			dynamicDataTable.setRowClasses("rowClass2");
			dynamicDataTable.setStyleClass("dataTableEx");
			dynamicDataTable.setWidth("800");

			for (int i = 0; i < dynamicList.get(0).size(); i++) {

				HtmlColumn column = new HtmlColumn();
				dynamicDataTable.getChildren().add(column);

				HtmlOutputText header = new HtmlOutputText();
				header.setValue(dynamicHeaders.get(i));
				column.setHeader(header);

				HtmlOutputText output = new HtmlOutputText();
				output.setValueExpression("value", createValueExpression("#{dynamicItem[" + i + "]}", String.class));
				column.getChildren().add(output);
			}

			dynamicDataTableGroup = new HtmlPanelGroup();
			dynamicDataTableGroup.getChildren().add(dynamicDataTable);
			} catch(IndexOutOfBoundsException e){
				FacesMessage msg = new FacesMessage("No data present in the table!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} catch(Exception e){
				FacesMessage msg = new FacesMessage("Unable to process the request!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
			return "success";
	}
	
	private ValueExpression createValueExpression(String valueExpression, Class<?> valueType) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return facesContext.getApplication().getExpressionFactory().createValueExpression(facesContext.getELContext(),
				valueExpression, valueType);
	}
	
	public String clearDynamicList(){
		dynamicList.clear();
		dynamicHeaders.clear();
		dynamicDataTableGroup = null;
		return "clearedList";
	}

	// Getters and setters
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public ArrayList<String> getTables() {
		return tables;
	}

	public void setTables(ArrayList<String> tables) {
		this.tables = tables;
	}

	public boolean isDisplayTables() {
		return displayTables;
	}

	public void setDisplayTables(boolean displayTables) {
		this.displayTables = displayTables;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNetId() {
		return netId;
	}

	public void setNetId(String netId) {
		this.netId = netId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public ArrayList<String> getCourseName() {
		return courseName;
	}

	public void setCourseName(ArrayList<String> courseName) {
		this.courseName = courseName;
	}

	public String getCourse_name() {
		return course_name;
	}

	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}

	public List<List<String>> getDynamicList() {
		return dynamicList;
	}

	public void setDynamicList(List<List<String>> dynamicList) {
		this.dynamicList = dynamicList;
	}

	public ArrayList<String> getDynamicHeaders() {
		return dynamicHeaders;
	}

	public void setDynamicHeaders(ArrayList<String> dynamicHeaders) {
		this.dynamicHeaders = dynamicHeaders;
	}

	public HtmlPanelGroup getDynamicDataTableGroup() {
		if(dynamicDataTableGroup==null)
		{
		processDynamicTables();
		}
		return dynamicDataTableGroup;
	}

	public void setDynamicDataTableGroup(HtmlPanelGroup dynamicDataTableGroup) {
		this.dynamicDataTableGroup = dynamicDataTableGroup;
	}

}
