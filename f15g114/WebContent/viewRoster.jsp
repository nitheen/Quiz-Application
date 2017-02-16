<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Student Roster</title>
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
					<h2>View Student Roster Page</h2>
					<br>
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
		<center>
			<h:form>
				<h2>Student Roster</h2>
				<br>
				<h:messages globalOnly="true" style="color:green"></h:messages>
				<br>
			</h:form>
			<br>
			<t:dataTable value="#{professorBean.roster}" var="rowNumber"
				rendered="#{professorBean.displayRoster}" border="1" cellspacing="0"
				cellpadding="1" columnClasses="columnClass1 border"
				headerClass="headerClass" footerClass="footerClass"
				rowClasses="rowClass2" styleClass="dataTableEx" width="1000">
				<h:column>
					<f:facet name="header">
						<h:outputText>User Name</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.netid}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>First Name</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.firstName}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>Last Name</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.lastName}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>Course ID</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.courseID}" />
				</h:column>

			</t:dataTable>
		</center>
	</f:view>
</body>
</html>