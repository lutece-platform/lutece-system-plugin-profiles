<%@ page errorPage="../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.plugins.profiles.web.views.ViewsJspBean"%>

${ viewsJspBean.init( pageContext.request, ViewsJspBean.RIGHT_MANAGE_VIEWS ) }
${ pageContext.response.sendRedirect( viewsJspBean.doCreateView( pageContext.request )) }
