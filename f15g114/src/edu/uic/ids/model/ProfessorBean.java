package edu.uic.ids.model;

/**
 * @author Nitheen
 *
 */

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
//import javax.servlet.jsp.jstl.sql.Result;
//import javax.servlet.jsp.jstl.sql.ResultSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.myfaces.custom.fileupload.UploadedFile;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

import edu.uic.ids.DbConnection.DbConnection;

@ManagedBean
@SessionScoped
public class ProfessorBean<HttpServletResponse> {

	private UploadedFile uploadedFile;
	private String fileLabel;
	private String fileName;
	private String fileContentType;
	private String name;
	private String comments;
	private ArrayList<String> names = new ArrayList<String>();
	private String student_name;
	private ArrayList<String> student_list = new ArrayList<String>();
	private ArrayList<QuestionBean> assessment = new ArrayList<QuestionBean>();
	private ArrayList<StudentBean> roster = new ArrayList<StudentBean>();
	Statement statement = null;
	ResultSet resultset = null;
	Connection conn = null;
	boolean displayAssessment = false;
	boolean display = false;
	boolean displayRoster = false;
	boolean displayStudent = false;
	BufferedReader br = null;
	private static final String NEW_LINE_SEPARATOR = "\n";
	ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	HttpSession session = (HttpSession) ec.getSession(true);
	StudentBean sb = new StudentBean();

	// Method to process the assessments upload file.
	// In this method a new table is created for every upload.
	// An entry is first made in the main assessments table where the the new
	// table name is mapped.
	// Post that the data from CSV is transferred to the database.

