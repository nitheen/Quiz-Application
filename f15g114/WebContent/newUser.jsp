<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Registration</title>
</head>
<body style="background-color: white">
	<f:view>
		<div
			style="background-color: #bfbfbf; border-bottom-style: solid; border-top-style: solid; border-left-style: solid; border-right-style: solid">
			<f:verbatim>
				<center>
					<h2>User Registration Page</h2>
					<a href="faces/userLogin.jsp">Login Page</a> <br> <a
						href="/f15g114/files/Professor_Tutor_User_Guide.pdf"
						target="_blank">Instructor/TA User Guide</a> <br>
				</center>
			</f:verbatim>
		</div>
		<center>
			<div>
				<br>
				<h:messages globalOnly="true" style="color:green" rendered="true"></h:messages>
				<br>
				<h:form>
					<h:panelGrid columns="3">
						<h:outputLabel value="First Name" />
						<h:inputText id="firstName" required="true"
							value="#{tutorBean.firstName}"
							requiredMessage="A first name is required" />
						<h:message showSummary="true" style="color:red" showDetail="false"
							for="firstName" />
						<h:outputLabel value="Last Name" />
						<h:inputText id="lastName" required="true"
							value="#{tutorBean.lastName}"
							requiredMessage="A last name is required" />
						<h:message showSummary="true" style="color:red" showDetail="false"
							for="lastName" />
						<h:outputLabel value="User Name" />
						<h:inputText id="netId" required="true" value="#{tutorBean.netId}"
							requiredMessage="A net id is required" />
						<h:message showSummary="true" style="color:red" showDetail="false"
							for="netId" />
						<h:outputLabel value="Password" />
						<h:inputSecret id="password" required="true" rendered="true"
							value="#{tutorBean.password}"
							requiredMessage="A password is required" />
						<h:message showSummary="true" style="color:red" showDetail="false"
							for="password" />
						<h:outputLabel value="Role" />
						<h:selectOneMenu value="#{tutorBean.role}">
							<f:selectItem itemValue="tutor" itemLabel="Tutor" />
							<f:selectItem itemValue="professor" itemLabel="Professor" />
						</h:selectOneMenu>
						<br />
						<h:outputLabel value="Select Course" />
						<h:selectOneMenu value="#{tutorBean.course}">
							<f:selectItems value="#{tutorBean.courseName}" />
						</h:selectOneMenu>
					</h:panelGrid>
					<h:commandButton type="submit" value="Register User"
						action="#{tutorBean.registerUser}" />
				</h:form>
			</div>
		</center>
	</f:view>
</body>
</html>