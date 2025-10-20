<%@ page errorPage="../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.plugins.profiles.web.ProfilesJspBean"%>

${ profilesJspBean.init( pageContext.request, ProfilesJspBean.RIGHT_MANAGE_PROFILES ) }
${ pageContext.response.sendRedirect( profilesJspBean.doAssignWorkgroupsProfile( pageContext.request )) }