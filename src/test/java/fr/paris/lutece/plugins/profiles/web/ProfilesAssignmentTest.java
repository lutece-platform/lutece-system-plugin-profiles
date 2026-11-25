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

import jakarta.enterprise.inject.spi.CDI;
import fr.paris.lutece.plugins.profiles.utils.constants.ProfilesConstants;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.profiles.business.Profile;
import fr.paris.lutece.plugins.profiles.service.IProfilesService;
import fr.paris.lutece.plugins.profiles.service.ProfilesPlugin;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.rbac.RBACHome;
import fr.paris.lutece.portal.business.rbac.RBACRole;
import fr.paris.lutece.portal.business.rbac.RBACRoleHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroup;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.util.sql.DAOUtil;
import jakarta.inject.Inject;

/**
 * Assignment of rights, roles, workgroups and users to profiles.
 */
public class ProfilesAssignmentTest extends LuteceTestCase
{
    private static final String PROFILE_A = "junit_assign_a";
    private static final String PROFILE_B = "junit_assign_b";
    private static final String ROLE_KEY = "junit_profiles_assignment";
    private static final String RIGHT = "CORE_USERS_MANAGEMENT";
    private static final String WORKGROUP = "junitprofileswg";
    private static final String RBAC_ROLE = "junit_profiles_rbac_role";
    private static final String ACCESS_CODE = "junit_profiles_user";

    @Inject
    private ProfilesJspBean _bean;

    @Inject
    private IProfilesService _profilesService;

    private Plugin _plugin;
    private RBAC _rbac;
    private AdminUser _user;

    @BeforeEach
    protected void createData( )
    {
        _plugin = PluginService.getPlugin( ProfilesPlugin.PLUGIN_NAME );
        for ( String strKey : new String [ ] {
                PROFILE_A, PROFILE_B
        } )
        {
            Profile profile = new Profile( );
            profile.setKey( strKey );
            profile.setDescription( strKey );
            _profilesService.create( profile, Locale.FRENCH, _plugin );
        }

        _rbac = new RBAC( );
        _rbac.setRoleKey( ROLE_KEY );
        _rbac.setResourceTypeKey( Profile.RESOURCE_TYPE );
        _rbac.setResourceId( RBAC.WILDCARD_RESOURCES_ID );
        _rbac.setPermissionKey( RBAC.WILDCARD_PERMISSIONS_KEY );
        RBACHome.create( _rbac );

        AdminWorkgroup workgroup = new AdminWorkgroup( );
        workgroup.setKey( WORKGROUP );
        workgroup.setDescription( WORKGROUP );
        AdminWorkgroupHome.create( workgroup );
        RBACRoleHome.create( new RBACRole( RBAC_ROLE, RBAC_ROLE ) );

        execute( "INSERT INTO core_admin_user ( access_code, last_name, first_name, email, status, locale, level_user ) VALUES ( '" + ACCESS_CODE
                + "', 'junit', 'junit', 'junit@example.org', 0, 'fr', 3 )" );
        _user = AdminUserHome.findUserByLogin( ACCESS_CODE );
    }

    @AfterEach
    protected void removeData( )
    {
        _profilesService.removeProfilesFromUser( _user.getUserId( ), _plugin );
        AdminUserHome.removeAllRightsForUser( _user.getUserId( ) );
        AdminUserHome.removeAllRolesForUser( _user.getUserId( ) );
        AdminWorkgroupHome.removeUserFromWorkgroup( _user, WORKGROUP );
        execute( "DELETE FROM core_admin_user_field WHERE id_user = " + _user.getUserId( ) );
        execute( "DELETE FROM core_admin_user WHERE id_user = " + _user.getUserId( ) );
        for ( String strKey : new String [ ] {
                PROFILE_A, PROFILE_B
        } )
        {
            _profilesService.removeRights( strKey, _plugin );
            _profilesService.removeRoles( strKey, _plugin );
            _profilesService.removeWorkgroups( strKey, _plugin );
            _profilesService.removeUsers( strKey, _plugin );
            _profilesService.remove( strKey, Locale.FRENCH, _plugin );
        }
        for ( int i = 0; i < 12; i++ )
        {
            if ( _profilesService.checkExistProfile( "junit_page_" + i, _plugin ) )
            {
                _profilesService.remove( "junit_page_" + i, Locale.FRENCH, _plugin );
            }
        }
        AdminWorkgroupHome.remove( WORKGROUP );
        RBACRoleHome.remove( RBAC_ROLE );
        RBACHome.remove( _rbac.getRBACId( ) );
    }

