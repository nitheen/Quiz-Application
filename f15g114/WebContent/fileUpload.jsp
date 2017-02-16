<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>File Upload Page</title>
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
					<h2>Assessment Upload Page</h2>
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
			<div>
				<h:messages globalOnly="true" style="color:green"></h:messages>

				<br> <br>
				<h:form enctype="multipart/form-data">


					<h:panelGrid columns="3">
						<h:outputLabel value="Select file to upload:" />
						<t:inputFileUpload id="fileUpload" label="File to upload"
							required="true" storage="default"
							value="#{professorBean.uploadedFile}" size="60"
							requiredMessage="Please Select a file" />
						<h:message showSummary="true" style="color:red" showDetail="false"
							for="fileUpload" />
						<h:outputLabel value="File Label:" />
						<h:inputText id="fileLabel" value="#{professorBean.fileLabel}"
							required="true" size="60"
							requiredMessage="Please enter a file label" />
						<h:message showSummary="true" style="color:red" showDetail="false"
							for="fileLabel" />
						<h:commandButton id="upload" type="submit"
							action="#{professorBean.processFileUpload}" value="Submit" />
					</h:panelGrid>
				</h:form>
			</div>
		</center>
	</f:view>
</body>
</html>