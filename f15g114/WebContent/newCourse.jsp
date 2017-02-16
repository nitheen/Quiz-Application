<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create New Course</title>
</head>
<body style="background-color: white">
	<f:view>
		<div
			style="background-color: #bfbfbf; border-bottom-style: solid; border-top-style: solid; border-left-style: solid; border-right-style: solid">
			<f:verbatim>
				<center>
					<h2>Course Registration Page</h2>
					<a href="faces/userLogin.jsp">Login Page</a> <br> <a
						href="/f15g114/files/Professor_Tutor_User_Guide.pdf"
						target="_blank">Instructor/TA User Guide</a> <br>
				</center>
			</f:verbatim>
		</div>
		<center>
			<div>
				<br>
				<h:messages globalOnly="true" style="color:green"></h:messages>
				<br>
				<h:form>
					<h:panelGrid columns="3">
						<h:outputLabel value="Course ID" />
						<h:inputText id="courseID" required="true"
							value="#{tutorBean.course}"
							requiredMessage="A course id is required" />
						<h:message showSummary="true" style="color:red" showDetail="false"
							for="courseID" />
						<h:outputLabel value="Course Name" />
						<h:inputText id="courseName" required="true"
							value="#{tutorBean.course_name}"
							requiredMessage="A course name is required" />
						<h:message showSummary="true" style="color:red" showDetail="false"
							for="courseName" />
					</h:panelGrid>
					<br>
					<h:commandButton type="submit" value="Register"
						action="#{tutorBean.newCourse}" />
				</h:form>
			</div>
		</center>

	</f:view>
</body>
</html>