    @Test
    public void testAssigningARightTheProfileAlreadyHasIsIgnored( ) throws AccessDeniedException
    {
        _bean.doAssignRightsProfile( assignRequest( "rights_list", RIGHT ) );
        assertTrue( _profilesService.hasRight( PROFILE_A, RIGHT, _plugin ), "the profile should have the right" );

        // second submission of the same form (stale list, back button): no duplicate key
        _bean.doAssignRightsProfile( assignRequest( "rights_list", RIGHT ) );
        assertEquals( 1, _profilesService.getRightsListForProfile( PROFILE_A, _plugin ).size( ) );
        assertFalse( _profilesService.hasRight( PROFILE_B, RIGHT, _plugin ), "another profile should not have the right" );
    }

    @Test
    public void testUnassigningARightKeepsItForTheUsersOfAnotherProfileGrantingIt( ) throws AccessDeniedException
    {
        _profilesService.addRightForProfile( PROFILE_A, RIGHT, _plugin );
        _profilesService.addRightForProfile( PROFILE_B, RIGHT, _plugin );
        _profilesService.addUserForProfile( PROFILE_A, _user.getUserId( ), _plugin );
        _profilesService.addUserForProfile( PROFILE_B, _user.getUserId( ), _plugin );
        AdminUserHome.createRightForUser( _user.getUserId( ), RIGHT );

        MockHttpServletRequest request = getUserRequest( );
        request.setParameter( "profile_key", PROFILE_A );
        request.setParameter( "id_right", RIGHT );
        _bean.doUnassignRightProfile( request );

        assertTrue( AdminUserHome.hasRight( _user, RIGHT ), "the right is still granted by the other profile of the user" );

        request = getUserRequest( );
        request.setParameter( "profile_key", PROFILE_B );
        request.setParameter( "id_right", RIGHT );
        _bean.doUnassignRightProfile( request );

        assertFalse( AdminUserHome.hasRight( _user, RIGHT ), "no profile of the user grants the right any more" );
    }

    @Test
    public void testUnassigningAWorkgroupKeepsItForTheUsersOfAnotherProfileGrantingIt( ) throws AccessDeniedException
    {
        _profilesService.addWorkgroupForProfile( PROFILE_A, WORKGROUP, _plugin );
        _profilesService.addWorkgroupForProfile( PROFILE_B, WORKGROUP, _plugin );
        _profilesService.addUserForProfile( PROFILE_A, _user.getUserId( ), _plugin );
        _profilesService.addUserForProfile( PROFILE_B, _user.getUserId( ), _plugin );
        AdminWorkgroupHome.addUserForWorkgroup( _user, WORKGROUP );

        MockHttpServletRequest request = getUserRequest( );
        request.setParameter( "profile_key", PROFILE_A );
        request.setParameter( "workgroup_key", WORKGROUP );
        _bean.doUnassignWorkgroupProfile( request );

        assertTrue( AdminWorkgroupHome.isUserInWorkgroup( _user, WORKGROUP ), "the workgroup is still granted by the other profile of the user" );

        request = getUserRequest( );
        request.setParameter( "profile_key", PROFILE_B );
        request.setParameter( "workgroup_key", WORKGROUP );
        _bean.doUnassignWorkgroupProfile( request );

        assertFalse( AdminWorkgroupHome.isUserInWorkgroup( _user, WORKGROUP ), "no profile of the user grants the workgroup any more" );
    }

    @Test
    public void testUnassigningARoleKeepsItForTheUsersOfAnotherProfileGrantingIt( ) throws AccessDeniedException
    {
        _profilesService.addRoleForProfile( PROFILE_A, RBAC_ROLE, _plugin );
        _profilesService.addRoleForProfile( PROFILE_B, RBAC_ROLE, _plugin );
        _profilesService.addUserForProfile( PROFILE_A, _user.getUserId( ), _plugin );
        _profilesService.addUserForProfile( PROFILE_B, _user.getUserId( ), _plugin );
        AdminUserHome.createRoleForUser( _user.getUserId( ), RBAC_ROLE );

        MockHttpServletRequest request = getUserRequest( );
        request.setParameter( "profile_key", PROFILE_A );
        request.setParameter( "role_key", RBAC_ROLE );
        _bean.doUnassignRoleProfile( request );

        assertTrue( AdminUserHome.hasRole( _user, RBAC_ROLE ), "the role is still granted by the other profile of the user" );

        request = getUserRequest( );
        request.setParameter( "profile_key", PROFILE_B );
        request.setParameter( "role_key", RBAC_ROLE );
        _bean.doUnassignRoleProfile( request );

        assertFalse( AdminUserHome.hasRole( _user, RBAC_ROLE ), "no profile of the user grants the role any more" );
    }

