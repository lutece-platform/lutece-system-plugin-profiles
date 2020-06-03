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
package fr.paris.lutece.plugins.profiles.service;

import fr.paris.lutece.plugins.profiles.business.Profile;
import fr.paris.lutece.plugins.profiles.business.ProfileHome;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.util.ReferenceList;

import java.util.Locale;

/**
 *
 * Class ProfileResourceIdService
 *
 */
public class ProfilesResourceIdService extends ResourceIdService
{
    public static final String PERMISSION_CREATE_PROFILE = "CREATE_PROFILE";
    public static final String PERMISSION_MODIFY_PROFILE = "MODIFY_PROFILE";
    public static final String PERMISSION_DELETE_PROFILE = "DELETE_PROFILE";
    public static final String PERMISSION_MANAGE_USERS_ASSIGNMENT = "MANAGE_USERS_ASSIGNMENT";
    public static final String PERMISSION_MANAGE_RIGHTS_ASSIGNMENT = "MANAGE_RIGHTS_ASSIGNMENT";
    public static final String PERMISSION_MANAGE_ROLES_ASSIGNMENT = "MANAGE_ROLES_ASSIGNMENT";
    public static final String PERMISSION_MANAGE_WORKGROUPS_ASSIGNMENT = "MANAGE_WORKGROUPS_ASSIGNMENT";
    public static final String PERMISSION_MANAGE_VIEW_ASSIGNMENT = "MANAGE_VIEW_ASSIGNMENT";
    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "profiles.permission.label.resource_type_profile";
    private static final String PROPERTY_LABEL_CREATE_PROFILE = "profiles.permission.label.create_profile";
    private static final String PROPERTY_LABEL_MODIFY_PROFILE = "profiles.permission.label.modify_profile";
    private static final String PROPERTY_LABEL_DELETE_PROFILE = "profiles.permission.label.delete_profile";
    private static final String PROPERTY_LABEL_MANAGE_USERS_ASSIGNMENT = "profiles.permission.label.manage_users_assignment";
    private static final String PROPERTY_LABEL_MANAGE_RIGHTS_ASSIGNMENT = "profiles.permission.label.manage_rights_assignment";
    private static final String PROPERTY_LABEL_MANAGE_ROLES_ASSIGNMENT = "profiles.permission.label.manage_roles_assignment";
    private static final String PROPERTY_LABEL_MANAGE_WORKGROUPS_ASSIGNMENT = "profiles.permission.label.manage_workgroups_assignment";
    private static final String PROPERTY_LABEL_MANAGE_VIEW_ASSIGNMENT = "profiles.permission.label.manage_view_assignment";

    /**
     * Create a new instance of ProfilesResourceIdService
     */
    public ProfilesResourceIdService( )
    {
        setPluginName( ProfilesPlugin.PLUGIN_NAME );
    }

    /**
     * Initializes the service
     */
    public void register( )
    {
        ResourceType rt = new ResourceType( );
        rt.setResourceIdServiceClass( ProfilesResourceIdService.class.getName( ) );
        rt.setResourceTypeKey( Profile.RESOURCE_TYPE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = new Permission( );
        p.setPermissionKey( PERMISSION_CREATE_PROFILE );
        p.setPermissionTitleKey( PROPERTY_LABEL_CREATE_PROFILE );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_MODIFY_PROFILE );
        p.setPermissionTitleKey( PROPERTY_LABEL_MODIFY_PROFILE );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_DELETE_PROFILE );
        p.setPermissionTitleKey( PROPERTY_LABEL_DELETE_PROFILE );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_MANAGE_USERS_ASSIGNMENT );
        p.setPermissionTitleKey( PROPERTY_LABEL_MANAGE_USERS_ASSIGNMENT );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_MANAGE_RIGHTS_ASSIGNMENT );
        p.setPermissionTitleKey( PROPERTY_LABEL_MANAGE_RIGHTS_ASSIGNMENT );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_MANAGE_ROLES_ASSIGNMENT );
        p.setPermissionTitleKey( PROPERTY_LABEL_MANAGE_ROLES_ASSIGNMENT );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_MANAGE_WORKGROUPS_ASSIGNMENT );
        p.setPermissionTitleKey( PROPERTY_LABEL_MANAGE_WORKGROUPS_ASSIGNMENT );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_MANAGE_VIEW_ASSIGNMENT );
        p.setPermissionTitleKey( PROPERTY_LABEL_MANAGE_VIEW_ASSIGNMENT );
        rt.registerPermission( p );

        ResourceTypeManager.registerResourceType( rt );
    }

    /**
     * Returns a list of profiles resource ids
     * 
     * @param locale
     *            The current locale
     * @return A list of resource ids
     */
    public ReferenceList getResourceIdList( Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( ProfilesPlugin.PLUGIN_NAME );

        return ProfileHome.getProfilesList( plugin );
    }

    /**
     * Returns the Title of a given resource
     * 
     * @param strProfileKey
     *            the profile key
     * @param locale
     *            The current locale
     * @return The Title of a given resource
     */
    public String getTitle( String strProfileKey, Locale locale )
    {
        return strProfileKey;
    }
}
