<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create and Drop tables</title>
</head>
<body style="background-color: white">
	<f:view>
		<div
			style="background-color: #bfbfbf; border-bottom-style: solid; border-top-style: solid; border-left-style: solid; border-right-style: solid">
			<center>
				<f:verbatim>
					<h2>Create and Drop Tables</h2>
					<br>
				</f:verbatim>
				<a href="faces/userLogin.jsp">Back to Login Page</a> <br> <a
					href="/f15g114/files/Professor_Tutor_User_Guide.pdf"
					target="_blank">Instructor/TA User Guide</a> <br>
			</center>
		</div>
		<center>
			<br> <br>
			<h:messages globalOnly="true" style="color:green"></h:messages>
			<br>
			<h:form>
				<h:commandLink action="#{tutorBean.createTables}"
					value="Create Tables">
				</h:commandLink>
				<br>
				<h:commandLink value="View Existing Tables"
					action="#{tutorBean.viewTables}" />
				<br>

				<h:commandLink value="Drop All Tables"
					rendered="#{tutorBean.displayTables}"
					action="#{tutorBean.dropAllTables}" />
			</h:form>
			<br> <br>

			<h:form>
				<div style="max-height: 50em;">
					<h:selectOneListbox id="tableName" value="#{tutorBean.tableName}"
						rendered="#{tutorBean.displayTables}" style="">
						<f:selectItems value="#{tutorBean.tables}" />
					</h:selectOneListbox>
				</div>
				<br>
				<br>
				<h:commandButton type="submit" rendered="#{tutorBean.displayTables}"
					action="#{tutorBean.dropTables}" value="Drop Table" />
				<h:commandButton type="submit" rendered="#{tutorBean.displayTables}"
					action="#{tutorBean.displayTable}" value="Display Table" />
			</h:form>
		</center>

	</f:view>
</body>
</html>