/*
 * Copyright (c) 2002-2026, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.profiles.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.profiles.business.Profile;
import fr.paris.lutece.plugins.profiles.business.views.View;
import fr.paris.lutece.plugins.profiles.service.ProfilesPlugin;
import fr.paris.lutece.plugins.profiles.web.views.ViewsJspBean;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.rbac.RBACHome;
import fr.paris.lutece.portal.business.rbac.RBACRole;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;

/**
 * Every back office action of the plugin requires a security token (CSRF), and no template reaches an action by a link.
 */
public class ProfilesSecurityTokenTest extends LuteceTestCase
{
    private static final String ROLE_KEY = "junit_profiles_token";
    private static final String TEMPLATES = "webapp/WEB-INF/templates/admin/plugins/profiles";
    private static final Pattern FORM_TO_ACTION = Pattern.compile( "(?s)<@tform[^>]*action='jsp/admin/plugins/profiles/Do\\w+\\.jsp'[^>]*>(.*?)</@tform>" );
    private static final Pattern LINK_TO_ACTION = Pattern.compile( "href=['\"][^'\"]*/Do\\w+\\.jsp" );

    @Inject
    private ProfilesJspBean _profilesBean;

    @Inject
    private ViewsJspBean _viewsBean;

    @Test
    public void testEveryActionRefusesARequestWithoutToken( ) throws Exception
    {
        List<String> listAccepted = new ArrayList<>( );
        int nActions = 0;
        RBAC rbacProfiles = rbac( Profile.RESOURCE_TYPE );
        RBAC rbacViews = rbac( View.RESOURCE_TYPE );

        try
        {
            for ( Object bean : new Object [ ] {
                    _profilesBean, _viewsBean
            } )
            {
                for ( Method method : actions( bean ) )
                {
                    nActions++;
                    MockHttpServletRequest request = getUserRequest( bean );
                    try
                    {
                        method.invoke( bean, request );
                        listAccepted.add( method.getName( ) );
                    }
                    catch( InvocationTargetException e )
                    {
                        if ( !( e.getCause( ) instanceof AccessDeniedException ) )
                        {
                            listAccepted.add( method.getName( ) + " (" + e.getCause( ) + ")" );
                        }
                    }
                }
            }
        }
        finally
        {
            RBACHome.remove( rbacProfiles.getRBACId( ) );
            RBACHome.remove( rbacViews.getRBACId( ) );
        }

        assertTrue( nActions >= 19, "the actions of the plugin should be found: " + nActions );
        assertTrue( listAccepted.isEmpty( ), "actions run without a security token: " + listAccepted );
    }

    @Test
    public void testEveryActionFormCarriesTheTokenAndNoLinkReachesAnAction( ) throws IOException
    {
        List<String> listProblems = new ArrayList<>( );
        try ( Stream<Path> paths = Files.walk( Paths.get( TEMPLATES ) ) )
        {
            for ( Path path : paths.filter( p -> p.toString( ).endsWith( ".html" ) ).toList( ) )
            {
                String strTemplate = Files.readString( path, StandardCharsets.UTF_8 );
                if ( LINK_TO_ACTION.matcher( strTemplate ).find( ) )
                {
                    listProblems.add( path.getFileName( ) + ": link to an action" );
                }
                Matcher matcher = FORM_TO_ACTION.matcher( strTemplate );
                while ( matcher.find( ) )
                {
                    if ( !matcher.group( 1 ).contains( "name='token'" ) )
                    {
                        listProblems.add( path.getFileName( ) + ": form without token at " + matcher.start( ) );
                    }
                }
            }
        }

        assertTrue( listProblems.isEmpty( ), listProblems.toString( ) );
    }

    private static List<Method> actions( Object bean )
    {
        List<Method> listActions = new ArrayList<>( );
        for ( Method method : bean.getClass( ).getMethods( ) )
        {
            if ( method.getName( ).startsWith( "do" ) && method.getDeclaringClass( ).getName( ).startsWith( "fr.paris.lutece.plugins.profiles" )
                    && !Modifier.isStatic( method.getModifiers( ) ) && method.getParameterCount( ) == 1
                    && method.getParameterTypes( ) [0].getName( ).equals( "jakarta.servlet.http.HttpServletRequest" ) )
            {
                listActions.add( method );
            }
        }

        return listActions;
    }

    private static RBAC rbac( String strResourceType )
    {
        RBAC rbac = new RBAC( );
        rbac.setRoleKey( ROLE_KEY );
        rbac.setResourceTypeKey( strResourceType );
        rbac.setResourceId( RBAC.WILDCARD_RESOURCES_ID );
        rbac.setPermissionKey( RBAC.WILDCARD_PERMISSIONS_KEY );
        RBACHome.create( rbac );

        return rbac;
    }

    private MockHttpServletRequest getUserRequest( Object bean ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setParameter( "plugin_name", ProfilesPlugin.PLUGIN_NAME );
        // parameters of a forged request: an unknown profile / view, so that an action accepted without token changes nothing
        for ( String strParameter : new String [ ] {
                "profile_key", "view_key", "id_right", "role_key", "workgroup_key", "dashboard_name", "column"
        } )
        {
            request.setParameter( strParameter, "junit_forged" );
        }
        request.setParameter( "id_user", "999999" );
        request.setParameter( "dashboard_column", "1" );
        request.setParameter( "dashboard_order", "1" );
        AdminUser user = new AdminUser( );
        user.setRoles( Map.of( ROLE_KEY, new RBACRole( ROLE_KEY, ROLE_KEY ) ) );
        if ( bean == _profilesBean )
        {
            AdminUserUtils.registerAdminUserWithRight( request, user, ProfilesJspBean.RIGHT_MANAGE_PROFILES );
            _profilesBean.init( request, ProfilesJspBean.RIGHT_MANAGE_PROFILES );
        }
        else
        {
            AdminUserUtils.registerAdminUserWithRight( request, user, ViewsJspBean.RIGHT_MANAGE_VIEWS );
            _viewsBean.init( request, ViewsJspBean.RIGHT_MANAGE_VIEWS );
        }

        return request;
    }
}
