<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="Style.css">
<title>Descriptive Statistics</title>
</head>
<body>
	<%
		if (session.getAttribute("login") == null)
			response.sendRedirect("userLogin.jsp");
	%>
	<f:view>

		<div id="header">
			<h2>Class Statistics</h2>
			<a href="faces/instructorHome.jsp">Home</a> <br> <a
				href="/f15g114/files/Professor_Tutor_User_Guide.pdf" target="_blank">Instructor/TA
				User Guide</a><br>
			<h:form>
			<h:commandLink action="#{loginBean.logout}" value="Logout">
			</h:commandLink>
			</h:form>
		</div>

		<div id="nav">
			<h:form>
				<h:commandLink action="#{chartBean.processBarChart}"
					value="Individual Scores by Assessment">
				</h:commandLink>
				<br>
				<h:commandLink action="#{chartBean.processTimeSeriesChart}"
					value="Mean scores by Assessment">
				</h:commandLink>
				<br>
				<h:commandLink action="#{chartBean.descriptiveStatsLoad}"
					value="Descriptive Statistics">
				</h:commandLink>
			</h:form>
		</div>

		<div id="section"
			style="vertical-align: middle; position: relative; margin-left: 100px; margin-top: 35px">
			<center>
				<h:messages globalOnly="true" style="color:green"></h:messages>
				<h:form rendered="#{chartBean.displayAssessment}">
					<h:selectOneMenu value="#{chartBean.assessment}">
						<f:selectItems value="#{chartBean.assessmentList}" />
					</h:selectOneMenu>
					&nbsp; &nbsp; &nbsp; 
					<h:commandButton type="submit"
						action="#{chartBean.descriptiveStats}"
						value="Get Descriptive Statistics" />
				</h:form>
				<br>

				<t:dataTable value="#{chartBean.statistics}" var="rowNumber"
					rendered="#{chartBean.display}" border="1" cellspacing="0"
					cellpadding="1" columnClasses="columnClass1 border"
					headerClass="headerClass" footerClass="footerClass"
					rowClasses="rowClass2" styleClass="dataTableEx" width="400">
					<h:column>
						<f:facet name="header">
							<h:outputText>Statistic Parameter</h:outputText>
						</f:facet>
						<h:outputText value="#{rowNumber.statDesc}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText>Value</h:outputText>
						</f:facet>
						<h:outputText value="#{rowNumber.statValue}" />
					</h:column>
				</t:dataTable>
			</center>
		</div>

		<div id="footer"></div>

	</f:view>
</body>
</html>