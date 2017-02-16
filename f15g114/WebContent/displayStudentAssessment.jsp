<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Assessment Display</title>
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
					<h2>Display Assessments Roster</h2>
				</f:verbatim>
				<a href="faces/instructorHome.jsp">Home</a>
				<h:form>
					<h:commandLink action="#{professorBean.displayRosterReset}"
						value="Back">
					</h:commandLink>
					<br>
					<a href="/f15g114/files/Professor_Tutor_User_Guide.pdf"
						target="_blank">Instructor/TA User Guide</a>
					<br>
					<h:commandLink action="#{loginBean.logout}" value="Logout">
					</h:commandLink>

				</h:form>
			</center>
		</div>
		<center>
			<div>
				<h:messages globalOnly="true" style="color:green"></h:messages>
				<br> <br>
				<h:form>
					<t:dataTable value="#{professorBean.roster}" var="rowNumber"
						rendered="#{professorBean.displayStudent}" border="1"
						cellspacing="0" cellpadding="1"
						columnClasses="columnClass1 border" headerClass="headerClass"
						footerClass="footerClass" rowClasses="rowClass2"
						styleClass="dataTableEx" width="1000">
						<h:column>
							<f:facet name="header">
								<h:outputText>Net ID</h:outputText>
							</f:facet>
							<h:outputText value="#{rowNumber.netid}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText>Assessment no.</h:outputText>
							</f:facet>
							<h:outputText value="#{rowNumber.assessment}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText>Total Points</h:outputText>
							</f:facet>
							<h:outputText value="#{rowNumber.max_score}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText>Scored Points</h:outputText>
							</f:facet>
							<h:outputText value="#{rowNumber.actual_score}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText>Comments</h:outputText>
							</f:facet>
							<h:inputText value="#{professorBean.comments}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText></h:outputText>
							</f:facet>
							<h:commandLink action="#{professorBean.saveComments}"
								value="Save Comments">
							</h:commandLink>
						</h:column>
					</t:dataTable>
					<br>
					<h:commandLink action="#{professorBean.processFileDownload}"
						value="Export Data as CSV">
					</h:commandLink>
				</h:form>
				<br> <br>
				<t:dataTable value="#{professorBean.assessment}" var="rowNumber"
					rendered="#{professorBean.displayStudent}" border="1"
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
			</div>
		</center>
	</f:view>
</body>
</html>