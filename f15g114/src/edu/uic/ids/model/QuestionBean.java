/**
 * 
 */
package edu.uic.ids.model;

import javax.faces.bean.ManagedBean;

/**
 * @author Nitheen
 *
 */
@ManagedBean
public class QuestionBean {

	String assessment_no;
	int question_no;
	String question_type;
	String actual_question;
	String answer;
	double tolerance;
	String courseID;
	int max_points;
	int actual_points;
	String netid;
	String response;
	String response_correct;

	public String getAssessment_no() {
		return assessment_no;
	}

	public void setAssessment_no(String assessment_no) {
		this.assessment_no = assessment_no;
	}

	public int getQuestion_no() {
		return question_no;
	}

	public void setQuestion_no(int question_no) {
		this.question_no = question_no;
	}

	public String getQuestion_type() {
		return question_type;
	}

	public void setQuestion_type(String question_type) {
		this.question_type = question_type;
	}

	public String getActual_question() {
		return actual_question;
	}

	public void setActual_question(String actual_question) {
		this.actual_question = actual_question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public double getTolerance() {
		return tolerance;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	public String getCourseID() {
		return courseID;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	public int getMax_points() {
		return max_points;
	}

	public void setMax_points(int max_points) {
		this.max_points = max_points;
	}

	public int getActual_points() {
		return actual_points;
	}

	public void setActual_points(int actual_points) {
		this.actual_points = actual_points;
	}

	public String getNetid() {
		return netid;
	}

	public void setNetid(String netid) {
		this.netid = netid;
	}

	public String getResponse() {
		return response;
	}

	public String getResponse_correct() {
		return response_correct;
	}

	public QuestionBean(String assessment_no, int question_no, String question_type, String actual_question,
			String answer, double tolerance, String courseID) {
		super();
		this.assessment_no = assessment_no;
		this.question_no = question_no;
		this.question_type = question_type;
		this.actual_question = actual_question;
		this.answer = answer;
		this.tolerance = tolerance;
		this.courseID = courseID;
	}

	public QuestionBean(int question_no, String actual_question) {
		super();
		this.question_no = question_no;
		this.actual_question = actual_question;
	}

	public QuestionBean(String assessment_no, String courseID, int max_points, int actual_points, String netid) {
		super();
		this.assessment_no = assessment_no;
		this.courseID = courseID;
		this.max_points = max_points;
		this.actual_points = actual_points;
		this.netid = netid;
	}

	public QuestionBean(int question_no, String actual_question, String answer, String response,
			String response_correct) {
		super();
		this.question_no = question_no;
		this.actual_question = actual_question;
		this.answer = answer;
		this.response = response;
		this.response_correct = response_correct;
	}
	

}
