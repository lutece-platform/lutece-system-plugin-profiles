<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.plugins.profiles.web.ProfilesJspBean"%>

${ profilesJspBean.init( pageContext.request, ProfilesJspBean.RIGHT_MANAGE_PROFILES ) }
${ profilesJspBean.getCreateProfile( pageContext.request ) }

<%@ include file="../../AdminFooter.jsp" %>