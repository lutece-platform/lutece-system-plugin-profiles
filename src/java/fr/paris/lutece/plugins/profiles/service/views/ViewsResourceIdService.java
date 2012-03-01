/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.plugins.profiles.service.views;

import fr.paris.lutece.plugins.profiles.business.views.View;
import fr.paris.lutece.plugins.profiles.business.views.ViewHome;
import fr.paris.lutece.plugins.profiles.service.ProfilesPlugin;
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
public class ViewsResourceIdService extends ResourceIdService
{
    public static final String PERMISSION_CREATE_VIEW = "CREATE_VIEW";
    public static final String PERMISSION_MODIFY_VIEW = "MODIFY_VIEW";
    public static final String PERMISSION_DELETE_VIEW = "DELETE_VIEW";
    public static final String PERMISSION_MANAGE_PROFILES_ASSIGNMENT = "MANAGE_PROFILES_ASSIGNMENT";
    public static final String PERMISSION_MANAGE_DASHBOARDS = "MANAGE_DASHBOARDS";
    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "profiles.permission.label.resource_type_view";
    private static final String PROPERTY_LABEL_CREATE_VIEW = "profiles.permission.label.create_view";
    private static final String PROPERTY_LABEL_MODIFY_VIEW = "profiles.permission.label.modify_view";
    private static final String PROPERTY_LABEL_DELETE_VIEW = "profiles.permission.label.delete_view";
    private static final String PROPERTY_LABEL_MANAGE_PROFILES_ASSIGNMENT = "profiles.permission.label.manage_profiles_assignment";
    private static final String PROPERTY_LABEL_MANAGE_DASHBOARDS = "profiles.permission.label.manage_dashboards";

    /**
     * Create a new instance of ProfilesResourceIdService
     */
    public ViewsResourceIdService(  )
    {
        setPluginName( ProfilesPlugin.PLUGIN_NAME );
    }

    /**
    * Initializes the service
    */
    public void register(  )
    {
        ResourceType rt = new ResourceType(  );
        rt.setResourceIdServiceClass( ViewsResourceIdService.class.getName(  ) );
        rt.setResourceTypeKey( View.RESOURCE_TYPE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = new Permission(  );
        p.setPermissionKey( PERMISSION_CREATE_VIEW );
        p.setPermissionTitleKey( PROPERTY_LABEL_CREATE_VIEW );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_MODIFY_VIEW );
        p.setPermissionTitleKey( PROPERTY_LABEL_MODIFY_VIEW );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_DELETE_VIEW );
        p.setPermissionTitleKey( PROPERTY_LABEL_DELETE_VIEW );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_MANAGE_PROFILES_ASSIGNMENT );
        p.setPermissionTitleKey( PROPERTY_LABEL_MANAGE_PROFILES_ASSIGNMENT );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_MANAGE_DASHBOARDS );
        p.setPermissionTitleKey( PROPERTY_LABEL_MANAGE_DASHBOARDS );
        rt.registerPermission( p );

        ResourceTypeManager.registerResourceType( rt );
    }

    /**
    * Returns a list of profiles resource ids
    * @param locale The current locale
    * @return A list of resource ids
    */
    public ReferenceList getResourceIdList( Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( ProfilesPlugin.PLUGIN_NAME );

        return ViewHome.getViewsList( plugin );
    }

    /**
    * Returns the Title of a given resource
    * @param strViewKey the view key
    * @param locale The current locale
    * @return The Title of a given resource
    */
    public String getTitle( String strViewKey, Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( ProfilesPlugin.PLUGIN_NAME );
        View view = ViewHome.findByPrimaryKey( strViewKey, plugin );

        return ( view != null ) ? view.getKey(  ) : null;
    }
}
