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
package fr.paris.lutece.plugins.profiles.service;

import fr.paris.lutece.plugins.profiles.business.Profile;
import fr.paris.lutece.plugins.profiles.business.ProfileAction;
import fr.paris.lutece.plugins.profiles.business.ProfileFilter;
import fr.paris.lutece.plugins.profiles.business.views.View;
import fr.paris.lutece.portal.business.rbac.AdminRole;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroup;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.ItemNavigator;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * IProfilesService
 *
 */
public interface IProfilesService
{
    // GET

    /**
    * Get the item navigator
    * @param pFilter the profile filter
    * @param profile the profile
    * @param url the url
    * @return the item navigator
    */
    ItemNavigator getItemNavigator( ProfileFilter pFilter, Profile profile, UrlItem url );

    /**
     * Get the list of actions
     * @param user the current user
     * @param profile the profile
     * @param strPermission the permission name
     * @param locale Locale
     * @param plugin Plugin
     * @return the list of actions
     */
    List<ProfileAction> getListActions( AdminUser user, Profile profile, String strPermission, Locale locale,
        Plugin plugin );

    /**
     * Do assign user to a profile
     * @param nIdUser the id suer
     * @param request the HTTP request
     * @param locale the Locale
     */
    void doAssignUserToProfile( int nIdUser, HttpServletRequest request, Locale locale );

    /**
     * Do unassign user from profile
     * @param nIdUser the id user
     * @param strProfileKey the profile key
     * @param currentUser the current user
     * @param request the HTTP request
     * @param locale the locale
     * @param plugin the plugin
     */
    void doUnassignUserFromProfile( int nIdUser, String strProfileKey, AdminUser currentUser,
        HttpServletRequest request, Locale locale, Plugin plugin );

    /**
     * Creation of an instance of profile
     * @param profile The instance of the profile which contains the informations to store
     * @param locale the Locale
     * @param plugin Plugin
     * @return The instance of profile which has been created with its primary key.
     */
    Profile create( Profile profile, Locale locale, Plugin plugin );

    /**
     * Update of the profile which is specified in parameter
     * @param profile The instance of the profile which contains the new data to store
     * @param locale the Locale
     * @param plugin Plugin
     * @return The instance of the profile which has been updated
     */
    Profile update( Profile profile, Locale locale, Plugin plugin );

    /**
     * Remove the Profile whose identifier is specified in parameter
     * @param strProfileKey The Profile object to remove
     * @param locale the Locale
     * @param plugin Plugin
     */
    void remove( String strProfileKey, Locale locale, Plugin plugin );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a profile whose identifier is specified in parameter
     * @param strProfileKey The key of the profile
     * @param plugin Plugin
     * @return An instance of profile
     */
    Profile findByPrimaryKey( String strProfileKey, Plugin plugin );

    /**
     * Returns a collection of profiles objects
     * @param plugin Plugin
     * @return A collection of profiles
     */
    List<Profile> findAll( Plugin plugin );

    /**
     * Find profile by filter
     * @param pFilter the Filter
     * @param plugin Plugin
     * @return List of profiles
     */
    List<Profile> findProfilesByFilter( ProfileFilter pFilter, Plugin plugin );

    /**
     * Check if a profile already exists or not
     * @param strProfileKey The profile key
     * @param plugin Plugin
     * @return true if it already exists
     */
    boolean checkExistProfile( String strProfileKey, Plugin plugin );

    /**
     * Get the list of profiles
     * @param plugin Plugin
     * @return the list of profiles
     */
    ReferenceList getProfilesList( Plugin plugin );

    /**
     * Check if the profile is attributed to any user
     * @param strProfileKey the profile key
     * @param plugin Plugin
     * @return true if it is attributed to at least one user, false otherwise
     */
    boolean checkProfileAttributed( String strProfileKey, Plugin plugin );

    /**
     * Load the list of profiles by a given ID user
     * @param nIdUser the ID user
     * @param plugin Plugin
     * @return The list of profiles
     */
    List<Profile> findProfileByIdUser( int nIdUser, Plugin plugin );

    /* RIGHTS */

    /**
     * Get the list of rights associated to the profile
     * @param strProfileKey The profile Key
     * @param plugin Plugin
     * @return The list of Right
     */
    List<Right> getRightsListForProfile( String strProfileKey, Plugin plugin );

    /**
     * Check if a profile has the given right.
     * @param strProfileKey The profile Key
     * @param strIdRight The Right ID
     * @param plugin Plugin
     * @return true if the profile has the right, false otherwise
     */
    boolean hasRight( String strProfileKey, String strIdRight, Plugin plugin );

    /**
     * Add a right for a profile
     * @param strProfileKey The profile Key
     * @param strIdRight The Right ID
     * @param plugin Plugin
     */
    void addRightForProfile( String strProfileKey, String strIdRight, Plugin plugin );

    /**
     * Remove a right from a profile
     * @param strProfileKey The profile Key
     * @param strIdRight The Right ID
     * @param plugin Plugin
     */
    void removeRightFromProfile( String strProfileKey, String strIdRight, Plugin plugin );

