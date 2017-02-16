package edu.uic.ids.model;

/**
 * @author Nitheen
 *
 */

import edu.uic.ids.DbConnection.*;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean
public class LoginDao {
	LoginBean loginBean = new LoginBean();
	Connection connection = null;
	boolean status = false;
	ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	HttpSession session = (HttpSession) ec.getSession(true);

	int userTypeId = 0;

	public int checkLogin(String userName, String pwd) {
		try {
			connection = DbConnection.getConnection();
			PreparedStatement ps = connection
					.prepareStatement("SELECT * FROM F15G114_USER_DETAILS WHERE NET_ID=? and PASSWORD=?");
			ps.setString(1, userName);
			ps.setString(2, pwd);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				userTypeId = Integer.parseInt(rs.getString(3));
				status = true;
				ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
				HttpServletRequest request = (HttpServletRequest) ec.getRequest();
				String ip = request.getRemoteAddr();
				Statement statement = connection.createStatement();
				DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
				DateFormat df1 = new SimpleDateFormat("HH:mm:ss z");
				String sql = "Select Login_Timestamp from f15g114_user_details where Net_Id ='" + userName + "';";
				rs = statement.executeQuery(sql);
				while (rs.next()) {
					Time temp = rs.getTime(1);
					String temp_time = df1.format(temp);
					Date temp1 = rs.getDate(1);
					String temp_date = df.format(temp1);
					String finalDate = temp_date + " at "+ temp_time;
					session.setAttribute("lastLogin", finalDate);
				}
				if (userTypeId == 1)
					sql = "SELECT FIRST_NAME FROM f15g114_professor_details WHERE NET_ID ='" + userName + "';";
				else if (userTypeId == 2)
					sql = "SELECT FIRST_NAME FROM f15g114_ta_details WHERE NET_ID ='" + userName + "';";
				else
					sql = "SELECT FIRST_NAME FROM f15g114_student_details WHERE NET_ID ='" + userName + "';";
				rs = statement.executeQuery(sql);
				while (rs.next()) {
					session.setAttribute("firstName", rs.getString(1));
				}
				sql = "update f15g114_user_details set IP='" + ip + "', Login_Timestamp = null where Net_id ='"
						+ userName + "'";
				statement.executeUpdate(sql);
				sql = "INSERT INTO F15G114_LOGIN_DETAILS VALUES('" + userName + "'," + userTypeId + ",null,'" + ip
						+ "')";
				statement.executeUpdate(sql);
				statement.close();
				rs.close();
				connection.commit();

			} else {
				FacesMessage msg = new FacesMessage(
						"Invalid user credentials. Please check the username and password.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			FacesMessage msg = new FacesMessage("Unable to login. Please try again!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} finally {

			DbConnection.closeConnection(connection);
		}
		return userTypeId;
	}

}
