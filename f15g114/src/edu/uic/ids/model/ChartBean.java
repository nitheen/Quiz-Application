package edu.uic.ids.model;

/**
 * @author Nitheen
 *
 */

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import org.apache.commons.math3.stat.StatUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import edu.uic.ids.DbConnection.DbConnection;

public class ChartBean {

	ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	HttpSession session = (HttpSession) ec.getSession(true);
	ResultSet resultset = null;
	Statement statement = null;
	Connection conn = null;
	boolean display = false;
	boolean displayAssessment = false;
	ArrayList<String> assessmentList = new ArrayList<String>();
	String assessment = null;
	String statDesc = null;
	double statValue;
	ArrayList<ChartBean> statistics = new ArrayList<>();

	public String processBarChart() throws SQLException {
		display = false;
		// String course = null;
		String assessment_list = null;
		ArrayList<String> newlist = new ArrayList<String>();
		conn = DbConnection.getConnection();
		String netid = session.getAttribute("login").toString();
		String sql = "SELECT DISTINCT COURSE_ID FROM F15G114_ASSESSMENTS WHERE NET_ID = '" + netid + "'";
		try {
			statement = conn.createStatement();
			resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				newlist.add(resultset.getString(1));
			}
			if (newlist.isEmpty()) {
				FacesMessage msg = new FacesMessage("No students have taken the assessments!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				display = false;
				return "fail";
			}

		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("No students have taken the assessments!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "noCharts";
		}
		// sql = "Select Assessment_No from f15g114_assessments where net_id =
		// '" + netid + "';";
		try {
			// statement = conn.createStatement();
			// resultset = statement.executeQuery(sql);
			// while (resultset.next()) {
			// newlist.add(resultset.getString(1));
			// display = true;
			// }
			// if(newlist.isEmpty()){
			// FacesMessage msg = new FacesMessage("No students have taken the
			// assessments!");
			// FacesContext.getCurrentInstance().addMessage(null, msg);
			// return "fail";
			// }
			assessment_list = "('";
			for (String string : newlist) {
				assessment_list = assessment_list + string;
				assessment_list = assessment_list + "','";
			}
			assessment_list = assessment_list + "')";
			// System.out.println(assessment_list);
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Unable to fetch list of assessments from the database!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// e.printStackTrace();
		}
		sql = "SELECT SCORED_POINTS,Assessment_No,Net_id FROM f15g114_student_grade_details where course_id in "
				+ assessment_list;
		// System.out.println(sql);
		try {
			statement = conn.createStatement();
			resultset = statement.executeQuery(sql);
			Result result = ResultSupport.toResult(resultset);
			Object[][] sData = result.getRowsByIndex();
			// double[] values = new double[sData.length];
			// String[] tDate = new String[sData.length];

			JFreeChart chart = null;
			FacesContext context = FacesContext.getCurrentInstance();
			String path = context.getExternalContext().getRealPath("/files");
			File out = null;
			out = new File(path + "/barChart.png");
			chart = createBarChart("Student Scores by Assessment", "label", sData);
			ChartUtilities.saveChartAsPNG(out, chart, 600, 450);
		} catch (IOException e) {
			FacesMessage msg = new FacesMessage("Error in generating the chart!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// e.printStackTrace();
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			statement.close();
			resultset.close();
			DbConnection.closeConnection(conn);
		}
		return "barChart";
	}

	public JFreeChart createBarChart(String title, String ticker, Object[][] sData) {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < sData.length; i++) {
			String temp1 = sData[i][1].toString();
			String temp2 = sData[i][2].toString();
			int temp = (int) sData[i][0];
			System.out.println(temp + "::" + temp1 + "::" +temp2);
			dataset.addValue(temp, temp1, temp2);
		}
		JFreeChart chart = ChartFactory.createBarChart3D("Bar Chart", "Student Net ID", "Points Scored", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		display = true;
		return chart;
	}

	// Code for time series chart

	public String processTimeSeriesChart() throws SQLException {
		display = false;
		String assessment_list = null;
		conn = DbConnection.getConnection();
		ArrayList<String> newlist = new ArrayList<String>();
		String netid = session.getAttribute("login").toString();
		String sql = "Select distinct course_id from f15g114_assessments where net_id = '" + netid + "';";
		try {
			statement = conn.createStatement();
			resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				newlist.add(resultset.getString(1));
			}
			if (newlist.isEmpty()) {
				FacesMessage msg = new FacesMessage("No students have taken the assessments!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				display = false;
				return "noCharts";
			}
			
			assessment_list = "('";
			for (String string : newlist) {
				assessment_list = assessment_list + string;
				assessment_list = assessment_list + "','";
			}
			assessment_list = assessment_list + "')";
			// System.out.println(assessment_list);
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Unable to fetch list of assessments from the database!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// e.printStackTrace();
		}
		sql = "SELECT avg(scored_points),Assessment_No FROM f15g114_student_grade_details where course_id in "
				+ assessment_list + "group by Assessment_No";
		// System.out.println(sql);
		try {
			statement = conn.createStatement();
			resultset = statement.executeQuery(sql);
			Result result = ResultSupport.toResult(resultset);
			Object[][] sData = result.getRowsByIndex();
			// double[] values = new double[sData.length];
			// String[] tDate = new String[sData.length];

			JFreeChart chart = null;
			FacesContext context = FacesContext.getCurrentInstance();
			String path = context.getExternalContext().getRealPath("/files");
			File out = null;
			out = new File(path + "/timeSeries.png");
			// System.out.println(out);
			chart = createTimeSeriesChart(sData);
			display = true;
			ChartUtilities.saveChartAsPNG(out, chart, 600, 450);
		} catch (IOException e) {
			FacesMessage msg = new FacesMessage("Error in generating the chart!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// e.printStackTrace();
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			statement.close();
			resultset.close();
			DbConnection.closeConnection(conn);
		}
		return "timeSeries";
	}

	public JFreeChart createTimeSeriesChart(Object[][] sData) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < sData.length; i++) {
			String temp = sData[i][1].toString();
			BigDecimal temp1 = (BigDecimal) sData[i][0];
			double temp2 = temp1.doubleValue();
			dataset.addValue(temp2, "Mean Scores", temp);
		}

		JFreeChart lineChart = ChartFactory.createLineChart("Average score of class by assessments", "Assessment",
				"Mean Scores", dataset, PlotOrientation.VERTICAL, true, true, false);
		display = true;
		return lineChart;
	}

	public String descriptiveStatsLoad() throws SQLException {
		conn = DbConnection.getConnection();
		display = false;
		assessmentList.clear();
		try {
			String netid = session.getAttribute("login").toString();
			String sql = "Select Assessment_No from f15g114_assessments where net_id = '" + netid + "';";
			statement = conn.createStatement();
			resultset = statement.executeQuery(sql);
			while (resultset.next()) {
				assessmentList.add(resultset.getString(1));
			}
			if (assessmentList.isEmpty()) {
				FacesMessage msg = new FacesMessage("No students have taken the assessments!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return "noCharts";
			}
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in fetching the list of assessments from the database!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// e.printStackTrace();
		} finally {
			statement.close();
			resultset.close();
			DbConnection.closeConnection(conn);
		}
		displayAssessment = true;
		return "descriptiveStatsLoad";
	}

	public String descriptiveStats() {

		int rowcount = 0;
		statistics.clear();
		conn = DbConnection.getConnection();
		try {
			statement = conn.createStatement();
			String sql = "SELECT SCORED_POINTS FROM F15G114_STUDENT_GRADE_DETAILS WHERE ASSESSMENT_NO ='" + assessment
					+ "'";
			resultset = statement.executeQuery(sql);
			if (resultset.last()) {
				rowcount = resultset.getRow();
				resultset.beforeFirst();
			}
			double[] values = new double[rowcount];
			int i = 0;
			while (resultset.next()) {
				values[i] = Double.parseDouble(resultset.getString(1));
				// System.out.println(resultset.getInt(1));
				i++;
			}

			double minValue = StatUtils.min(values);
			addValue(minValue, "Minimum Value");
			double maxValue = StatUtils.max(values);
			addValue(maxValue, "Maximum Value");
			double mean = StatUtils.mean(values);
			addValue(mean, "Mean");
			double variance = StatUtils.variance(values, mean);
			addValue(variance, "Variance");
			double std = Math.sqrt(variance);
			addValue(std, "Standard Deviation");
			double median = StatUtils.percentile(values, 50.0);
			addValue(median, "Median");
			double q1 = StatUtils.percentile(values, 25.0);
			addValue(q1, "1st Quartile");
			double q3 = StatUtils.percentile(values, 75.0);
			addValue(q3, "3rd Quartile");
			double iqr = q3 - q1;
			addValue(iqr, "Interquartile Range");
			double range = maxValue - minValue;
			addValue(range, "Range");
			display = true;
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error in calculating the descriptive statistics!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// e.printStackTrace();
		} finally {
			DbConnection.closeConnection(conn);
		}

		return "descriptiveStats";
	}

	public void addValue(double statValue, String statDesc) {
		ChartBean cb = new ChartBean(statDesc, statValue);
		statistics.add(cb);
		// System.out.println(cb.statDesc + "::::" + cb.statValue);
	}

	public boolean isDisplayAssessment() {
		return displayAssessment;
	}

	public void setDisplayAssessment(boolean displayAssessment) {
		this.displayAssessment = displayAssessment;
	}

	public ArrayList<String> getAssessmentList() {
		return assessmentList;
	}

	public void setAssessmentList(ArrayList<String> assessmentList) {
		this.assessmentList = assessmentList;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public String getAssessment() {
		return assessment;
	}

	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}

	public ArrayList<ChartBean> getStatistics() {
		return statistics;
	}

	public void setStatistics(ArrayList<ChartBean> statistics) {
		this.statistics = statistics;
	}

	public String getStatDesc() {
		return statDesc;
	}

	public void setStatDesc(String statDesc) {
		this.statDesc = statDesc;
	}

	public double getStatValue() {
		return statValue;
	}

	public void setStatValue(double statValue) {
		this.statValue = statValue;
	}

	public ChartBean(String statDesc, double statValue) {
		super();
		this.statDesc = statDesc;
		this.statValue = statValue;
	}

	public ChartBean() {
		super();
	}

}
