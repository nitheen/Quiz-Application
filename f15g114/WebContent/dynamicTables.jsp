<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Dynamic Tables</title>
</head>
<body>
	<f:view>
		<div
			style="background-color: #bfbfbf; border-bottom-style: solid; border-top-style: solid; border-left-style: solid; border-right-style: solid">
			<center>
				<f:verbatim>
					<h2>Display Tables</h2>
				</f:verbatim>
				<a href="/f15g114/files/Professor_Tutor_User_Guide.pdf"
					target="_blank">Instructor/TA User Guide</a>
				<h:form>
					<h:commandLink action="#{tutorBean.clearDynamicList}" value="Back">
					</h:commandLink>
				</h:form>
			</center>
		</div>
		<center>
			<h:messages globalOnly="true" style="color:red"></h:messages>
			<h4>
				Table -
				<h:outputText value="#{tutorBean.tableName}"></h:outputText>
			</h4>
			<h:panelGroup binding="#{tutorBean.dynamicDataTableGroup}" />
		</center>
	</f:view>
</body>
</html>