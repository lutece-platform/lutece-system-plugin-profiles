/*
 * Copyright (c) 2002-2022, City of Paris
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

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.profiles.business.Profile;
import fr.paris.lutece.plugins.profiles.business.ProfileAction;
import fr.paris.lutece.plugins.profiles.business.ProfileFilter;
import fr.paris.lutece.plugins.profiles.business.ProfileHome;
import fr.paris.lutece.plugins.profiles.business.views.View;
import fr.paris.lutece.plugins.profiles.service.action.IProfileActionService;
import fr.paris.lutece.plugins.profiles.utils.constants.ProfilesConstants;
import fr.paris.lutece.portal.business.rbac.RBACRole;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.attribute.AdminUserField;
import fr.paris.lutece.portal.business.user.attribute.AdminUserFieldHome;
import fr.paris.lutece.portal.business.user.attribute.AttributeField;
import fr.paris.lutece.portal.business.user.attribute.IAttribute;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroup;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.user.attribute.AttributeFieldService;
import fr.paris.lutece.portal.service.user.attribute.AttributeService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.ItemNavigator;
import fr.paris.lutece.util.url.UrlItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * ProfilesService
 * 
 */
public class ProfilesService implements IProfilesService
{
    @Inject
    private IProfileActionService _profileActionService;

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemNavigator getItemNavigator( ProfileFilter pFilter, Profile profile, UrlItem url )
    {
        Plugin plugin = PluginService.getPlugin( ProfilesPlugin.PLUGIN_NAME );
        List<String> listItem = new ArrayList<>( );
        Collection<Profile> listAllProfiles = ProfileHome.findProfilesByFilter( pFilter, plugin );
        int nIndex = 0;
        int nCurrentItemId = 0;

        for ( Profile allProfile : listAllProfiles )
        {
            listItem.add( allProfile.getKey( ) );

            if ( allProfile.getKey( ).equals( profile.getKey( ) ) )
            {
                nCurrentItemId = nIndex;
            }

            nIndex++;
        }

        return new ItemNavigator( listItem, nCurrentItemId, url.getUrl( ), ProfilesConstants.PARAMETER_PROFILE_KEY );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProfileAction> getListActions( AdminUser user, Profile profile, String strPermission, Locale locale, Plugin plugin )
    {
        List<ProfileAction> listActions = new ArrayList<>( );

        for ( ProfileAction action : _profileActionService.selectActionsList( locale, plugin ) )
        {
            if ( !action.getPermission( ).equals( strPermission ) )
            {
                listActions.add( action );
            }
        }

        listActions = (List<ProfileAction>) RBACService.getAuthorizedActionsCollection( listActions, profile, (User) user );

        return listActions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doAssignUserToProfile( int nIdUser, HttpServletRequest request, Locale locale )
    {
        AdminUser user = AdminUserHome.findByPrimaryKey( nIdUser );

        if ( user != null )
        {
            ProfilesAdminUserFieldListenerService.getService( ).doCreateUserFields( user, request, locale );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doUnassignUserFromProfile( int nIdUser, String strProfileKey, AdminUser currentUser, HttpServletRequest request, Locale locale, Plugin plugin )
    {
        AdminUser user = AdminUserHome.findByPrimaryKey( nIdUser );

        // Remove User Fields
        List<IAttribute> listAttributes = AttributeService.getInstance( ).getPluginAttributesWithoutFields( ProfilesPlugin.PLUGIN_NAME, locale );
        IAttribute attribute = listAttributes.get( 0 );
        List<AdminUserField> listUserFields = AdminUserFieldHome.selectUserFieldsByIdUserIdAttribute( user.getUserId( ), attribute.getIdAttribute( ) );

        for ( AdminUserField userField : listUserFields )
        {
            AdminUserFieldHome.remove( userField );
        }

        // Remove profile
        removeUserFromProfile( strProfileKey, nIdUser, plugin );

        List<Profile> listProfiles = findProfileByIdUser( nIdUser, plugin );
        Set<Right> listProfilesRights = new HashSet<>( );
        Set<RBACRole> listProfilesRoles = new HashSet<>( );
        Set<AdminWorkgroup> listProfilesWorkgroups = new HashSet<>( );
        for ( Profile profile : listProfiles )
        {
            if ( !StringUtils.equals( profile.getKey( ), strProfileKey ) )
            {
                listProfilesRights.addAll( getRightsListForProfile( profile.getKey( ), plugin ) );
                listProfilesRoles.addAll( getRolesListForProfile( profile.getKey( ), plugin ) );
                listProfilesWorkgroups.addAll( getWorkgroupsListForProfile( profile.getKey( ), plugin ) );
            }
        }

        // Remove rights to the user
        for ( Right right : getRightsListForProfile( strProfileKey, plugin ) )
        {
            if ( !listProfilesRights.contains( right ) && AdminUserHome.hasRight( user, right.getId( ) )
                    && ( ( user.getUserLevel( ) > currentUser.getUserLevel( ) ) || currentUser.isAdmin( ) ) )
            {
                AdminUserHome.removeRightForUser( nIdUser, right.getId( ) );
            }
        }

        // Remove roles to the user
        for ( RBACRole role : getRolesListForProfile( strProfileKey, plugin ) )
        {
            if ( !listProfilesRoles.contains( role ) && AdminUserHome.hasRole( user, role.getKey( ) ) )
            {
                AdminUserHome.removeRoleForUser( nIdUser, role.getKey( ) );
            }
        }

        // Remove workgroups to the user
        for ( AdminWorkgroup workgroup : getWorkgroupsListForProfile( strProfileKey, plugin ) )
        {
            if ( !listProfilesWorkgroups.contains( workgroup ) && AdminWorkgroupHome.isUserInWorkgroup( user, workgroup.getKey( ) ) )
            {
                AdminWorkgroupHome.removeUserFromWorkgroup( user, workgroup.getKey( ) );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Profile create( Profile profile, Locale locale, Plugin plugin )
    {
        if ( profile != null )
        {
            ProfileHome.create( profile, plugin );

            // Create user field
            List<IAttribute> listAttributes = AttributeService.getInstance( ).getPluginAttributesWithoutFields( ProfilesPlugin.PLUGIN_NAME, locale );
            AttributeField attributeField = new AttributeField( );
            attributeField.setTitle( profile.getKey( ) );
            attributeField.setValue( profile.getDescription( ) );
            attributeField.setDefaultValue( false );
            attributeField.setAttribute( listAttributes.get( 0 ) );
            AttributeFieldService.getInstance( ).createAttributeField( attributeField );
        }

        return profile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Profile update( Profile profile, Locale locale, Plugin plugin )
    {
        if ( profile != null )
        {
            ProfileHome.update( profile, plugin );

            // Modify user field
            List<IAttribute> listAttributes = AttributeService.getInstance( ).getPluginAttributesWithFields( ProfilesPlugin.PLUGIN_NAME, locale );

            for ( IAttribute attribute : listAttributes )
            {
                if ( ( attribute == null ) || ( attribute.getListAttributeFields( ) == null ) || attribute.getListAttributeFields( ).isEmpty( ) )
                {
                    continue;
                }

                for ( AttributeField attributeField : attribute.getListAttributeFields( ) )
                {
                    if ( ( attributeField.getTitle( ) != null ) && attributeField.getTitle( ).equals( profile.getKey( ) ) )
                    {
                        attributeField.setValue( profile.getDescription( ) );
                        AttributeFieldService.getInstance( ).updateAttributeField( attributeField );

                        break;
                    }
                }
            }
        }

        return profile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( String strProfileKey, Locale locale, Plugin plugin )
    {
        ProfileHome.remove( strProfileKey, plugin );

        // Remove user field
        List<IAttribute> listAttributes = AttributeService.getInstance( ).getPluginAttributesWithFields( ProfilesPlugin.PLUGIN_NAME, locale );

        for ( IAttribute attribute : listAttributes )
        {
            if ( ( attribute == null ) || ( attribute.getListAttributeFields( ) == null ) || attribute.getListAttributeFields( ).isEmpty( ) )
            {
                continue;
            }

            for ( AttributeField attributeField : attribute.getListAttributeFields( ) )
            {
                if ( ( attributeField.getTitle( ) != null ) && attributeField.getTitle( ).equals( strProfileKey ) )
                {
                    AttributeFieldService.getInstance( ).removeAttributeFieldFromIdField( attributeField.getIdField( ) );

                    break;
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * {@inheritDoc}
     */
    @Override
    public Profile findByPrimaryKey( String strProfileKey, Plugin plugin )
    {
        return ProfileHome.findByPrimaryKey( strProfileKey, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Profile> findAll( Plugin plugin )
    {
        return ProfileHome.findAll( plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Profile> findProfilesByFilter( ProfileFilter pFilter, Plugin plugin )
    {
        return ProfileHome.findProfilesByFilter( pFilter, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkExistProfile( String strProfileKey, Plugin plugin )
    {
        return ProfileHome.checkExistProfile( strProfileKey, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getProfilesList( Plugin plugin )
    {
        return ProfileHome.getProfilesList( plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkProfileAttributed( String strProfileKey, Plugin plugin )
    {
        return ProfileHome.checkProfileAttributed( strProfileKey, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Profile> findProfileByIdUser( int nIdUser, Plugin plugin )
    {
        return ProfileHome.findProfileByIdUser( nIdUser, plugin );
    }

    /* RIGHTS */

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Right> getRightsListForProfile( String strProfileKey, Plugin plugin )
    {
        return ProfileHome.getRightsListForProfile( strProfileKey, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasRight( String strProfileKey, String strIdRight, Plugin plugin )
    {
        return ProfileHome.hasRight( strProfileKey, strIdRight, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addRightForProfile( String strProfileKey, String strIdRight, Plugin plugin )
    {
        ProfileHome.addRightForProfile( strProfileKey, strIdRight, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeRightFromProfile( String strProfileKey, String strIdRight, Plugin plugin )
    {
        ProfileHome.removeRightFromProfile( strProfileKey, strIdRight, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeRights( String strProfileKey, Plugin plugin )
    {
        ProfileHome.removeRights( strProfileKey, plugin );
    }

    /* WORKGROUPS */

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AdminWorkgroup> getWorkgroupsListForProfile( String strProfileKey, Plugin plugin )
    {
        return ProfileHome.getWorkgroupsListForProfile( strProfileKey, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasWorkgroup( String strProfileKey, String strWorkgroupKey, Plugin plugin )
    {
        return ProfileHome.hasWorkgroup( strProfileKey, strWorkgroupKey, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWorkgroupForProfile( String strProfileKey, String strWorkgroupKey, Plugin plugin )
    {
        ProfileHome.addWorkgroupForProfile( strProfileKey, strWorkgroupKey, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeWorkgroupFromProfile( String strProfileKey, String strWorkgroupKey, Plugin plugin )
    {
        ProfileHome.removeWorkgroupFromProfile( strProfileKey, strWorkgroupKey, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeWorkgroups( String strProfileKey, Plugin plugin )
    {
        ProfileHome.removeWorkgroups( strProfileKey, plugin );
    }

    /* ROLES */

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RBACRole> getRolesListForProfile( String strProfileKey, Plugin plugin )
    {
        return ProfileHome.getRolesListForProfile( strProfileKey, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasRole( String strProfileKey, String strRoleKey, Plugin plugin )
    {
        return ProfileHome.hasRole( strProfileKey, strRoleKey, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addRoleForProfile( String strProfileKey, String strRoleKey, Plugin plugin )
    {
        ProfileHome.addRoleForProfile( strProfileKey, strRoleKey, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeRoleFromProfile( String strProfileKey, String strRoleKey, Plugin plugin )
    {
        ProfileHome.removeRoleFromProfile( strProfileKey, strRoleKey, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeRoles( String strProfileKey, Plugin plugin )
    {
        ProfileHome.removeRoles( strProfileKey, plugin );
    }

    /* USERS */

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AdminUser> getUsersListForProfile( String strProfileKey, Plugin plugin )
    {
        return ProfileHome.getUsersListForProfile( strProfileKey, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasUser( String strProfileKey, int nIdUser, Plugin plugin )
    {
        return ProfileHome.hasUser( strProfileKey, nIdUser, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addUserForProfile( String strProfileKey, int nIdUser, Plugin plugin )
    {
        ProfileHome.addUserForProfile( strProfileKey, nIdUser, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUserFromProfile( String strProfileKey, int nIdUser, Plugin plugin )
    {
        ProfileHome.removeUserFromProfile( strProfileKey, nIdUser, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUsers( String strProfileKey, Plugin plugin )
    {
        ProfileHome.removeUsers( strProfileKey, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeProfilesFromUser( int nIdUser, Plugin plugin )
    {
        ProfileHome.removeProfilesFromUser( nIdUser, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasProfile( String strProfileKey, int nIdUser, Plugin plugin )
    {
        return ProfileHome.hasProfile( strProfileKey, nIdUser, plugin );
    }

    /* VIEW */

    /**
     * {@inheritDoc}
     */
    @Override
    public View getViewForProfile( String strProfileKey, Plugin plugin )
    {
        return ProfileHome.getViewForProfile( strProfileKey, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeView( String strProfileKey, Plugin plugin )
    {
        ProfileHome.removeView( strProfileKey, plugin );
    }
}
