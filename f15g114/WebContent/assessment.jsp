<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Assessment Page</title>
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
						<h2>Assessments Page</h2>
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
			<h:messages globalOnly="true" style="color:green"></h:messages>
			<br> <br>
			<h:form rendered="#{studentBean.displayList}">
				<h:selectOneMenu value="#{studentBean.assessment}">
					<f:selectItems value="#{studentBean.assessments}" />
				</h:selectOneMenu>
				<br>
				<br>
				<h:commandButton type="submit"
					action="#{studentBean.processAssessments}" value="Start Assessment" />
			</h:form>
			<br>
			<div>
				<h:form rendered="#{studentBean.displayQuiz}">
					<t:dataTable value="#{studentBean.questions}" var="rowNumber"
						rendered="#{studentBean.displayQuiz}" border="1" cellspacing="0"
						cellpadding="1" columnClasses="columnClass1 border"
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
							<h:inputText value="#{studentBean.answer}" />
						</h:column>
					</t:dataTable>

					<h:commandButton id="submitAssessment" type="submit"
						action="#{studentBean.evaluateAssessment}"
						value="Submit Assessment" />

				</h:form>
			</div>
		</center>
	</f:view>
</body>
</html>