<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="profile" scope="session" class="fr.paris.lutece.plugins.profiles.web.ProfilesJspBean" />

<% profile.init( request, fr.paris.lutece.plugins.profiles.web.ProfilesJspBean.RIGHT_MANAGE_PROFILES ) ; %>
<%= profile.getAssignUsersProfile( request ) %>

<%@ include file="../../AdminFooter.jsp" %>