<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Page</title>
</head>
<body style="background-color: white">
	<f:view>
		<center>
			<div
				style="background-color: #bfbfbf; border-bottom-style: solid; border-top-style: solid; border-left-style: solid; border-right-style: solid">
				<f:verbatim>
					<center>
						<h2>User Login Page</h2>
						<br> <a href="/f15g114/files/Student_User_Guide.pdf"
							target="_blank">Student User Guide</a> <br> <a
							href="/f15g114/files/Professor_Tutor_User_Guide.pdf"
							target="_blank">Instructor/TA User Guide</a> <br>
					</center>
				</f:verbatim>
			</div>
			<div>
				<br>
				<h:messages globalOnly="true" style="color:red"></h:messages>
				<br>
				<h:form>
					<h:panelGrid columns="3">
						<h:outputLabel value="User Name" />
						<h:inputText id="userName" required="true"
							value="#{loginBean.userName}"
							requiredMessage="A user name is required" />
						<h:message showSummary="true" style="color:red" showDetail="false"
							for="userName" />
						<h:outputLabel value="Password" />
						<h:inputSecret id="password" required="true"
							value="#{loginBean.password}"
							requiredMessage="A password is required" />
						<h:message showSummary="true" style="color:red" showDetail="false"
							for="password" />
					</h:panelGrid>
					<br>
					<h:commandButton type="submit" value="Login"
						action="#{loginBean.validate}" />
				</h:form>
				<br> <br>
				<h:form>
					<h:commandLink action="#{tutorBean.createTableLoad}"
						value="Create and Drop Application Tables">
					</h:commandLink>
					<br>
					<h:commandLink action="#{tutorBean.loadRegister}"
						value="New User? Register Here">
					</h:commandLink>
					<br>
					<a href="faces/newCourse.jsp">Create a new course</a>
				</h:form>
			</div>
		</center>
	</f:view>
</body>
</html>