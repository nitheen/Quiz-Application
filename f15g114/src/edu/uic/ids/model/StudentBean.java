/**
 * 
 */
package edu.uic.ids.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

import edu.uic.ids.DbConnection.DbConnection;

/**
 * @author Nitheen
 *
 */

@ManagedBean
@SessionScoped
public class StudentBean {

	private String netid;
	private String firstName;
	private String lastName;
	private String courseID;
	private String assessment;
	private String answer;
	private String comments;
	private int max_score, actual_score, grade_id;
	Connection conn = null;
	ResultSet resultset = null;
	Statement statement = null;
	ArrayList<String> assessments = new ArrayList<String>();
	boolean displayQuiz, displayList;
	boolean instructorAccess = false;
	ArrayList<QuestionBean> questions = new ArrayList<QuestionBean>();
	ArrayList<String> response = new ArrayList<String>();
	ArrayList<String> actual_answers = new ArrayList<String>();
	ArrayList<QuestionBean> assessment_view = new ArrayList<QuestionBean>();
	ArrayList<String> response_type = new ArrayList<String>();
	ArrayList<String> tolerance = new ArrayList<String>();
	ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	HttpSession session = (HttpSession) ec.getSession(true);

	// Method to load the initial list of assessments
	public String loadAssessments() {
		netid = session.getAttribute("login").toString();
		conn = DbConnection.getConnection();
		displayList = true;
		displayQuiz = false;
		answer = "";
		assessments.clear();
		try {
			String sql = "select assessment_no from f15g114_assessments where course_id in"
					+ "(select course_id from f15g114_student_details where net_id = '" + netid + "')"
					+ "and assessment_no not in (select assessment_no from f15g114_response where net_id = '" + netid
					+ "');";
			statement = (Statement) conn.createStatement();
			//System.out.println(sql);
			resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				assessments.add(resultset.getString(1));
			}
			sql = "(select course_id from f15g114_student_details where net_id = '" + netid + "')";
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				courseID = resultset.getString(1);
			}
			if (assessments.isEmpty()) {
				displayList = false;
				FacesMessage msg = new FacesMessage("No new assessments available. Please check with your instructor.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in fetching the available assessments from the database.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} finally {
			statement = null;
			resultset = null;
			DbConnection.closeConnection(conn);
		}

		return "assessmentsPage";
	}

