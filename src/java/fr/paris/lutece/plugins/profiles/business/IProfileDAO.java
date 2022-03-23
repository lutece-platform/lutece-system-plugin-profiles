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
package fr.paris.lutece.plugins.profiles.business;

import fr.paris.lutece.plugins.profiles.business.views.View;
import fr.paris.lutece.portal.business.rbac.RBACRole;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroup;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;

/**
 *
 * IProfileDAO
 *
 */
public interface IProfileDAO
{
    /**
     * Delete a record from the table
     * 
     * @param strProfileKey
     *            The profile key
     * @param plugin
     *            Plugin
     */
    void delete( String strProfileKey, Plugin plugin );

    /**
     * Insert a new record in the table.
     * 
     * @param profile
     *            The profile object
     * @param plugin
     *            Plugin
     */
    void insert( Profile profile, Plugin plugin );

    /**
     * Load the data of Profile from the table
     * 
     * @param strProfileKey
     *            The profile key
     * @param plugin
     *            Plugin
     * @return the instance of the Profile
     */
    Profile load( String strProfileKey, Plugin plugin );

    /**
     * Load the list of profiles
     * 
     * @param plugin
     *            Plugin
     * @return The List of the profiles
     */
    List<Profile> selectProfileList( Plugin plugin );

    /**
     * Update the record identified by the given profile key with the given profile in the table
     * 
     * @param profile
     *            The reference of profile to be the new one
     * @param plugin
     *            Plugin
     */
    void store( Profile profile, Plugin plugin );

    /**
     * Find profile by filter
     * 
     * @param pFilter
     *            the Filter
     * @param plugin
     *            Plugin
     * @return List of profiles
     */
    List<Profile> selectProfilesByFilter( ProfileFilter pFilter, Plugin plugin );

    /**
     * Check if a profile already exists or not
     * 
     * @param strKey
     *            The profile key
     * @param plugin
     *            Plugin
     * @return true if it already exists
     */
    boolean checkExistProfile( String strKey, Plugin plugin );

    /**
     * Get the list of profiles
     * 
     * @param plugin
     *            Plugin
     * @return the list of profiles
     */
    ReferenceList getProfileList( Plugin plugin );

    /**
     * Check if the profile is attributed to any user
     * 
     * @param strProfileKey
     *            the profile key
     * @param plugin
     *            Plugin
     * @return true if it is attributed to at least one user, false otherwise
     */
    boolean checkProfileAttributed( String strProfileKey, Plugin plugin );

    /**
     * Load the profiles by a given ID user
     * 
     * @param nIdUser
     *            the ID user
     * @param plugin
     *            Plugin
     * @return the list of profiles of the user
     */
    List<Profile> selectProfileByIdUser( int nIdUser, Plugin plugin );

    /* RIGHTS */

    /**
     * Get the list of rights associated to the profile
     * 
     * @param strProfileKey
     *            The profile Key
     * @param plugin
     *            Plugin
     * @return The list of Right
     */
    List<Right> selectRightsListForProfile( String strProfileKey, Plugin plugin );

    /**
     * Check if a profile has the given right.
     * 
     * @param strProfileKey
     *            The profile Key
     * @param strIdRight
     *            The Right ID
     * @param plugin
     *            Plugin
     * @return true if the profile has the right, false otherwise
     */
    boolean hasRight( String strProfileKey, String strIdRight, Plugin plugin );

    /**
     * Add a right for a profile
     * 
     * @param strProfileKey
     *            The profile Key
     * @param strIdRight
     *            The Right ID
     * @param plugin
     *            Plugin
     */
    void insertRightForProfile( String strProfileKey, String strIdRight, Plugin plugin );

    /**
     * Remove a right from a profile
     * 
     * @param strProfileKey
     *            The profile Key
     * @param strIdRight
     *            The Right ID
     * @param plugin
     *            Plugin
     */
    void deleteRightFromProfile( String strProfileKey, String strIdRight, Plugin plugin );

    /**
     * Remove all rights from profile
     * 
     * @param strProfileKey
     *            The profile key
     * @param plugin
     *            Plugin
     */
    void deleteRights( String strProfileKey, Plugin plugin );

    /* WORKGROUPS */

    /**
     * Get the list of workgroups associated to the profile
     * 
     * @param strProfileKey
     *            The profile Key
     * @param plugin
     *            Plugin
     * @return The list of workgroups
     */
    List<AdminWorkgroup> selectWorkgroupsListForProfile( String strProfileKey, Plugin plugin );

    /**
     * Check if a profile has the given workgroup.
     * 
     * @param strProfileKey
     *            The profile Key
     * @param strWorkgroupKey
     *            The Workgroup key
     * @param plugin
     *            Plugin
     * @return true if the profile has the workgroup, false otherwise
     */
    boolean hasWorkgroup( String strProfileKey, String strWorkgroupKey, Plugin plugin );

