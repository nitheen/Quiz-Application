<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Student Home Page</title>
</head>
<body style="background-color: white">

	<%
		if (session.getAttribute("login") == null)
			response.sendRedirect("userLogin.jsp");
	%>

	<f:view>

		<center>
			<div
				style="background-color: #bfbfbf; border-bottom-style: solid; border-top-style: solid; border-left-style: solid; border-right-style: solid">
				<center>
					<f:verbatim>
						<h2>Student Home Page</h2>
					</f:verbatim>
					<h:form>
						<h:commandLink rendered="#{studentBean.instructorAccess}"
							action="#{studentBean.professorView}"
							value="Back to Instructor View">
						</h:commandLink>
						<br>
						<a href="/f15g114/files/Student_User_Guide.pdf" target="_blank">Student
							User Guide</a>
						<br>
						<h:commandLink action="#{loginBean.logout}" value="Logout">
						</h:commandLink>
					</h:form>
				</center>
			</div>
			<div>
				<h4>
					Welcome <%=session.getAttribute("firstName")%>
				</h4>
				<br>
				<h:form>
					<h:commandLink action="#{studentBean.loadAssessments}"
						value="Attempt Quiz">
					</h:commandLink>
					<br>
					<h:commandLink action="#{studentBean.loadGrades}"
						value="View Grades">
					</h:commandLink>
				</h:form>
			</div>
		</center>
		<div style="position: fixed; bottom: 0; width: 100%; background-color: #bfbfbf;">
				<i>Last Login at <%= session.getAttribute("lastLogin") %>
				</i>
			</div>
	</f:view>
</body>
</html>