    @Test
    public void testUnassigningAUserFromAProfileKeepsTheFieldsOfTheOtherProfiles( ) throws AccessDeniedException
    {
        int nIdAttribute = queryInt( "SELECT id_attribute FROM core_attribute WHERE plugin_name = 'profiles'" );
        for ( String strKey : new String [ ] {
                PROFILE_A, PROFILE_B
        } )
        {
            _profilesService.addUserForProfile( strKey, _user.getUserId( ), _plugin );
            int nIdField = queryInt( "SELECT id_field FROM core_attribute_field WHERE id_attribute = " + nIdAttribute + " AND title = '" + strKey + "'" );
            execute( "INSERT INTO core_admin_user_field ( id_user, id_attribute, id_field, user_field_value ) VALUES ( " + _user.getUserId( ) + ", "
                    + nIdAttribute + ", " + nIdField + ", '" + strKey + "' )" );
        }

        MockHttpServletRequest request = getUserRequest( );
        _profilesService.doUnassignUserFromProfile( _user.getUserId( ), PROFILE_A, AdminUserHome.findByPrimaryKey( 1 ), request, Locale.FRENCH, _plugin );

        assertFalse( _profilesService.hasUser( PROFILE_A, _user.getUserId( ), _plugin ) );
        assertTrue( _profilesService.hasUser( PROFILE_B, _user.getUserId( ), _plugin ) );
        assertEquals( 0, queryInt( "SELECT COUNT(*) FROM core_admin_user_field WHERE id_user = " + _user.getUserId( ) + " AND user_field_value = '" + PROFILE_A + "'" ),
                "the field of the profile left should be removed" );
        assertEquals( 1, queryInt( "SELECT COUNT(*) FROM core_admin_user_field WHERE id_user = " + _user.getUserId( ) + " AND user_field_value = '" + PROFILE_B + "'" ),
                "the field of the other profile should be kept, else the next save of the user drops that profile" );
    }

    @Test
    public void testProfileListPaginatorShownOnEveryPage( ) throws AccessDeniedException
    {
        for ( int i = 0; i < 12; i++ )
        {
            Profile profile = new Profile( );
            profile.setKey( "junit_page_" + i );
            profile.setDescription( "junit_page_" + i );
            _profilesService.create( profile, Locale.FRENCH, _plugin );
        }

        MockHttpServletRequest request = getUserRequest( );
        request.setParameter( "items_per_page", "10" );
        String strHtml = _bean.getManageProfiles( request );

        assertTrue( strHtml.contains( "page_index=2" ), "the second page should be reachable from the first one" );

        request = getUserRequest( );
        request.setParameter( "items_per_page", "10" );
        request.setParameter( "page_index", "2" );
        strHtml = _bean.getManageProfiles( request );

        assertTrue( strHtml.contains( "page_index=1" ), "the first page should be reachable from the second one" );
    }

    private MockHttpServletRequest assignRequest( String strListParameter, String strValue ) throws AccessDeniedException
    {
        MockHttpServletRequest request = getUserRequest( );
        request.setParameter( "profile_key", PROFILE_A );
        request.setParameter( strListParameter, strValue );

        return request;
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
        // the token the forms and confirmations of the plugin carry
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                CDI.current( ).select( ISecurityTokenService.class ).get( ).getToken( request, ProfilesConstants.TOKEN_ACTION_ADMIN ) );

        return request;
    }

    private static void execute( String strSql )
    {
        try ( DAOUtil daoUtil = new DAOUtil( strSql ) )
        {
            daoUtil.executeUpdate( );
        }
    }

    private static int queryInt( String strSql )
    {
        try ( DAOUtil daoUtil = new DAOUtil( strSql ) )
        {
            daoUtil.executeQuery( );

            return daoUtil.next( ) ? daoUtil.getInt( 1 ) : -1;
        }
    }
}