    /**
     * Add a workgroup for a profile
     * 
     * @param strProfileKey
     *            The profile Key
     * @param strWorkgroupKey
     *            The WorkgroupKey
     * @param plugin
     *            Plugin
     */
    void insertWorkgroupForProfile( String strProfileKey, String strWorkgroupKey, Plugin plugin );

    /**
     * Remove a workgroup from a profile
     * 
     * @param strProfileKey
     *            The profile Key
     * @param strWorkgroupKey
     *            The Right ID
     * @param plugin
     *            Plugin
     */
    void deleteWorkgroupFromProfile( String strProfileKey, String strWorkgroupKey, Plugin plugin );

    /**
     * Remove all workgroups from profile
     * 
     * @param strProfileKey
     *            The profile key
     * @param plugin
     *            Plugin
     */
    void deleteWorkgroups( String strProfileKey, Plugin plugin );

    /* ROLES */

    /**
     * Get the list of roles associated to the profile
     * 
     * @param strProfileKey
     *            The profile Key
     * @param plugin
     *            Plugin
     * @return The list of roles
     */
    List<RBACRole> selectRolesListForProfile( String strProfileKey, Plugin plugin );

    /**
     * Check if a profile has the given role.
     * 
     * @param strProfileKey
     *            The profile Key
     * @param strRoleKey
     *            The Role key
     * @param plugin
     *            Plugin
     * @return true if the profile has the workgroup, false otherwise
     */
    boolean hasRole( String strProfileKey, String strRoleKey, Plugin plugin );

    /**
     * Add a roles for a profile
     * 
     * @param strProfileKey
     *            The profile Key
     * @param strRoleKey
     *            The WorkgroupKey
     * @param plugin
     *            Plugin
     */
    void insertRoleForProfile( String strProfileKey, String strRoleKey, Plugin plugin );

    /**
     * Remove a roles from a profile
     * 
     * @param strProfileKey
     *            The profile Key
     * @param strRoleKey
     *            The Right ID
     * @param plugin
     *            Plugin
     */
    void deleteRoleFromProfile( String strProfileKey, String strRoleKey, Plugin plugin );

    /**
     * Remove all roles from profile
     * 
     * @param strProfileKey
     *            The profile key
     * @param plugin
     *            Plugin
     */
    void deleteRoles( String strProfileKey, Plugin plugin );

    /* USERS */

    /**
     * Get the list of users associated to the profile
     * 
     * @param strProfileKey
     *            The profile Key
     * @param plugin
     *            Plugin
     * @return The list of users
     */
    List<AdminUser> selectUsersListForProfile( String strProfileKey, Plugin plugin );

    /**
     * Check if a profile has the given user.
     * 
     * @param strProfileKey
     *            The profile Key
     * @param nIdUser
     *            The User ID
     * @param plugin
     *            Plugin
     * @return true if the profile has the user, false otherwise
     */
    boolean hasUser( String strProfileKey, int nIdUser, Plugin plugin );

    /**
     * Add an user for a profile
     * 
     * @param strProfileKey
     *            The profile Key
     * @param nIdUser
     *            The User ID
     * @param plugin
     *            Plugin
     */
    void insertUserForProfile( String strProfileKey, int nIdUser, Plugin plugin );

    /**
     * Remove an user from a profile
     * 
     * @param strProfileKey
     *            The key of the profile
     * @param nIdUser
     *            The User ID
     * @param plugin
     *            Plugin
     */
    void deleteUserFromProfile( String strProfileKey, int nIdUser, Plugin plugin );

    /**
     * Remove all users from profile
     * 
     * @param strProfileKey
     *            The profile key
     * @param plugin
     *            Plugin
     */
    void deleteUsers( String strProfileKey, Plugin plugin );

    /**
     * Remove all profiles associated to an user
     * 
     * @param nIdUser
     *            The User ID
     * @param plugin
     *            Plugin
     */
    void deleteProfilesFromUser( int nIdUser, Plugin plugin );

    /**
     * Check if the given user has a given profile or not
     * 
     * @param strProfileKey
     *            The key of the profile
     * @param nIdUser
     *            the ID user
     * @param plugin
     *            Plugin
     * @return true if the user has the profile, false otherwise
     */
    boolean hasProfile( String strProfileKey, int nIdUser, Plugin plugin );

    /* VIEW */

    /**
     * Get the view associated to the profile
     * 
     * @param strProfileKey
     *            the profile key
     * @param plugin
     *            Plugin
     * @return the view
     */
    View selectViewForProfile( String strProfileKey, Plugin plugin );

    /**
     * Remove profile from a view
     * 
     * @param strProfileKey
     *            the profile key
     * @param plugin
     *            Plugin
     */
    void deleteView( String strProfileKey, Plugin plugin );
}