    /**
     * Remove all rights from profile
     * @param strProfileKey The profile key
     * @param plugin Plugin
     */
    void removeRights( String strProfileKey, Plugin plugin );

    /* WORKGROUPS */

    /**
     * Get the list of workgroups associated to the profile
     * @param strProfileKey The profile Key
     * @param plugin Plugin
     * @return The list of workgroups
     */
    List<AdminWorkgroup> getWorkgroupsListForProfile( String strProfileKey, Plugin plugin );

    /**
     * Check if a profile has the given workgroup.
     * @param strProfileKey The profile Key
     * @param strWorkgroupKey The Workgroup key
     * @param plugin Plugin
     * @return true if the profile has the workgroup, false otherwise
     */
    boolean hasWorkgroup( String strProfileKey, String strWorkgroupKey, Plugin plugin );

    /**
     * Add a workgroup for a profile
     * @param strProfileKey The profile Key
     * @param strWorkgroupKey The WorkgroupKey
     * @param plugin Plugin
     */
    void addWorkgroupForProfile( String strProfileKey, String strWorkgroupKey, Plugin plugin );

    /**
     * Remove a workgroup from a profile
     * @param strProfileKey The profile Key
     * @param strWorkgroupKey The Workgroup key
     * @param plugin Plugin
     */
    void removeWorkgroupFromProfile( String strProfileKey, String strWorkgroupKey, Plugin plugin );

    /**
     * Remove all workgroups from profile
     * @param strProfileKey The profile key
     * @param plugin Plugin
     */
    void removeWorkgroups( String strProfileKey, Plugin plugin );

    /* ROLES */

    /**
     * Get the list of roles associated to the profile
     * @param strProfileKey The profile Key
     * @param plugin Plugin
     * @return The list of roles
     */
    List<AdminRole> getRolesListForProfile( String strProfileKey, Plugin plugin );

    /**
     * Check if a profile has the given role.
     * @param strProfileKey The profile Key
     * @param strRoleKey The Role key
     * @param plugin Plugin
     * @return true if the profile has the role, false otherwise
     */
    boolean hasRole( String strProfileKey, String strRoleKey, Plugin plugin );

    /**
     * Add a role for a profile
     * @param strProfileKey The profile Key
     * @param strRoleKey The RoleKey
     * @param plugin Plugin
     */
    void addRoleForProfile( String strProfileKey, String strRoleKey, Plugin plugin );

    /**
     * Remove a role from a profile
     * @param strProfileKey The profile Key
     * @param strRoleKey The role key
     * @param plugin Plugin
     */
    void removeRoleFromProfile( String strProfileKey, String strRoleKey, Plugin plugin );

    /**
     * Remove all roles from profile
     * @param strProfileKey The profile key
     * @param plugin Plugin
     */
    void removeRoles( String strProfileKey, Plugin plugin );

    /* USERS */

    /**
     * Get the list of users associated to the profile
     * @param strProfileKey The profile Key
     * @param plugin Plugin
     * @return The list of users
     */
    List<AdminUser> getUsersListForProfile( String strProfileKey, Plugin plugin );

    /**
     * Check if a profile has the given user.
     * @param strProfileKey The profile Key
     * @param nIdUser The User ID
     * @param plugin Plugin
     * @return true if the profile has the user, false otherwise
     */
    boolean hasUser( String strProfileKey, int nIdUser, Plugin plugin );

    /**
     * Add an user for a profile
     * @param strProfileKey The profile Key
     * @param nIdUser The User ID
     * @param plugin Plugin
     */
    void addUserForProfile( String strProfileKey, int nIdUser, Plugin plugin );

    /**
     * Remove a user from a profile
     * @param strProfileKey The key of the profile
     * @param nIdUser The User ID
     * @param plugin Plugin
     */
    void removeUserFromProfile( String strProfileKey, int nIdUser, Plugin plugin );

    /**
     * Remove all users from profile
     * @param strProfileKey The profile key
     * @param plugin Plugin
     */
    void removeUsers( String strProfileKey, Plugin plugin );

    /**
     * Remove all profiles associated to an user
     * @param nIdUser The User ID
     * @param plugin Plugin
     */
    void removeProfilesFromUser( int nIdUser, Plugin plugin );

    /**
     * Check if the given user has a given profile or not
     * @param strProfileKey The key of the profile
     * @param nIdUser the ID user
     * @param plugin Plugin
     * @return true if the user has the profile, false otherwise
     */
    boolean hasProfile( String strProfileKey, int nIdUser, Plugin plugin );

    /* VIEW */

    /**
     * Get the view associated to the profile
     * @param strProfileKey the profile key
     * @param plugin Plugin
     * @return the view
     */
    View getViewForProfile( String strProfileKey, Plugin plugin );

    /**
     * Remove profile from a view
     * @param strProfileKey the profile key
     * @param plugin Plugin
     */
    void removeView( String strProfileKey, Plugin plugin );
}
