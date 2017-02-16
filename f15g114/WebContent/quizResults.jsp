<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Quiz Results</title>
</head>
<body style="background-color: white">
	<%
		if (session.getAttribute("login") == null)
			response.sendRedirect("userLogin.jsp");
	%>
	<f:view>
		<center>
			<div
				style="background-color: #bfbfbf; border-bottom-style: solid; border-top-style: solid; border-left-style: solid; border-right-style: solid">
				<center>
					<f:verbatim>
						<h2>Quiz Results Page</h2>
					</f:verbatim>
					<h:form>
						<a href="faces/studentHome.jsp">Home</a>
						<br>
						<a href="/f15g114/files/Professor_Tutor_User_Guide.pdf"
							target="_blank">Instructor/TA User Guide</a>
						<br>
						<h:commandLink action="#{loginBean.logout}" value="Logout">
						</h:commandLink>
					</h:form>
				</center>
			</div>
			<br>
			<h:messages globalOnly="true" style="color:green"></h:messages>
			<br> <br>
			<h:panelGrid columns="2" border="2">
				<h:outputLabel value="Net ID" />
				<h:outputText id="netID" value="#{studentBean.netid}" />
				<h:outputLabel value="Assessment Number" />
				<h:outputText id="assessmentNumber"
					value="#{studentBean.assessment}" />
				<h:outputLabel value="Max Points" />
				<h:outputText id="maxPoints" value="#{studentBean.max_score}" />
				<h:outputLabel value="Scored Points" />
				<h:outputText id="scoredPoints" value="#{studentBean.actual_score}" />
			</h:panelGrid>

		</center>
	</f:view>
</body>
</html>