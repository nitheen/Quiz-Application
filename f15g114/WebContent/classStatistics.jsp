<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="Style.css">
<title>Class Statistics</title>
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
			<h:messages globalOnly="true" style="color:green"></h:messages>
			<!-- 			<img alt="F15G114" src="files/barChart.png"> -->
			<h:graphicImage value="files/barChart.png"
				rendered="#{chartBean.display}" />
		</div>

		<div id="footer"></div>

	</f:view>
</body>
</html>