	public String processFileUpload() throws SQLException {

		//System.out.println("inside file upload");
		String netid = session.getAttribute("login").toString();
		int n = 0;
		String course_id = null;
		String tempFile = uploadProcess();
		if (fileLabel.equals("")) {
			String[] parts = tempFile.split(".");
			fileLabel = parts[0];
		}
		String create_table = "CREATE TABLE " + fileLabel + " (" + "Assessment_No varchar(50)  NOT NULL,"
				+ "Qn_No int  NOT NULL," + "Qn_Type varchar(20)  NOT NULL," + "Actual_Qn text  NOT NULL,"
				+ "Answer text  NOT NULL," + "Tolerance decimal(10,2)," + "Course_Id varchar(10)  NOT NULL,"
				+ "FOREIGN KEY (Course_Id)" + "REFERENCES f15g114_Course_Details (Course_Id));";

		//System.out.println(create_table);
		conn = DbConnection.getConnection();
		try {
			statement = (Statement) conn.createStatement();
			statement.executeUpdate(create_table);
			statement.close();
		} catch (SQLException e) {
			FacesMessage msg = new FacesMessage("Assessment already exists. Please enter a new assessment.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
			return "fail";
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in creating a new assessment!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
			return "fail";
		}
		try {
			String sql = null;
			int temp = (int) session.getAttribute("userType");
			if (temp == 1)
				sql = "SELECT COURSE_ID FROM F15G114_PROFESSOR_DETAILS WHERE NET_ID ='" + netid + "'";
			else
				sql = "SELECT COURSE_ID FROM F15G114_TA_DETAILS WHERE NET_ID ='" + netid + "'";
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(sql);
			course_id = null;
			while (resultset.next()) {
				course_id = resultset.getString(1);
			}
			statement.close();
		} catch (SQLException e) {
			FacesMessage msg = new FacesMessage(
					"Course does not exist! Please verify the course exists and try again!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in fetching the course details to process the request!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		}
		try {
			String sql = "";
			String tokens[] = null;
			String Assessment_no = null;
			String line = "";
			br = new BufferedReader(new FileReader(tempFile));
			line = br.readLine();
			line = br.readLine();
			//System.out.println(line);
			tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
			Assessment_no = fileLabel;			
			//System.out.println(sql);
			br.close();			
			br = new BufferedReader(new FileReader(tempFile));
			br.readLine();
			while ((line = br.readLine()) != null) {
				tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				String str = "insert into " + fileLabel + "  values (?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement query1 = conn.prepareStatement(str);
				query1.setString(1, fileLabel);
				query1.setString(2, tokens[0]);
				query1.setString(3, tokens[1]);
				String temp = tokens[2].replace("\"", "");
				query1.setString(4, temp);
				query1.setString(5, tokens[3]);
				if (tokens[4].isEmpty())
					query1.setString(6, null);
				else
					query1.setString(6, tokens[4]);
				query1.setString(7, course_id);
				//System.out.println(query1);
				query1.executeUpdate();
				n++;				
			}			
			
			sql = "INSERT INTO F15G114_ASSESSMENTS VALUES ('" + Assessment_no + "','" + fileLabel + "','" + course_id
					+ "','" + netid + "')";
			statement = (Statement) conn.createStatement();
			statement.executeUpdate(sql);
			statement.close();
			FacesMessage msg = new FacesMessage(
					"Assessment was successfully uploaded into the database! " + n + " rows inserted!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			fileLabel = "";
			conn.commit();
		} catch (SQLException e) {
			String sql = "drop table "+fileLabel;
			statement = conn.createStatement();
			statement.executeUpdate(sql);
			FacesMessage msg = new FacesMessage("Unable to upload the assessment to the database!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} catch (Exception e) {
			String sql = "drop table if exists "+fileLabel;
			statement = conn.createStatement();
			statement.executeUpdate(sql);
			FacesMessage msg = new FacesMessage("Error in processing the upload request. ");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} finally {
			statement = null;
			resultset = null;
			try {
				DbConnection.closeConnection(conn);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		return "success";
	}

	// Method to upload the file that user selects into the server.
	// This method returns the path of the uploaded file.

	public String uploadProcess() {

		FacesContext context = FacesContext.getCurrentInstance();
		String path = context.getExternalContext().getRealPath("/files");
		File tempFile = null;
		FileOutputStream fos = null;
		try {
			fileName = uploadedFile.getName();
			fileContentType = uploadedFile.getContentType();
			//System.out.println(fileContentType);
			tempFile = new File(path + "/" + fileName);
			//System.out.println(tempFile);
			fos = new FileOutputStream(tempFile);
			fos.write(uploadedFile.getBytes());
			fos.close();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in uploading the file.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		}

		String temp = tempFile.toString();
		return temp;
	}

	// Method that runs before the ViewAssessments page loads in order to
	// populate the available assessments

	public String viewAssessments() throws SQLException {
		String netid = session.getAttribute("login").toString();
		int userType = (int) session.getAttribute("userType");
		String course_id = "";
		names.clear();
		displayAssessment = false;
		conn = DbConnection.getConnection();
		String sql = "";
		//System.out.println(sql);
		try {
			if (userType == 1)
				sql = "Select course_id from f15g114_professor_details where net_id = '" + netid + "'";
			else if (userType == 2)
				sql = "Select course_id from f15g114_ta_details where net_id = '" + netid + "'";
			//System.out.println(sql);
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				course_id = resultset.getString(1);
			}
			//System.out.println(course_id);
			sql = "select Assessment_No from f15g114_assessments where course_id= '" + course_id + "'";
			//System.out.println(sql);
			resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				//System.out.println(resultset.getString(1));
				names.add(resultset.getString(1));
			}
			if (names.isEmpty()) {
				FacesMessage msg = new FacesMessage("No assessments exist!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				display = false;
			} else {
				name = names.get(0);
				display = true;
			}
			statement.close();
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			statement = null;
			resultset = null;
			DbConnection.closeConnection(conn);
		}
		return "viewAssessments";
	}

	// Method to display the uploaded assessments

	public String processViewAssessments() {
		assessment.clear();
		conn = DbConnection.getConnection();
		String sql = "select Assessment_Table_Name from f15g114_assessments where Assessment_No = '" + name + "'";
		//System.out.println(sql);
		String temp = null;
		try {
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(sql);

			while (resultset.next()) {
				temp = resultset.getString(1);
			}
			statement.close();

			sql = "select * from " + temp;
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				QuestionBean qb = new QuestionBean(resultset.getString(1), resultset.getInt(2), resultset.getString(3),
						resultset.getString(4), resultset.getString(5), resultset.getDouble(6), resultset.getString(7));
				assessment.add(qb);
			}
			displayAssessment = true;
			if (assessment.isEmpty()) {
				displayAssessment = false;
			}
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			DbConnection.closeConnection(conn);
		}
		return "success";
	}

	// Method to load the list of assessments in the view assessments roster
	// page
	public String viewAssessmentsRoster() {
		try {
			viewAssessments();
			displayAssessment = true;
			displayStudent = false;
		} catch (SQLException e) {
			FacesMessage msg = new FacesMessage("Error in fetching the assessments list from the database");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		}
		return "viewAssessmentRoster";
	}

	// Method to fetch the student list for the view Assessment roster page
	// after selecting the assessment name
	public String fetchStudentList() {
		student_list.clear();
		//System.out.println(name);
		conn = DbConnection.getConnection();
		String sql = "";
		try {
			sql = "SELECT DISTINCT NET_ID FROM F15G114_RESPONSE WHERE ASSESSMENT_NO = '" + name + "';";
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				student_list.add(resultset.getString(1));
			}
			student_name = student_list.get(0);
			displayAssessment = false;
			displayStudent = true;
		} catch (SQLException e) {
			FacesMessage msg = new FacesMessage("Error in fetching the data from the database. " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			displayAssessment = true;
			displayStudent = false;
			FacesMessage msg = new FacesMessage("No students have taken the selected assignment. ");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in processing the request. " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} finally {
			statement = null;
			resultset = null;
			DbConnection.closeConnection(conn);
		}

		return "studentList";
	}

	// Method to reset the roster page
	public String displayRosterReset() {
		displayAssessment = true;
		displayStudent = false;
		return "reset";
	}

	// Method to fetch the assessment roster from the database.
	// Method uses student netid and assessment no to fetch the required data.
	public String fetchStudentAssessment() {
		assessment.clear();
		conn = DbConnection.getConnection();
		String sql = "";
		String netid = session.getAttribute("login").toString();
		try {
			sql = "SELECT B.Qn_No, B.Actual_Qn,B.ANSWER, A.Response, A.Response_Correct " + "FROM F15G114_RESPONSE A, "
					+ name + " B WHERE A.Assessment_No = B.Assessment_No "
					+ "AND A.Course_Id = B.Course_Id AND A.Qn_No = B.Qn_No AND A.NET_ID = '" + student_name
					+ "' AND A.Assessment_No = '" + name + "';";
			//System.out.println(sql);
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				QuestionBean qb = new QuestionBean(resultset.getInt(1), resultset.getString(2), resultset.getString(3),
						resultset.getString(4), resultset.getString(5));
				assessment.add(qb);
			}
			sb.getScores(conn, name, student_name, netid);
			StudentBean values = new StudentBean(sb.getNetid(), sb.getAssessment(), sb.getMax_score(),
					sb.getActual_score(), sb.getComments());
			roster.clear();
			roster.add(values);
			comments = sb.getComments();
		} catch (SQLException e) {
			// StringWriter sw = new StringWriter();
			// PrintWriter pw = new PrintWriter(sw);
			// e.printStackTrace(pw);
			// String trace = sw.toString();
			// FacesMessage msg = new FacesMessage(sw.toString());
			FacesMessage msg = new FacesMessage("Error in fetching the data from the database. " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in processing request. " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} finally {
			statement = null;
			resultset = null;
			DbConnection.closeConnection(conn);
		}
		return "studentAssessment";
	}

	// Method to delete the table
	public String processDeleteAssessments() {
		conn = DbConnection.getConnection();
		String sql = "";
		try {
			sql = "DROP TABLE " + name;
			Statement statement = (Statement) conn.createStatement();
			statement.executeUpdate(sql);
			sql = "DELETE FROM f15g114_assessments WHERE Assessment_No = '" + name + "';";
			//System.out.println(sql);
			statement.executeUpdate(sql);
			conn.commit();
			DbConnection.closeConnection(conn);
			FacesMessage msg = new FacesMessage("Successfully deleted the assessment");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			viewAssessments();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in deleting the Assessment");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		}
		displayAssessment = false;
		return "success";
	}

	// Method to save Comments
	public String saveComments() {
		conn = DbConnection.getConnection();
		String sql = "";
		try {
			sql = "UPDATE f15g114_student_grade_details SET comments = '" + comments + "' WHERE Grade_Id = "
					+ sb.getGrade_id();
			statement = (Statement) conn.createStatement();
			statement.executeUpdate(sql);
			conn.commit();
			FacesMessage msg = new FacesMessage("Comments successfully saved in the database");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (SQLException e) {
			FacesMessage msg = new FacesMessage("Error in updating the data. " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in processing request. " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} finally {
			statement = null;
			resultset = null;
			DbConnection.closeConnection(conn);
		}
		
		return "saveComments";
	}

	// Method to process Student roster upload
	public String processRosterUpload() throws SQLException {

		conn = DbConnection.getConnection();
		String tempFile = uploadProcess();
		String line = "";
		String tokens[] = null;
		int n = 0;
		try {
			br = new BufferedReader(new FileReader(tempFile));
			br.readLine();
			while ((line = br.readLine()) != null) {
				tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				String str = "insert into f15g114_user_details values (?, ?, ?, ?, ?)";
				PreparedStatement query1 = conn.prepareStatement(str);
				query1.setString(1, tokens[0]);
				query1.setString(2, tokens[1]);
				query1.setString(3, "3");
				query1.setString(4, null);
				query1.setString(5, "0.0.0.0");

				//System.out.println(query1);
				query1.executeUpdate();
			}
			br.close();
		} catch (SQLException e) {
			FacesMessage msg = new FacesMessage("Error in uploading the roster in User	Details table. SQL Exception");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(
					"Error in uploading the roster in User	Details table. " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		}

		try {
			br = new BufferedReader(new FileReader(tempFile));
			br.readLine();
			while ((line = br.readLine()) != null) {
				tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				String str = "insert into f15g114_student_details values (?, ?, ?, ?)";
				PreparedStatement query1 = conn.prepareStatement(str);
				query1.setString(1, tokens[0]);
				query1.setString(2, tokens[2]);
				query1.setString(3, tokens[3]);
				query1.setString(4, tokens[4]);

				//System.out.println(query1);
				query1.executeUpdate();
				n++;
				conn.commit();
			}

			FacesMessage msg = new FacesMessage(
					"Roster was successfully uploaded into the database! " + n + " rows successfully inserted");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (MySQLIntegrityConstraintViolationException e) {
			FacesMessage msg = new FacesMessage(
					"Error in uploading the roster in User	Details table. Wrong course entered in the roster file!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(
					"Error in uploading the roster in Student Details table! Please try again!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} finally {
			statement = null;
			resultset = null;
			DbConnection.closeConnection(conn);
		}

		return "success";
	}

	// Method to view the enrolled students

	public String viewRoster() {
		String netid = session.getAttribute("login").toString();
		int userType = (int) session.getAttribute("userType");
		roster.clear();
		conn = DbConnection.getConnection();
		String sql = "";
		if (userType == 1)
			sql = "select course_id from f15g114_professor_details where net_id = '" + netid + "'";
		else if (userType == 2)
			sql = "select course_id from f15g114_ta_details where net_id = '" + netid + "'";
		//System.out.println(sql);
		String temp = null;
		try {
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(sql);

			while (resultset.next()) {
				temp = resultset.getString(1);
			}
			statement.close();
			sql = "select * from f15g114_student_details where course_id = '" + temp + "'";
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				StudentBean sb = new StudentBean(resultset.getString(1), resultset.getString(2), resultset.getString(3),
						resultset.getString(4));
				roster.add(sb);
			}
			displayRoster = true;

		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			statement = null;
			resultset = null;
			DbConnection.closeConnection(conn);
		}
		return "viewRoster";
	}

	// Method to load the courses for class grades view
	public String classGrades() {
		conn = DbConnection.getConnection();
		names.clear();
		String netid = session.getAttribute("login").toString();
		displayRoster = false;
		String sql = "";
		try {
			int userType = (int) session.getAttribute("userType");
			if(userType == 1)
				sql = "SELECT COURSE_ID FROM F15G114_PROFESSOR_DETAILS WHERE NET_ID = '" + netid + "';";
			else if (userType == 2)
				sql = "SELECT COURSE_ID FROM F15G114_TA_DETAILS WHERE NET_ID = '" + netid + "';";
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(sql);

			while (resultset.next()) {
				names.add(resultset.getString(1));
			}
			name = names.get(0);
			statement.close();
		} catch (MySQLSyntaxErrorException e) {
			FacesMessage msg = new FacesMessage("SQL syntax exception!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
			return "fail";
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in loading the course values!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
			return "fail";
		} finally {
			statement = null;
			resultset = null;
			DbConnection.closeConnection(conn);
		}
		return "classGrades";
	}

	// Method to view the class grades
	public String processClassGrades() {
		conn = DbConnection.getConnection();
		assessment.clear();
		String sql = "";
		try {
			sql = "SELECT * FROM F15G114_STUDENT_GRADE_DETAILS WHERE COURSE_ID = '" + name + "';";
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				QuestionBean qb = new QuestionBean(resultset.getString(6), resultset.getString(5), resultset.getInt(2),
						resultset.getInt(3), resultset.getString(4));
				assessment.add(qb);
			}
		} catch (MySQLSyntaxErrorException e) {
			FacesMessage msg = new FacesMessage("SQL syntax exception!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
			return "fail";
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in loading the course values!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
			return "fail";
		} finally {
			statement = null;
			resultset = null;
			DbConnection.closeConnection(conn);
		}
		displayRoster = true;
		return "";
	}

	// Method to process file download for roster
	public String processFileDownload() {

		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		// FileOutputStream fos = null;
		String path = fc.getExternalContext().getRealPath("/files");
		String fileNameBase = "AssessmentRoster.csv";
		// need to differentiate for multiple users
		String fileName = path + "/" + student_name + "_" + fileNameBase;
		// //System.out.println(fileName);
		File f = new File(fileName);
		conn = DbConnection.getConnection();
		CSVPrinter csvFilePrinter = null;
		try {
			FileWriter fw = new FileWriter(fileName);
			csvFilePrinter = new CSVPrinter(fw, csvFileFormat);
			String sql = "SELECT B.Qn_No, B.Actual_Qn,B.ANSWER, A.Response, A.Response_Correct "
					+ "FROM F15G114_RESPONSE A," + name + " B WHERE A.Assessment_No = B.Assessment_No "
					+ "AND A.Course_Id = B.Course_Id AND A.Qn_No = B.Qn_No AND A.NET_ID = '" + student_name
					+ "' AND A.Assessment_No = '" + name + "';";
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(sql);

			ResultSetMetaData metaData = resultset.getMetaData();
			int count = metaData.getColumnCount(); // number of column
			// String columnName[] = new String[count];

			for (int i = 1; i <= count; i++) {
				// columnName[i-1] = metaData.getColumnLabel(i);
				fw.append(metaData.getColumnLabel(i));
				fw.append(',');
			}
			fw.append('\n');

			while (resultset.next()) {
				List<String> questions = new ArrayList<String>();
				for (int i = 1; i <= count; i++) {
					questions.add(resultset.getString(i));
				}
				csvFilePrinter.printRecord(questions);
			}

			// Result result = ResultSupport.toResult(resultset);
			// Object[][] sData = result.getRowsByIndex();
			// String columnNames[] = result.getColumnNames();
			// StringBuffer sb = new StringBuffer();
			// fos = new FileOutputStream(fileName);
			// for (int i = 0; i < columnNames.length; i++) {
			// sb.append(columnNames[i].toString() + ",");
			// }
			// sb.append("\n");
			// fos.write(sb.toString().getBytes());
			// for (int i = 0; i < sData.length; i++) {
			// sb = new StringBuffer();
			// for (int j = 0; j < sData[0].length; j++) {
			// sb.append(sData[i][j].toString() + ",");
			// }
			// sb.append("\n");
			// fos.write(sb.toString().getBytes());
			// }
			fw.flush();
			fw.close();
		} catch (FileNotFoundException e) {
			FacesMessage msg = new FacesMessage("Error in creating a new file!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} catch (SQLException e) {
			FacesMessage msg = new FacesMessage("SQL syntax exception!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} catch (IOException e) {
			FacesMessage msg = new FacesMessage("Error in processing the file!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in processing the Request. " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} finally {
			statement = null;
			resultset = null;
			DbConnection.closeConnection(conn);
		}
		String mimeType = ec.getMimeType(fileName);
		FileInputStream in = null;
		byte b;
		ec.responseReset();
		ec.setResponseContentType(mimeType);
		ec.setResponseContentLength((int) f.length());
		ec.setResponseHeader("Content-Disposition",
				"attachment; filename=\"" + student_name + "_" + fileNameBase + "\"");
		try {
			in = new FileInputStream(f);
			OutputStream output = ec.getResponseOutputStream();
			while (true) {
				b = (byte) in.read();
				if (b < 0)
					break;
				output.write(b);
			}
		} catch (IOException e) {
			FacesMessage msg = new FacesMessage("Error in processing the file!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				FacesMessage msg = new FacesMessage("Error in file processing!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				//e.printStackTrace();
			}
		}
		fc.responseComplete();
		return "success";
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getFileLabel() {
		return fileLabel;
	}

	public void setFileLabel(String fileLabel) {
		this.fileLabel = fileLabel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getNames() {
		return names;
	}

	public void setNames(ArrayList<String> names) {
		this.names = names;
	}

	public ArrayList<QuestionBean> getAssessment() {
		return assessment;
	}

	public void setAssessment(ArrayList<QuestionBean> assessment) {
		this.assessment = assessment;
	}

	public boolean isDisplayAssessment() {
		return displayAssessment;
	}

	public void setDisplayAssessment(boolean displayAssessment) {
		this.displayAssessment = displayAssessment;
	}

	public ArrayList<StudentBean> getRoster() {
		return roster;
	}

	public void setRoster(ArrayList<StudentBean> roster) {
		this.roster = roster;
	}

	public boolean isDisplayRoster() {
		return displayRoster;
	}

	public void setDisplayRoster(boolean displayRoster) {
		this.displayRoster = displayRoster;
	}

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public ArrayList<String> getStudent_list() {
		return student_list;
	}

	public void setStudent_list(ArrayList<String> student_list) {
		this.student_list = student_list;
	}

	public boolean isDisplayStudent() {
		return displayStudent;
	}

	public void setDisplayStudent(boolean displayStudent) {
		this.displayStudent = displayStudent;
	}

	public String getComments() {
		return comments;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
