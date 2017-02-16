<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Grades</title>
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
						<a href="/f15g114/files/Student_User_Guide.pdf" target="_blank">Student
							User Guide</a>
						<br>
						<h:commandLink action="#{loginBean.logout}" value="Logout">
						</h:commandLink>
					</h:form>
				</center>
			</div>
			<br>
			<h:form>
				<br>
				<br>
				<h:outputLabel value="Select Assessment to View Grades:" />
				<h:selectOneMenu value="#{studentBean.assessment}">
					<f:selectItems value="#{studentBean.assessments}" />
				</h:selectOneMenu>
				<br>
				<br>
				<h:commandButton id="processGrades" type="submit"
					action="#{studentBean.processGrades}" value="Submit" />
			</h:form>
			<br>
			<h:messages globalOnly="true" style="color:green"></h:messages>
			<br>
			<h:panelGrid columns="2" border="2"
				rendered="#{studentBean.displayList}">
				<h:outputLabel value="Net ID" />
				<h:outputText id="netID" value="#{studentBean.netid}" />
				<h:outputLabel value="Assessment Number" />
				<h:outputText id="assessmentNumber"
					value="#{studentBean.assessment}" />
				<h:outputLabel value="Max Points" />
				<h:outputText id="maxPoints" value="#{studentBean.max_score}" />
				<h:outputLabel value="Scored Points" />
				<h:outputText id="scoredPoints" value="#{studentBean.actual_score}" />
				<h:outputLabel value="Course ID" />
				<h:outputText id="courseID" value="#{studentBean.courseID}" />
				<h:outputLabel value="Comments" />
				<h:outputText id="comments" value="#{studentBean.comments}" />
			</h:panelGrid>
			<br> <br>
			<t:dataTable value="#{studentBean.assessment_view}" var="rowNumber"
					rendered="#{studentBean.displayList}" border="1"
					cellspacing="0" cellpadding="1" columnClasses="columnClass1 border"
					headerClass="headerClass" footerClass="footerClass"
					rowClasses="rowClass2" styleClass="dataTableEx" width="1000">
					<h:column>
						<f:facet name="header">
							<h:outputText>Question No.</h:outputText>
						</f:facet>
						<h:outputText value="#{rowNumber.question_no}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText>Question</h:outputText>
						</f:facet>
						<h:outputText value="#{rowNumber.actual_question}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText>Answer</h:outputText>
						</f:facet>
						<h:outputText value="#{rowNumber.answer}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText>Response</h:outputText>
						</f:facet>
						<h:outputText value="#{rowNumber.response}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText>Is Response Correct?</h:outputText>
						</f:facet>
						<h:outputText value="#{rowNumber.response_correct}" />
					</h:column>
				</t:dataTable>
		</center>
	</f:view>
</body>
</html>