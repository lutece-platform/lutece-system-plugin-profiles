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

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.profiles.business.Profile;
import fr.paris.lutece.plugins.profiles.business.ProfileHome;
import fr.paris.lutece.plugins.profiles.service.ProfilesPlugin;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.rbac.RBACHome;
import fr.paris.lutece.portal.business.rbac.RBACRole;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;

/**
 * The button of an error message of the profiles back office opens the screen the user came from when there is no page to go
 * back to
 */
public class ProfilesJspBeanTest extends LuteceTestCase
{
    private static final String PROFILE_KEY = "junit_return_url";
    private static final String ROLE_KEY = "junit_profiles_return_url";

    @Inject
    private ProfilesJspBean _bean;

    private Plugin _plugin;
    private RBAC _rbac;

    @BeforeEach
    protected void createProfile( )
    {
        _plugin = PluginService.getPlugin( ProfilesPlugin.PLUGIN_NAME );

        Profile profile = new Profile( );
        profile.setKey( PROFILE_KEY );
        profile.setDescription( "description" );
        ProfileHome.create( profile, _plugin );

        _rbac = new RBAC( );
        _rbac.setRoleKey( ROLE_KEY );
        _rbac.setResourceTypeKey( Profile.RESOURCE_TYPE );
        _rbac.setResourceId( RBAC.WILDCARD_RESOURCES_ID );
        _rbac.setPermissionKey( RBAC.WILDCARD_PERMISSIONS_KEY );
        RBACHome.create( _rbac );
    }

    @AfterEach
    protected void removeProfile( )
    {
        ProfileHome.remove( PROFILE_KEY, _plugin );
        RBACHome.remove( _rbac.getRBACId( ) );
    }

    private MockHttpServletRequest getUserRequest( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setParameter( "plugin_name", ProfilesPlugin.PLUGIN_NAME );
        AdminUser user = new AdminUser( );
        user.setRoles( Map.of( ROLE_KEY, new RBACRole( ROLE_KEY, ROLE_KEY ) ) );
        AdminUserUtils.registerAdminUserWithRight( request, user, ProfilesJspBean.RIGHT_MANAGE_PROFILES );
        _bean.init( request, ProfilesJspBean.RIGHT_MANAGE_PROFILES );

        return request;
    }

    @Test
    public void testDoCreateProfileWithoutKeyReturnsToTheCreationScreen( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = getUserRequest( );
        request.setParameter( "profile_description", "description" );

        String strUrl = _bean.doCreateProfile( request );

        assertTrue( strUrl.endsWith( AdminMessageService.getMessageRelativeUrl( ) ), strUrl );
        assertTrue( AdminMessageService.getMessage( request ).getBackUrl( ).endsWith( "jsp/admin/plugins/profiles/CreateProfile.jsp" ),
                AdminMessageService.getMessage( request ).getBackUrl( ) );
    }

    @Test
    public void testDoModifyProfileWithoutDescriptionReturnsToTheModificationScreen( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = getUserRequest( );
        request.setParameter( "profile_key", PROFILE_KEY );

        String strUrl = _bean.doModifyProfile( request );

        assertTrue( strUrl.endsWith( AdminMessageService.getMessageRelativeUrl( ) ), strUrl );
        String strBackUrl = AdminMessageService.getMessage( request ).getBackUrl( );
        assertTrue( strBackUrl.endsWith( "jsp/admin/plugins/profiles/ModifyProfile.jsp?profile_key=" + PROFILE_KEY ), strBackUrl );
    }

    @Test
    public void testDoModifyUnknownProfileReturnsToTheProfileList( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = getUserRequest( );
        request.setParameter( "profile_key", "https://example.com/" );

        _bean.doModifyProfile( request );

        // The key read in the request is not written in the URL: it is unknown
        assertTrue( AdminMessageService.getMessage( request ).getBackUrl( ).endsWith( "jsp/admin/plugins/profiles/ManageProfiles.jsp" ),
                AdminMessageService.getMessage( request ).getBackUrl( ) );
    }
}
