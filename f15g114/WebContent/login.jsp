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
				<h1>Group - F15G114</h1>
				<h2>Database Login Page</h2>
			</div>
			<div>
				<br>
				<h:messages globalOnly="true" style="color:red"></h:messages>

				<h:form>
					<h:panelGrid columns="3">
						<h:outputLabel value="User Name" />
						<h:inputText id="userName" required="true"
							value="#{dataValuesBean.userName}"
							requiredMessage="A user name is required" />
						<h:message showSummary="true" style="color:red" showDetail="false"
							for="userName" />
						<h:outputLabel value="Password" />
						<h:inputSecret id="password" redisplay="true" required="true"
							value="#{dataValuesBean.password}"
							requiredMessage="A password is required" />
						<h:message showSummary="true" style="color:red" showDetail="false"
							for="password" />
						<h:outputLabel value="Host" />
						<h:inputText id="dbmsHost" required="true"
							value="#{dataValuesBean.dbmsHost}"
							requiredMessage="A dbms host is required" />
						<h:message showSummary="true" style="color:red" showDetail="false"
							for="dbmsHost" />
						<h:outputLabel value="RDBMS" />
						<h:selectOneMenu value="#{dataValuesBean.dbms}">
							<f:selectItem itemValue="mysql" itemLabel="MySQL" />
							<f:selectItem itemValue="db2" itemLabel="DB2" />
							<f:selectItem itemValue="oracle:thin" itemLabel="Oracle" />
						</h:selectOneMenu>
						<br />
						<h:outputLabel value="Schema" />
						<h:inputText id="schema" required="true"
							value="#{dataValuesBean.schema}"
							requiredMessage="A schema name is required" />
						<h:message showSummary="true" style="color:red" showDetail="false"
							for="schema" />
					</h:panelGrid>
					<h:commandButton type="submit" value="Login"
						action="#{dataValuesBean.checkDbValues}" />
					<br>
					<br>
				</h:form>
				<br> <a
					href="/f15g114/files/Design_Document.pdf"
					target="_blank">Technical Documentation</a> <br>
					<a
					href="/f15g114/files/Student_User_Guide.pdf"
					target="_blank">Student User Guide</a> <br>
					<a
					href="/f15g114/files/Professor_Tutor_User_Guide.pdf"
					target="_blank">Instructor/TA User Guide</a> <br>
			</div>
		</center>
	</f:view>
</body>
</html>