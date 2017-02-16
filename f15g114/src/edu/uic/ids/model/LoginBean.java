package edu.uic.ids.model;

/**
 * @author Nitheen
 *
 */

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean
public class LoginBean {

	String userName;
	String password;
	String lastLogin;
	String firstName;
	boolean loginCheck = false;
	String loginURL = "faces/userLogin.jsp";
	ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	HttpSession session = (HttpSession) ec.getSession(true);

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

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String validate() {
		LoginDao loginDao = new LoginDao();
		int check = loginDao.checkLogin(getUserName(), getPassword());
		String returnmsg;
		String temp = null;
		try {
			temp = session.getAttribute("lastLogin").toString();
			lastLogin = temp;
			temp = session.getAttribute("firstName").toString().toUpperCase();
			firstName = temp.toUpperCase();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Unable to login. Please try again!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}		
		if (check != 0) {
			session.setAttribute("login", userName);
			session.setAttribute("userType", check);

			loginCheck = true;
			if (check == 1)
				returnmsg = "instructor";
			else if (check == 2)
				returnmsg = "instructor";
			else
				returnmsg = "student";
		}

		else {
			loginCheck = false;
			returnmsg = "unsuccessful";
		}
		return returnmsg;

	}

	public String logout() {
		if (loginCheck = true) {
			try {
				session.setAttribute("login", null);
				loginCheck = false;
				ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
				ec.invalidateSession();
				FacesContext.getCurrentInstance().getExternalContext().redirect(loginURL);
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}
		return "success";
	}
}
