<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="view" scope="session" class="fr.paris.lutece.plugins.profiles.web.views.ViewsJspBean" />


<%
	view.init( request, fr.paris.lutece.plugins.profiles.web.views.ViewsJspBean.RIGHT_MANAGE_VIEWS );
 %>
<%= view.getManageViews( request ) %>

<%@ include file="../../AdminFooter.jsp" %>