	// Method to load the quiz from the assessments
	public String processAssessments() {
		displayList = false;
		displayQuiz = true;
		questions.clear();
		conn = DbConnection.getConnection();
		try {
			String temp = "";
			String sql = "select Assessment_Table_Name from f15g114_assessments where Assessment_No = '" + assessment
					+ "'";
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				temp = resultset.getString(1);
			}
			statement.close();
			sql = "select Qn_no, Actual_Qn, answer,qn_type, tolerance from " + temp;
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(sql);
			max_score = 0;
			while (resultset.next()) {
				QuestionBean qb = new QuestionBean(resultset.getInt(1), resultset.getString(2));
				questions.add(qb);
				actual_answers.add(resultset.getString(3));
				response_type.add(resultset.getString(4));
				tolerance.add(resultset.getString(5));
				max_score++;
			}

		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			statement = null;
			resultset = null;
			DbConnection.closeConnection(conn);
		}
		return "success";
	}

	// Method to evaluate assessments after submission
	public String evaluateAssessment() {
		netid = session.getAttribute("login").toString();
		actual_score = 0;
		String sql = "";
		conn = DbConnection.getConnection();
		try {
			statement = (Statement) conn.createStatement();
			int j = 0;
			for (int i = 0; i < actual_answers.size(); i++) {
				j = i + 1;
				if (response_type.get(i).equalsIgnoreCase("Categorical")) {
					if (actual_answers.get(i).equalsIgnoreCase(response.get(i))) {
						actual_score++;
						sql = "INSERT INTO F15G114_RESPONSE VALUES(NULL,'" + netid + "','" + response.get(i) + "','"
								+ courseID + "','" + assessment + "'," + j + ",'YES');";
						statement.executeUpdate(sql);
					} else {
						sql = "INSERT INTO F15G114_RESPONSE VALUES(NULL,'" + netid + "','" + response.get(i) + "','"
								+ courseID + "','" + assessment + "'," + j + ",'NO');";
						statement.executeUpdate(sql);
					}
				} else if (response_type.get(i).equalsIgnoreCase("Numerical")) {
					int flag = 0;
					if (!response.get(i).isEmpty() && isNumeric(response.get(i))) {
						double temp_1 = Double.parseDouble(actual_answers.get(i));
						double temp_2 = Double.parseDouble(response.get(i));
						double temp_tolerance = Double.parseDouble(tolerance.get(i));
						if (temp_1 == temp_2) {
							actual_score++;
							flag = 1;
						}
						else if (temp_1 <= (temp_2 + temp_tolerance) && temp_1 >= (temp_2 - temp_tolerance)) {
							actual_score++;
							flag = 1;
						} else {
							flag = 0;
						}
					}
					if (flag == 1) {
						sql = "INSERT INTO F15G114_RESPONSE VALUES(NULL,'" + netid + "','" + response.get(i) + "','"
								+ courseID + "','" + assessment + "'," + j + ",'YES');";
						statement.executeUpdate(sql);
					} else {
						sql = "INSERT INTO F15G114_RESPONSE VALUES(NULL,'" + netid + "','" + response.get(i) + "','"
								+ courseID + "','" + assessment + "'," + j + ",'NO');";
						statement.executeUpdate(sql);
					}
				}
			}
			sql = "INSERT INTO F15G114_STUDENT_GRADE_DETAILS VALUES(NULL," + max_score + "," + actual_score + ",'"
					+ netid + "','" + courseID + "','" + assessment + "',null);";
			statement.executeUpdate(sql);
			FacesMessage msg = new FacesMessage("Quiz submitted and evaluated successfully!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			conn.commit();
			actual_answers.clear();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Unable to submit the Quiz. " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			answer = "";
			//e.printStackTrace();
			return "fail";
		} finally {
			statement = null;
			resultset = null;
			DbConnection.closeConnection(conn);
		}
		return "evaluatedQuiz";
	}
	
	//Method to check if the value is numeric
	public boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException e)  
	  {  
	    return false;  
	  }  
	  return true;  
	}

	// Method to load the list of assessments the user has taken
	public String loadGrades() {
		netid = session.getAttribute("login").toString();
		conn = DbConnection.getConnection();
		displayList = false;
		assessments.clear();
		String sql = "";
		try {
			sql = "select Assessment_no from f15g114_student_grade_details where net_id = '" + netid + "';";
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				assessments.add(resultset.getString(1));
			}
		} catch (MySQLSyntaxErrorException e) {
			FacesMessage msg = new FacesMessage("SQL syntax exception!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in processing request!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} finally {
			statement = null;
			resultset = null;
			DbConnection.closeConnection(conn);
		}

		return "viewGrades";
	}

	// Method to display the grades of selected assessments
	public String processGrades() {
		netid = session.getAttribute("login").toString();
		conn = DbConnection.getConnection();
		displayList = true;
		String sql = "";
		try {
			sql = "select * from f15g114_student_grade_details where assessment_no = '" + assessment + "';";
			//System.out.println(sql);
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				max_score = Integer.parseInt(resultset.getString(2));
				actual_score = Integer.parseInt(resultset.getString(3));
				courseID = resultset.getString(5);
				comments = resultset.getString(7);
			}
			fetchStudentAssessment();
		} catch (MySQLSyntaxErrorException e) {
			FacesMessage msg = new FacesMessage("SQL syntax exception!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in processing request!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//e.printStackTrace();
		} finally {
			statement = null;
			resultset = null;
			DbConnection.closeConnection(conn);
		}

		return "displayGrades";
	}

	// Method called form the Professor Bean to get the score and comments of

	// each assessment
	public void getScores(Connection conn, String assessment_name, String student_name, String prof_netid) {
		String sql = "";
		try {
			sql = "select * from f15g114_student_grade_details where assessment_no = '" + assessment_name
					+ "' and net_id = '" + student_name + "';";
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				max_score = resultset.getInt(2);
				actual_score = resultset.getInt(3);
				netid = student_name;
				assessment = assessment_name;
				comments = resultset.getString(7);
				grade_id = resultset.getInt(1);
			}
		} catch (SQLException e) {
			FacesMessage msg = new FacesMessage("Error in fetching grades and comments from database!");
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
	}

	// Method to switch to student view
	public String studentView() {
		conn = DbConnection.getConnection();
		String str = "";
		netid = session.getAttribute("login").toString();
		try {
			str = "SELECT * FROM f15g114_student_details WHERE NET_ID = '" + netid + "'";
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(str);
			if (resultset.next()) {
				instructorAccess = true;
				str = "studentView";
			} else {
				FacesMessage msg = new FacesMessage(
						"Please create a student account by uploading your credentials to the roster and then proceed to Student View.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				str = "fail";
			}
		} catch (SQLException e) {
			FacesMessage msg = new FacesMessage("Error in fetching data from database!");
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
		return str;
	}

	// Method to switch back to the professor view
	public String professorView() {
		int userType = (int) session.getAttribute("userType");
		String returnmsg = "";
		instructorAccess = false;
		if (userType == 1)
			returnmsg = "instructor";
		else if (userType == 2)
			returnmsg = "instructor";
		return returnmsg;
	}

	// Method to fetch the assessment roster from the database.
	// Method uses student netid and assessment no to fetch the required data.
	public String fetchStudentAssessment() {
		assessment_view.clear();
		conn = DbConnection.getConnection();
		String sql = "";
		String netid = session.getAttribute("login").toString();
		try {
			sql = "SELECT B.Qn_No, B.Actual_Qn,B.ANSWER, A.Response, A.Response_Correct " + "FROM F15G114_RESPONSE A, "
					+ assessment + " B WHERE A.Assessment_No = B.Assessment_No "
					+ "AND A.Course_Id = B.Course_Id AND A.Qn_No = B.Qn_No AND A.NET_ID = '" + netid
					+ "' AND A.Assessment_No = '" + assessment + "';";
			//System.out.println(sql);
			statement = (Statement) conn.createStatement();
			resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				QuestionBean qb = new QuestionBean(resultset.getInt(1), resultset.getString(2), resultset.getString(3),
						resultset.getString(4), resultset.getString(5));
				assessment_view.add(qb);
			}
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

	// Getters and setters

	public void init() {

	}

	public int getGrade_id() {
		return grade_id;
	}

	public String getNetid() {
		return netid;
	}

	public void setNetid(String netid) {
		this.netid = netid;
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

	public String getCourseID() {
		return courseID;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	public String getAssessment() {
		return assessment;
	}

	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}

	public ArrayList<String> getAssessments() {
		return assessments;
	}

	public void setAssessments(ArrayList<String> assessments) {
		this.assessments = assessments;
	}

	public boolean isDisplayQuiz() {
		return displayQuiz;
	}

	public void setDisplayQuiz(boolean displayQuiz) {
		this.displayQuiz = displayQuiz;
	}

	public boolean isDisplayList() {
		return displayList;
	}

	public void setDisplayList(boolean displayList) {
		this.displayList = displayList;
	}

	public ArrayList<QuestionBean> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<QuestionBean> questions) {
		this.questions = questions;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
		response.add(answer);
	}

	public int getMax_score() {
		return max_score;
	}

	public void setMax_score(int max_score) {
		this.max_score = max_score;
	}

	public int getActual_score() {
		return actual_score;
	}

	public void setActual_score(int actual_score) {
		this.actual_score = actual_score;
	}

	public ArrayList<String> getResponse() {
		return response;
	}

	public void setResponse(ArrayList<String> response) {
		this.response = response;
	}

	public ArrayList<String> getActual_answers() {
		return actual_answers;
	}

	public void setActual_answers(ArrayList<String> actual_answers) {
		this.actual_answers = actual_answers;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isInstructorAccess() {
		return instructorAccess;
	}

	public void setInstructorAccess(boolean instructorAccess) {
		this.instructorAccess = instructorAccess;
	}

	public ArrayList<QuestionBean> getAssessment_view() {
		return assessment_view;
	}

	public void setAssessment_view(ArrayList<QuestionBean> assessment_view) {
		this.assessment_view = assessment_view;
	}

	public StudentBean(String netid, String firstName, String lastName, String courseID) {
		this.netid = netid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.courseID = courseID;
	}

	public StudentBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StudentBean(String netid, String assessment, int max_score, int actual_score, String comments) {
		super();
		this.netid = netid;
		this.assessment = assessment;
		this.max_score = max_score;
		this.actual_score = actual_score;
		this.comments = comments;
	}

}
