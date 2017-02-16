<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Assessment Results</title>
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
					<h2>View Assessments Roster</h2>
				</f:verbatim>
				<a href="faces/instructorHome.jsp">Home</a> <br> <a
					href="/f15g114/files/Professor_Tutor_User_Guide.pdf"
					target="_blank">Instructor/TA User Guide</a>
				<h:form>
					<h:commandLink action="#{loginBean.logout}" value="Logout">
					</h:commandLink>
				</h:form>
			</center>
		</div>
		<div>
			<center>
				<br>
				<h:messages globalOnly="true" style="color:green"></h:messages>
				<br>
				<h:form>
					<h:outputLabel rendered="#{professorBean.displayAssessment}"
						value="Select Assessment to View : " />
					<h:selectOneMenu rendered="#{professorBean.displayAssessment}"
						value="#{professorBean.name}">
						<f:selectItems value="#{professorBean.names}" />
					</h:selectOneMenu> &nbsp;
					<h:commandButton id="upload" type="submit"
						rendered="#{professorBean.displayAssessment}"
						action="#{professorBean.fetchStudentList}" value="Submit" />
					<h:outputLabel rendered="#{professorBean.displayStudent}"
						value="Selected Assessment : " />
					<h:outputText rendered="#{professorBean.displayStudent}"
						value="#{professorBean.name}"></h:outputText>
					<br>
					<br>
					<h:outputLabel rendered="#{professorBean.displayStudent}"
						value="Select Student : " />
					<h:selectOneMenu rendered="#{professorBean.displayStudent}"
						value="#{professorBean.student_name}">
						<f:selectItems value="#{professorBean.student_list}" />
					</h:selectOneMenu>
					<br>
					<br>
					<h:commandButton id="fetchResult" type="submit"
						rendered="#{professorBean.displayStudent}"
						action="#{professorBean.fetchStudentAssessment}"
						value="Fetch Data" />
					<h:commandButton id="reset" type="submit"
						action="#{professorBean.displayRosterReset}" value="Reset" />
				</h:form>
			</center>
		</div>
	</f:view>
</body>
</html>