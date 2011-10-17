/*
 * Copyright (c) 2002-2011, Mairie de Paris
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

import fr.paris.lutece.plugins.profiles.business.views.View;
import fr.paris.lutece.portal.business.rbac.AdminRole;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroup;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;


/**
 *
 * ProfileHome
 *
 */
public final class ProfileHome
{
    // Static variable pointed at the DAO instance
    private static IProfileDAO _dao = (IProfileDAO) SpringContextService.getPluginBean( "profiles",
            "profiles.profileDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private ProfileHome(  )
    {
    }

    /**
     * Creation of an instance of profile
     * @param profile The instance of the profile which contains the informations to store
     * @param plugin Plugin
     * @return The instance of profile which has been created with its primary key.
     */
    public static Profile create( Profile profile, Plugin plugin )
    {
        _dao.insert( profile, plugin );

        return profile;
    }

    /**
     * Update of the profile which is specified in parameter
     * @param profile The instance of the profile which contains the new data to store
     * @param plugin Plugin
     * @return The instance of the profile which has been updated
     */
    public static Profile update( Profile profile, Plugin plugin )
    {
        _dao.store( profile, plugin );

        return profile;
    }

    /**
     * Remove the Profile whose identifier is specified in parameter
     * @param strProfileKey The Profile object to remove
     * @param plugin Plugin
     */
    public static void remove( String strProfileKey, Plugin plugin )
    {
        _dao.delete( strProfileKey, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a profile whose identifier is specified in parameter
     * @param strProfileKey The key of the profile
     * @param plugin Plugin
     * @return An instance of profile
     */
    public static Profile findByPrimaryKey( String strProfileKey, Plugin plugin )
    {
        return _dao.load( strProfileKey, plugin );
    }

    /**
     * Returns a collection of profiles objects
     * @param plugin Plugin
     * @return A collection of profiles
     */
    public static Collection<Profile> findAll( Plugin plugin )
    {
        return _dao.selectProfileList( plugin );
    }

    /**
     * Find profile by filter
     * @param pFilter the Filter
     * @param plugin Plugin
     * @return List of profiles
     */
    public static Collection<Profile> findProfilesByFilter( ProfileFilter pFilter, Plugin plugin )
    {
        return _dao.selectProfilesByFilter( pFilter, plugin );
    }

    /**
     * Check if a profile already exists or not
     * @param strProfileKey The profile key
     * @param plugin Plugin
     * @return true if it already exists
     */
    public static boolean checkExistProfile( String strProfileKey, Plugin plugin )
    {
        return _dao.checkExistProfile( strProfileKey, plugin );
    }

    /**
     * Get the list of profiles
     * @param plugin Plugin
     * @return the list of profiles
     */
    public static ReferenceList getProfilesList( Plugin plugin )
    {
        return _dao.getProfileList( plugin );
    }

    /**
     * Check if the profile is attributed to any user
     * @param strProfileKey the profile key
     * @param plugin Plugin
     * @return true if it is attributed to at least one user, false otherwise
     */
    public static boolean checkProfileAttributed( String strProfileKey, Plugin plugin )
    {
        return _dao.checkProfileAttributed( strProfileKey, plugin );
    }

    /**
     * Load the profile by a given ID user
     * @param nIdUser the ID user
     * @param plugin Plugin
     * @return a profile
     */
    public static Profile findProfileByIdUser( int nIdUser, Plugin plugin )
    {
        return _dao.selectProfileByIdUser( nIdUser, plugin );
    }

    /* RIGHTS */

    /**
     * Get the list of rights associated to the profile
     * @param strProfileKey The profile Key
     * @param plugin Plugin
     * @return The list of Right
     */
    public static Collection<Right> getRightsListForProfile( String strProfileKey, Plugin plugin )
    {
        return _dao.selectRightsListForProfile( strProfileKey, plugin );
    }

    /**
     * Check if a profile has the given right.
     * @param strProfileKey The profile Key
     * @param strIdRight The Right ID
     * @param plugin Plugin
     * @return true if the profile has the right, false otherwise
     */
    public static boolean hasRight( String strProfileKey, String strIdRight, Plugin plugin )
    {
        return _dao.hasRight( strProfileKey, strIdRight, plugin );
    }

    /**
     * Add a right for a profile
     * @param strProfileKey The profile Key
     * @param strIdRight The Right ID
     * @param plugin Plugin
     */
    public static void addRightForProfile( String strProfileKey, String strIdRight, Plugin plugin )
    {
        _dao.insertRightForProfile( strProfileKey, strIdRight, plugin );
    }

    /**
     * Remove a right from a profile
     * @param strProfileKey The profile Key
     * @param strIdRight The Right ID
     * @param plugin Plugin
     */
    public static void removeRightFromProfile( String strProfileKey, String strIdRight, Plugin plugin )
    {
        _dao.deleteRightFromProfile( strProfileKey, strIdRight, plugin );
    }

    /**
     * Remove all rights from profile
     * @param strProfileKey The profile key
     * @param plugin Plugin
     */
    public static void removeRights( String strProfileKey, Plugin plugin )
    {
        _dao.deleteRights( strProfileKey, plugin );
    }

    /* WORKGROUPS */

    /**
     * Get the list of workgroups associated to the profile
     * @param strProfileKey The profile Key
     * @param plugin Plugin
     * @return The list of workgroups
     */
    public static Collection<AdminWorkgroup> getWorkgroupsListForProfile( String strProfileKey, Plugin plugin )
    {
        return _dao.selectWorkgroupsListForProfile( strProfileKey, plugin );
    }

    /**
     * Check if a profile has the given workgroup.
     * @param strProfileKey The profile Key
     * @param strWorkgroupKey The Workgroup key
     * @param plugin Plugin
     * @return true if the profile has the workgroup, false otherwise
     */
    public static boolean hasWorkgroup( String strProfileKey, String strWorkgroupKey, Plugin plugin )
    {
        return _dao.hasWorkgroup( strProfileKey, strWorkgroupKey, plugin );
    }

    /**
     * Add a workgroup for a profile
     * @param strProfileKey The profile Key
     * @param strWorkgroupKey The WorkgroupKey
     * @param plugin Plugin
     */
    public static void addWorkgroupForProfile( String strProfileKey, String strWorkgroupKey, Plugin plugin )
    {
        _dao.insertWorkgroupForProfile( strProfileKey, strWorkgroupKey, plugin );
    }

    /**
     * Remove a workgroup from a profile
     * @param strProfileKey The profile Key
     * @param strWorkgroupKey The Workgroup key
     * @param plugin Plugin
     */
    public static void removeWorkgroupFromProfile( String strProfileKey, String strWorkgroupKey, Plugin plugin )
    {
        _dao.deleteWorkgroupFromProfile( strProfileKey, strWorkgroupKey, plugin );
    }

    /**
     * Remove all workgroups from profile
     * @param strProfileKey The profile key
     * @param plugin Plugin
     */
    public static void removeWorkgroups( String strProfileKey, Plugin plugin )
    {
        _dao.deleteWorkgroups( strProfileKey, plugin );
    }

    /* ROLES */

    /**
     * Get the list of roles associated to the profile
     * @param strProfileKey The profile Key
     * @param plugin Plugin
     * @return The list of roles
     */
    public static Collection<AdminRole> getRolesListForProfile( String strProfileKey, Plugin plugin )
    {
        return _dao.selectRolesListForProfile( strProfileKey, plugin );
    }

    /**
     * Check if a profile has the given role.
     * @param strProfileKey The profile Key
     * @param strRoleKey The Role key
     * @param plugin Plugin
     * @return true if the profile has the role, false otherwise
     */
    public static boolean hasRole( String strProfileKey, String strRoleKey, Plugin plugin )
    {
        return _dao.hasRole( strProfileKey, strRoleKey, plugin );
    }

    /**
     * Add a role for a profile
     * @param strProfileKey The profile Key
     * @param strRoleKey The RoleKey
     * @param plugin Plugin
     */
    public static void addRoleForProfile( String strProfileKey, String strRoleKey, Plugin plugin )
    {
        _dao.insertRoleForProfile( strProfileKey, strRoleKey, plugin );
    }

    /**
     * Remove a role from a profile
     * @param strProfileKey The profile Key
     * @param strRoleKey The role key
     * @param plugin Plugin
     */
    public static void removeRoleFromProfile( String strProfileKey, String strRoleKey, Plugin plugin )
    {
        _dao.deleteRoleFromProfile( strProfileKey, strRoleKey, plugin );
    }

    /**
     * Remove all roles from profile
     * @param strProfileKey The profile key
     * @param plugin Plugin
     */
    public static void removeRoles( String strProfileKey, Plugin plugin )
    {
        _dao.deleteRoles( strProfileKey, plugin );
    }

    /* USERS */

    /**
     * Get the list of users associated to the profile
     * @param strProfileKey The profile Key
     * @param plugin Plugin
     * @return The list of users
     */
    public static Collection<AdminUser> getUsersListForProfile( String strProfileKey, Plugin plugin )
    {
        return _dao.selectUsersListForProfile( strProfileKey, plugin );
    }

    /**
     * Check if a profile has the given user.
     * @param strProfileKey The profile Key
     * @param nIdUser The User ID
     * @param plugin Plugin
     * @return true if the profile has the user, false otherwise
     */
    public static boolean hasUser( String strProfileKey, int nIdUser, Plugin plugin )
    {
        return _dao.hasUser( strProfileKey, nIdUser, plugin );
    }

    /**
     * Add an user for a profile
     * @param strProfileKey The profile Key
     * @param nIdUser The User ID
     * @param plugin Plugin
     */
    public static void addUserForProfile( String strProfileKey, int nIdUser, Plugin plugin )
    {
        _dao.insertUserForProfile( strProfileKey, nIdUser, plugin );
    }

    /**
     * Remove a user from a profile
     * @param strProfileKey The profile Key
     * @param nIdUser The User ID
     * @param plugin Plugin
     */
    public static void removeUserFromProfile( String strProfileKey, int nIdUser, Plugin plugin )
    {
        _dao.deleteUserFromProfile( strProfileKey, nIdUser, plugin );
    }

    /**
     * Remove all users from profile
     * @param strProfileKey The profile key
     * @param plugin Plugin
     */
    public static void removeUsers( String strProfileKey, Plugin plugin )
    {
        _dao.deleteUsers( strProfileKey, plugin );
    }

    /**
     * Remove all profiles associated to an user
     * @param nIdUser The User ID
     * @param plugin Plugin
     */
    public static void removeProfilesFromUser( int nIdUser, Plugin plugin )
    {
        _dao.deleteProfilesFromUser( nIdUser, plugin );
    }

    /**
     * Check if the given user has a profile or not
     * @param nIdUser the ID user
     * @param plugin Plugin
     * @return true if the user has the profile, false otherwise
     */
    public static boolean hasProfile( int nIdUser, Plugin plugin )
    {
        return _dao.hasProfile( nIdUser, plugin );
    }

    /* VIEW */

    /**
     * Get the view associated to the profile
     * @param strProfileKey the profile key
     * @param plugin Plugin
     * @return the view
     */
    public static View getViewForProfile( String strProfileKey, Plugin plugin )
    {
        return _dao.selectViewForProfile( strProfileKey, plugin );
    }

    /**
     * Remove profile from a view
     * @param strProfileKey the profile key
     * @param plugin Plugin
     */
    public static void removeView( String strProfileKey, Plugin plugin )
    {
        _dao.deleteView( strProfileKey, plugin );
    }
}
