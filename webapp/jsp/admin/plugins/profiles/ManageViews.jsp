<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.plugins.profiles.web.views.ViewsJspBean"%>

${ viewsJspBean.init( pageContext.request, ViewsJspBean.RIGHT_MANAGE_VIEWS ) }
${ viewsJspBean.getManageViews( pageContext.request ) }

<%@ include file="../../AdminFooter.jsp" %>