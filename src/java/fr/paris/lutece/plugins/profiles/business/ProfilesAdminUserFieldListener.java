/*
 * Copyright (c) 2002-2020, City of Paris
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
package fr.paris.lutece.plugins.profiles.business;

import fr.paris.lutece.plugins.profiles.service.ProfilesPlugin;
import fr.paris.lutece.portal.business.rbac.RBACRole;
import fr.paris.lutece.portal.business.rbac.RBACRoleHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.attribute.AdminUserField;
import fr.paris.lutece.portal.business.user.attribute.SimpleAdminUserFieldListener;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroup;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

import java.util.List;
import java.util.Locale;

/**
 *
 * ProfilesAdminUserFieldListener
 *
 */
public class ProfilesAdminUserFieldListener extends SimpleAdminUserFieldListener
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void doCreateUserFields( AdminUser user, List<AdminUserField> listUserFields, Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( ProfilesPlugin.PLUGIN_NAME );
        // For each selected profiles
        for ( AdminUserField userField : listUserFields )
        {
            String strProfileKey = userField.getValue( );
            int nIdUser = user.getUserId( );

            if ( ProfileHome.hasUser( strProfileKey, nIdUser, plugin ) )
            {
                continue;
            }
            ProfileHome.addUserForProfile( strProfileKey, nIdUser, plugin );

            // Add rights to the user
            for ( Right right : ProfileHome.getRightsListForProfile( strProfileKey, plugin ) )
            {
                right = RightHome.findByPrimaryKey( right.getId( ) );

                if ( !AdminUserHome.hasRight( user, right.getId( ) ) && ( user.getUserLevel( ) <= right.getLevel( ) ) )
                {
                    AdminUserHome.createRightForUser( nIdUser, right.getId( ) );
                }
            }

            // Add roles to the user
            for ( RBACRole role : ProfileHome.getRolesListForProfile( strProfileKey, plugin ) )
            {
                role = RBACRoleHome.findByPrimaryKey( role.getKey( ) );

                if ( !AdminUserHome.hasRole( user, role.getKey( ) ) )
                {
                    AdminUserHome.createRoleForUser( nIdUser, role.getKey( ) );
                }
            }

            // Add workgroups to the user
            for ( AdminWorkgroup workgroup : ProfileHome.getWorkgroupsListForProfile( strProfileKey, plugin ) )
            {
                workgroup = AdminWorkgroupHome.findByPrimaryKey( workgroup.getKey( ) );

                if ( !AdminWorkgroupHome.isUserInWorkgroup( user, workgroup.getKey( ) ) )
                {
                    AdminWorkgroupHome.addUserForWorkgroup( user, workgroup.getKey( ) );
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doModifyUserFields( AdminUser user, List<AdminUserField> listUserFields, Locale locale, AdminUser currentUser )
    {
        Plugin plugin = PluginService.getPlugin( ProfilesPlugin.PLUGIN_NAME );
        // Check if the user has a profile
        List<Profile> listAssignedProfiles = ProfileHome.findProfileByIdUser( user.getUserId( ), plugin );

        if ( listAssignedProfiles != null )
        {
            for ( Profile assignedProfile : listAssignedProfiles )
            {
                // Remove profiles, rights, roles and workgroups from the user
                String strKey = assignedProfile.getKey( );

                // Remove all profiles from the user
                ProfileHome.removeUserFromProfile( strKey, user.getUserId( ), plugin );

                // Remove rights to the user
                for ( Right right : ProfileHome.getRightsListForProfile( strKey, plugin ) )
                {
                    right = RightHome.findByPrimaryKey( right.getId( ) );

                    if ( AdminUserHome.hasRight( user, right.getId( ) )
                            && ( ( user.getUserLevel( ) > currentUser.getUserLevel( ) ) || currentUser.isAdmin( ) ) )
                    {
                        AdminUserHome.removeRightForUser( user.getUserId( ), right.getId( ) );
                    }
                }

                // Remove roles to the user
                for ( RBACRole role : ProfileHome.getRolesListForProfile( strKey, plugin ) )
                {
                    role = RBACRoleHome.findByPrimaryKey( role.getKey( ) );

                    if ( AdminUserHome.hasRole( user, role.getKey( ) ) )
                    {
                        AdminUserHome.removeRoleForUser( user.getUserId( ), role.getKey( ) );
                    }
                }

                // Remove workgroups to the user
                for ( AdminWorkgroup workgroup : ProfileHome.getWorkgroupsListForProfile( strKey, plugin ) )
                {
                    workgroup = AdminWorkgroupHome.findByPrimaryKey( workgroup.getKey( ) );

                    if ( AdminWorkgroupHome.isUserInWorkgroup( user, workgroup.getKey( ) ) )
                    {
                        AdminWorkgroupHome.removeUserFromWorkgroup( user, workgroup.getKey( ) );
                    }
                }
            }
        }

        // For each selected profiles
        for ( AdminUserField userField : listUserFields )
        {
            String strProfileKey = userField.getValue( );
            int nIdUser = user.getUserId( );

            if ( !ProfileHome.hasProfile( strProfileKey, nIdUser, plugin ) )
            {
                ProfileHome.addUserForProfile( strProfileKey, nIdUser, plugin );

                // Add rights to the user
                for ( Right right : ProfileHome.getRightsListForProfile( strProfileKey, plugin ) )
                {
                    right = RightHome.findByPrimaryKey( right.getId( ) );

                    if ( right != null && !AdminUserHome.hasRight( user, right.getId( ) ) && ( user.getUserLevel( ) <= right.getLevel( ) ) )
                    {
                        AdminUserHome.createRightForUser( nIdUser, right.getId( ) );
                    }
                }

                // Add roles to the user
                for ( RBACRole role : ProfileHome.getRolesListForProfile( strProfileKey, plugin ) )
                {
                    role = RBACRoleHome.findByPrimaryKey( role.getKey( ) );

                    if ( role != null && !AdminUserHome.hasRole( user, role.getKey( ) ) )
                    {
                        AdminUserHome.createRoleForUser( nIdUser, role.getKey( ) );
                    }
                }

                // Add workgroups to the user
                for ( AdminWorkgroup workgroup : ProfileHome.getWorkgroupsListForProfile( strProfileKey, plugin ) )
                {
                    workgroup = AdminWorkgroupHome.findByPrimaryKey( workgroup.getKey( ) );

                    if ( workgroup != null && !AdminWorkgroupHome.isUserInWorkgroup( user, workgroup.getKey( ) ) )
                    {
                        AdminWorkgroupHome.addUserForWorkgroup( user, workgroup.getKey( ) );
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRemoveUserFields( AdminUser user, Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( ProfilesPlugin.PLUGIN_NAME );
        ProfileHome.removeProfilesFromUser( user.getUserId( ), plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Plugin getPlugin( )
    {
        return PluginService.getPlugin( ProfilesPlugin.PLUGIN_NAME );
    }
}
