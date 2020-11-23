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

import fr.paris.lutece.plugins.profiles.business.views.View;
import fr.paris.lutece.plugins.profiles.utils.constants.ProfilesConstants;
import fr.paris.lutece.portal.business.rbac.RBACRole;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroup;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ProfileDAO
 * 
 */
public class ProfileDAO implements IProfileDAO
{
    private static final String SQL_QUERY_SELECT = " SELECT profile_key, profile_description FROM profile_profile WHERE profile_key = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO profile_profile (profile_key, profile_description) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM profile_profile WHERE profile_key = ? ";
    private static final String SQL_QUERY_UPDATE = " UPDATE profile_profile SET profile_description = ? WHERE profile_key = ? ";
    private static final String SQL_QUERY_SELECTALL = " SELECT profile_key, profile_description FROM profile_profile ORDER BY profile_key ";
    private static final String SQL_QUERY_SELECT_PROFILES_FROM_SEARCH = " SELECT profile_key, profile_description FROM profile_profile "
            + " WHERE profile_key LIKE ? AND profile_description LIKE ? ORDER BY profile_key ";
    private static final String SQL_QUERY_CHECK_PROFILE_ATTRIBUTED = " SELECT id_user FROM profile_user WHERE profile_key = ? LIMIT 1 ";
    private static final String SQL_QUERY_SELECT_PROFILE_FROM_ID_USER = " SELECT p.profile_key, p.profile_description "
            + " FROM profile_profile p INNER JOIN profile_user pu ON p.profile_key = pu.profile_key WHERE pu.id_user = ? ";
    private static final String SQL_QUERY_SELECT_RIGHTS_LIST_FOR_PROFILE = " SELECT id_right FROM profile_right WHERE profile_key = ? ORDER BY id_right ASC ";
    private static final String SQL_QUERY_SELECT_PROFILE_RIGHT = " SELECT profile_key, id_right FROM profile_right WHERE profile_key = ? AND profile_key = ? ";
    private static final String SQL_QUERY_INSERT_PROFILE_RIGHT = " INSERT INTO profile_right (profile_key, id_right) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE_PROFILE_RIGHT = " DELETE FROM profile_right WHERE profile_key = ? AND id_right = ? ";
    private static final String SQL_QUERY_DELETE_RIGHTS = " DELETE FROM profile_right WHERE profile_key = ? ";
    private static final String SQL_QUERY_SELECT_WORKGROUPS_LIST_FOR_PROFILE = " SELECT workgroup_key FROM profile_workgroup WHERE profile_key = ? ORDER BY workgroup_key ASC ";
    private static final String SQL_QUERY_SELECT_PROFILE_WORKGROUP = " SELECT profile_key, workgroup_key FROM profile_workgroup WHERE profile_key = ? AND workgroup_key = ? ";
    private static final String SQL_QUERY_INSERT_PROFILE_WORKGROUP = " INSERT INTO profile_workgroup (profile_key, workgroup_key) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE_PROFILE_WORKGROUP = " DELETE FROM profile_workgroup WHERE profile_key = ? AND workgroup_key = ? ";
    private static final String SQL_QUERY_DELETE_WORKGROUPS = " DELETE FROM profile_workgroup WHERE profile_key = ? ";
    private static final String SQL_QUERY_SELECT_ROLES_LIST_FOR_PROFILE = " SELECT role_key FROM profile_role WHERE profile_key = ? ORDER BY role_key ASC ";
    private static final String SQL_QUERY_SELECT_PROFILE_ROLE = " SELECT profile_key, role_key FROM profile_role WHERE profile_key = ? AND role_key = ? ";
    private static final String SQL_QUERY_INSERT_PROFILE_ROLE = " INSERT INTO profile_role (profile_key, role_key) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE_PROFILE_ROLE = " DELETE FROM profile_role WHERE profile_key = ? AND role_key = ? ";
    private static final String SQL_QUERY_DELETE_ROLES = " DELETE FROM profile_role WHERE profile_key = ? ";
    private static final String SQL_QUERY_SELECT_USERS_LIST_FOR_PROFILE = " SELECT id_user FROM profile_user WHERE profile_key = ? ORDER BY id_user ASC ";
    private static final String SQL_QUERY_SELECT_PROFILE_USER = " SELECT profile_key, id_user FROM profile_user WHERE profile_key = ? AND id_user = ? ";
    private static final String SQL_QUERY_INSERT_PROFILE_USER = " INSERT INTO profile_user (profile_key, id_user) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE_PROFILE_USER = " DELETE FROM profile_user WHERE id_user = ? AND profile_key = ? ";
    private static final String SQL_QUERY_DELETE_USERS = " DELETE FROM profile_user WHERE profile_key = ? ";
    private static final String SQL_QUERY_DELETE_PROFILES_FROM_USER = " DELETE FROM profile_user WHERE id_user = ? ";
    private static final String SQL_QUERY_SELECT_PROFILE_USER_FROM_ID_USER_AND_PROFILE_KEY = " SELECT profile_key, id_user FROM profile_user WHERE id_user = ? AND profile_key = ? ";
    private static final String SQL_QUERY_SELECT_VIEW_FOR_PROFILE = " SELECT view_key FROM profile_view_profile WHERE profile_key = ? ";
    private static final String SQL_QUERY_DELETE_VIEW = " DELETE FROM profile_view_profile WHERE profile_key = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( Profile profile, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {
            daoUtil.setString( 1, profile.getKey( ) );
            daoUtil.setString( 2, profile.getDescription( ) );
    
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Profile load( String strProfileKey, Plugin plugin )
    {
        Profile profile = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.executeQuery( );
    
            if ( daoUtil.next( ) )
            {
                profile = new Profile( );
                profile.setKey( daoUtil.getString( 1 ) );
                profile.setDescription( daoUtil.getString( 2 ) );
            }
        }
        return profile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( String strProfileKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( Profile profile, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            daoUtil.setString( 1, profile.getDescription( ) );
            daoUtil.setString( 2, profile.getKey( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Profile> selectProfileList( Plugin plugin )
    {
        List<Profile> listProfiles = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                Profile profile = new Profile( );
                profile.setKey( daoUtil.getString( 1 ) );
                profile.setDescription( daoUtil.getString( 2 ) );
    
                listProfiles.add( profile );
            }
        }
        return listProfiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Profile> selectProfilesByFilter( ProfileFilter pFilter, Plugin plugin )
    {
        List<Profile> listFilteredProfiles = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PROFILES_FROM_SEARCH, plugin ) )
        {
            daoUtil.setString( 1, ProfilesConstants.PERCENT + pFilter.getKey( ) + ProfilesConstants.PERCENT );
            daoUtil.setString( 2, ProfilesConstants.PERCENT + pFilter.getDescription( ) + ProfilesConstants.PERCENT );
    
            daoUtil.executeQuery( );
    
            while ( daoUtil.next( ) )
            {
                Profile profile = new Profile( );
                profile.setKey( daoUtil.getString( 1 ) );
                profile.setDescription( daoUtil.getString( 2 ) );
    
                listFilteredProfiles.add( profile );
            }
        }
        return listFilteredProfiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkExistProfile( String strProfileKey, Plugin plugin )
    {
        boolean bResult = false;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.executeQuery( );
           
            bResult =  daoUtil.next( ) ;
        }
        return bResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getProfileList( Plugin plugin )
    {
        ReferenceList listProfiles = new ReferenceList( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
    
            while ( daoUtil.next( ) )
            {
                Profile profile = new Profile( );
                profile.setKey( daoUtil.getString( 1 ) );
                profile.setDescription( daoUtil.getString( 2 ) );
    
                listProfiles.addItem( profile.getKey( ), profile.getKey( ) );
            }
        }
        return listProfiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkProfileAttributed( String strProfileKey, Plugin plugin )
    {
        boolean bInUse = false;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECK_PROFILE_ATTRIBUTED, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.executeQuery( );
    
            bInUse = daoUtil.next( );
        }
        return bInUse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Profile> selectProfileByIdUser( int nIdUser, Plugin plugin )
    {
        List<Profile> listProfiles = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PROFILE_FROM_ID_USER, plugin ) )
        {
            daoUtil.setInt( 1, nIdUser );
            daoUtil.executeQuery( );
    
            while ( daoUtil.next( ) )
            {
                Profile profile = new Profile( );
                profile.setKey( daoUtil.getString( 1 ) );
                profile.setDescription( daoUtil.getString( 2 ) );
                listProfiles.add( profile );
            }
        }
        return listProfiles;
    }

    /* RIGHTS */

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Right> selectRightsListForProfile( String strProfileKey, Plugin plugin )
    {
        List<Right> listRights = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_RIGHTS_LIST_FOR_PROFILE, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                Right right = new Right( );
                right.setId( daoUtil.getString( 1 ) );
    
                listRights.add( right );
            }
        }
        return listRights;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasRight( String strProfileKey, String strIdRight, Plugin plugin )
    {
        boolean bHasRight = false;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PROFILE_RIGHT, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.setString( 2, strIdRight );
            daoUtil.executeQuery( );

            bHasRight = daoUtil.next( );
        }
        return bHasRight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertRightForProfile( String strProfileKey, String strIdRight, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_PROFILE_RIGHT, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.setString( 2, strIdRight );
    
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRightFromProfile( String strProfileKey, String strIdRight, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_PROFILE_RIGHT, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.setString( 2, strIdRight );
    
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRights( String strProfileKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_RIGHTS, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.executeUpdate( );
        }
    }

    /* WORKGROUPS */

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AdminWorkgroup> selectWorkgroupsListForProfile( String strProfileKey, Plugin plugin )
    {
        List<AdminWorkgroup> listWorkgroups = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_WORKGROUPS_LIST_FOR_PROFILE, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
    
            daoUtil.executeQuery( );
    
            while ( daoUtil.next( ) )
            {
                AdminWorkgroup workgroup = new AdminWorkgroup( );
                workgroup.setKey( daoUtil.getString( 1 ) );
    
                listWorkgroups.add( workgroup );
            }
        }
        return listWorkgroups;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasWorkgroup( String strProfileKey, String strWorkgroupKey, Plugin plugin )
    {
        boolean bHasWorkgroup = false;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PROFILE_WORKGROUP, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.setString( 2, strWorkgroupKey );
            daoUtil.executeQuery( );
    
            bHasWorkgroup = daoUtil.next( );
        }
        return bHasWorkgroup;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertWorkgroupForProfile( String strProfileKey, String strWorkgroupKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_PROFILE_WORKGROUP, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.setString( 2, strWorkgroupKey );
    
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteWorkgroupFromProfile( String strProfileKey, String strWorkgroupKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_PROFILE_WORKGROUP, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.setString( 2, strWorkgroupKey );
    
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteWorkgroups( String strProfileKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_WORKGROUPS, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
    
            daoUtil.executeUpdate( );
        }
    }

    /* ROLES */

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RBACRole> selectRolesListForProfile( String strProfileKey, Plugin plugin )
    {
        List<RBACRole> listRoles = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ROLES_LIST_FOR_PROFILE, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
    
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                RBACRole role = new RBACRole( );
                role.setKey( daoUtil.getString( 1 ) );
    
                listRoles.add( role );
            }
        }
        return listRoles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasRole( String strProfileKey, String strRoleKey, Plugin plugin )
    {
        boolean bHasRole = false;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PROFILE_ROLE, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.setString( 2, strRoleKey );
            daoUtil.executeQuery( );
    
            bHasRole = daoUtil.next( );
        }
        return bHasRole;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertRoleForProfile( String strProfileKey, String strRoleKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_PROFILE_ROLE, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.setString( 2, strRoleKey );
    
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRoleFromProfile( String strProfileKey, String strRoleKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_PROFILE_ROLE, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.setString( 2, strRoleKey );
    
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRoles( String strProfileKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ROLES, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.executeUpdate( );
        }
    }

    /* USERS */

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AdminUser> selectUsersListForProfile( String strProfileKey, Plugin plugin )
    {
        List<AdminUser> listUsers = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USERS_LIST_FOR_PROFILE, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.executeQuery( );
    
            while ( daoUtil.next( ) )
            {
                AdminUser user = new AdminUser( );
                user.setUserId( daoUtil.getInt( 1 ) );
    
                listUsers.add( user );
            }
        }
        return listUsers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasUser( String strProfileKey, int nIdUser, Plugin plugin )
    {
        boolean bHasUser = false;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PROFILE_USER, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.setInt( 2, nIdUser );
            daoUtil.executeQuery( );
            bHasUser = daoUtil.next( );
        }
        return bHasUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertUserForProfile( String strProfileKey, int nIdUser, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_PROFILE_USER, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.setInt( 2, nIdUser );
    
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUserFromProfile( String strProfileKey, int nIdUser, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_PROFILE_USER, plugin ) )
        {
            daoUtil.setInt( 1, nIdUser );
            daoUtil.setString( 2, strProfileKey );
    
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUsers( String strProfileKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_USERS, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteProfilesFromUser( int nIdUser, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_PROFILES_FROM_USER, plugin ) )
        {
            daoUtil.setInt( 1, nIdUser );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasProfile( String strProfileKey, int nIdUser, Plugin plugin )
    {
        boolean bHasProfile = false;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PROFILE_USER_FROM_ID_USER_AND_PROFILE_KEY, plugin ) )
        {
            daoUtil.setInt( 1, nIdUser );
            daoUtil.setString( 2, strProfileKey );
            daoUtil.executeQuery( );
            bHasProfile = daoUtil.next( );
        }
        return bHasProfile;
    }

    /* VIEW */

    /**
     * {@inheritDoc}
     */
    @Override
    public View selectViewForProfile( String strProfileKey, Plugin plugin )
    {
        View view = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_VIEW_FOR_PROFILE, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.executeQuery( );
    
            if ( daoUtil.next( ) )
            {
                view = new View( );
                view.setKey( daoUtil.getString( 1 ) );
            }
        }
        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteView( String strProfileKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_VIEW, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.executeUpdate( );
        }
    }
}
