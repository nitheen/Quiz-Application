<%@page import="edu.uic.ids.model.LoginBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Tutor Home Page</title>
</head>
<body style="background-color: white">

	<%
		if (session.getAttribute("login") == null)
			response.sendRedirect("userLogin.jsp");
	%>

	<f:view>
		<div
			style="background-color: #bfbfbf; border-bottom-style: solid; border-top-style: solid; border-left-style: solid; border-right-style: solid">
			<center>
				<f:verbatim>
					<h2>Tutor Home Page</h2>
				</f:verbatim>
				<h:form>
					<h:commandLink action="#{studentBean.studentView}"
						value="Switch to Student View">
					</h:commandLink>
					<br>
					<a href="/f15g114/files/Professor_Tutor_User_Guide.pdf"
						target="_blank">Instructor/TA User Guide</a>
					<br>
					<h:commandLink action="#{loginBean.logout}" value="Logout">
					</h:commandLink>

				</h:form>
			</center>

		</div>
		<center>
			<div>
				<br>
				<h:messages globalOnly="true" style="color:red"></h:messages>
				<h4>
					Welcome
					<h:outputText value="#{loginBean.firstName}"></h:outputText>
				</h4>
				<a href="faces/fileUpload.jsp">Upload
					Assessment</a><br>
				<h:form>
					<h:commandLink action="#{professorBean.viewAssessments}"
						value="View Assessments">
					</h:commandLink>
					<br>
					<h:commandLink action="#{professorBean.viewRoster}"
						value="View Student Roster">
					</h:commandLink>
					<br>
					<h:commandLink action="#{professorBean.classGrades}"
						value="View Class Grades">
					</h:commandLink>
					<br>
					<h:commandLink action="#{professorBean.viewAssessmentsRoster}"
						value="View Assessments Roster">
					</h:commandLink>
					<br>
				</h:form>
			</div>
		</center>
		<div
			style="position: fixed; bottom: 0; width: 100%; background-color: #bfbfbf;">
			<i>Last Login at <h:outputText value="#{loginBean.lastLogin}"></h:outputText>
			</i>
		</div>
	</f:view>
</body>
</html>