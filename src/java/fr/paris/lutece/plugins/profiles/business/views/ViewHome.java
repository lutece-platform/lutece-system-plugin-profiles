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
package fr.paris.lutece.plugins.profiles.business.views;

import fr.paris.lutece.plugins.profiles.business.Profile;
import fr.paris.lutece.portal.business.dashboard.DashboardFilter;
import fr.paris.lutece.portal.service.dashboard.IDashboardComponent;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;

/**
 *
 * ProfileHome
 *
 */
public final class ViewHome
{
    private static final String BEAN_VIEW_DAO = "profiles.viewDAO";
    private static IViewDAO _dao = SpringContextService.getBean( BEAN_VIEW_DAO );

    /**
     * Private constructor - this class need not be instantiated
     */
    private ViewHome( )
    {
    }

    /**
     * Creation of an instance of View
     * 
     * @param view
     *            The instance of the View which contains the informations to store
     * @param plugin
     *            Plugin
     * @return The instance of View which has been created with its primary key.
     */
    public static View create( View view, Plugin plugin )
    {
        _dao.insert( view, plugin );

        return view;
    }

    /**
     * Update of the view which is specified in parameter
     * 
     * @param view
     *            The instance of the view which contains the new data to store
     * @param plugin
     *            Plugin
     * @return The instance of the view which has been updated
     */
    public static View update( View view, Plugin plugin )
    {
        _dao.store( view, plugin );

        return view;
    }

    /**
     * Remove the View whose identifier is specified in parameter
     * 
     * @param strViewKey
     *            The View object to remove
     * @param plugin
     *            Plugin
     */
    public static void remove( String strViewKey, Plugin plugin )
    {
        _dao.delete( strViewKey, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a profile whose identifier is specified in parameter
     * 
     * @param strViewKey
     *            The key of the View
     * @param plugin
     *            Plugin
     * @return An instance of View
     */
    public static View findByPrimaryKey( String strViewKey, Plugin plugin )
    {
        return _dao.load( strViewKey, plugin );
    }

    /**
     * Returns a List of Views objects
     * 
     * @param plugin
     *            Plugin
     * @return A List of Views
     */
    public static List<View> findAll( Plugin plugin )
    {
        return _dao.selectViewsList( plugin );
    }

    /**
     * Find Views by filter
     * 
     * @param vFilter
     *            the Filter
     * @param plugin
     *            Plugin
     * @return List of Views
     */
    public static List<View> findViewsByFilter( ViewFilter vFilter, Plugin plugin )
    {
        return _dao.selectViewsByFilter( vFilter, plugin );
    }

    /**
     * Check if a view already exists or not
     * 
     * @param strViewKey
     *            The view key
     * @param plugin
     *            Plugin
     * @return true if it already exists, false otherwise
     */
    public static boolean checkExistView( String strViewKey, Plugin plugin )
    {
        return _dao.checkExistView( strViewKey, plugin );
    }

    /**
     * Get the list of Views
     * 
     * @param plugin
     *            Plugin
     * @return the list of Views
     */
    public static ReferenceList getViewsList( Plugin plugin )
    {
        return _dao.getViewsList( plugin );
    }

    /* PROFILES */

    /**
     * Get the list of profiles associated to the view
     * 
     * @param strViewKey
     *            The view Key
     * @param plugin
     *            Plugin
     * @return The list of profile
     */
    public static List<Profile> getProfilesListForView( String strViewKey, Plugin plugin )
    {
        return _dao.selectProfilesListForView( strViewKey, plugin );
    }

    /**
     * Get the view from a profile
     * 
     * @param strProfileKey
     *            the profile key
     * @param plugin
     *            Plugin
     * @return the view associated to the profile
     */
    public static View findViewForProfile( String strProfileKey, Plugin plugin )
    {
        return _dao.selectViewForProfile( strProfileKey, plugin );
    }

    /**
     * Check if the given profile has a view or not
     * 
     * @param strProfileKey
     *            the profile key
     * @param plugin
     *            Plugin
     * @return true if the profile has the view, false otherwise
     */
    public static boolean hasView( String strProfileKey, Plugin plugin )
    {
        return _dao.hasView( strProfileKey, plugin );
    }

    /**
     * Add a profile for a view
     * 
     * @param strViewKey
     *            The view Key
     * @param strProfileKey
     *            The profile Key
     * @param plugin
     *            Plugin
     */
    public static void addProfileForView( String strViewKey, String strProfileKey, Plugin plugin )
    {
        _dao.insertProfileForView( strViewKey, strProfileKey, plugin );
    }

    /**
     * Remove a profile from a view
     * 
     * @param strViewKey
     *            The view Key
     * @param plugin
     *            Plugin
     */
    public static void removeProfiles( String strViewKey, Plugin plugin )
    {
        _dao.deleteProfiles( strViewKey, plugin );
    }

    /**
     * Remove a view from a profile
     * 
     * @param strViewKey
     *            the view key
     * @param strProfileKey
     *            the profile key
     * @param plugin
     *            Plugin
     */
    public static void removeProfileFromView( String strViewKey, String strProfileKey, Plugin plugin )
    {
        _dao.deleteProfileFromView( strViewKey, strProfileKey, plugin );
    }

    /* DASHBOARDS */

    /**
     * Load the list of dashboards from a given view key
     * 
     * @param strViewKey
     *            the view key
     * @param plugin
     *            Plugin
     * @return a list of {@link IDashboardComponent}
     */
    public static List<IDashboardComponent> findDashboards( String strViewKey, Plugin plugin )
    {
        return _dao.selectDashboards( strViewKey, plugin );
    }

    /**
     * Load the dashboard
     * 
     * @param strDashboardName
     *            the dashboard name
     * @param strViewKey
     *            the view key
     * @param plugin
     *            Plugin
     * @return the dashboard
     */
    public static IDashboardComponent findDashboard( String strDashboardName, String strViewKey, Plugin plugin )
    {
        return _dao.selectDashboard( strDashboardName, strViewKey, plugin );
    }

    /**
     * Insert a dashboard for a view
     * 
     * @param strViewKey
     *            the view key
     * @param dashboard
     *            the dashboard
     * @param plugin
     *            Plugin
     */
    public static void createDashboard( String strViewKey, IDashboardComponent dashboard, Plugin plugin )
    {
        _dao.insertDashboard( strViewKey, dashboard, plugin );
    }

    /**
     * Delete all dashboards of a view
     * 
     * @param strViewKey
     *            the view key
     * @param plugin
     *            Plugin
     */
    public static void removeDashboards( String strViewKey, Plugin plugin )
    {
        _dao.deleteDashboards( strViewKey, plugin );
    }

    /**
     * Delete a dashboard of a view
     * 
     * @param strViewKey
     *            the view key
     * @param strDashboardName
     *            the dashboard name
     * @param plugin
     *            Plugin
     */
    public static void removeDashboard( String strViewKey, String strDashboardName, Plugin plugin )
    {
        _dao.deleteDashboard( strViewKey, strDashboardName, plugin );
    }

    /**
     * Update a dashboard
     * 
     * @param strViewKey
     *            the view key
     * @param dashboard
     *            the dashboard
     * @param plugin
     *            Plugin
     */
    public static void updateDashboard( String strViewKey, IDashboardComponent dashboard, Plugin plugin )
    {
        _dao.storeDashboard( strViewKey, dashboard, plugin );
    }

    /**
     * Loads the data of all the IDashboardComponent
     * 
     * @param filter
     *            the filter
     * @param strViewKey
     *            the view key
     * @param plugin
     *            Plugin
     * @return the list which contains the data of all the IDashboardComponent
     */
    public static List<IDashboardComponent> findDashboardsByFilter( DashboardFilter filter, String strViewKey, Plugin plugin )
    {
        return _dao.selectDashboardsByFilter( filter, strViewKey, plugin );
    }

    /**
     * Finds the max order for all columns.
     * 
     * @param plugin
     *            Plugin
     * @return the max order
     */
    public static int findMaxOrder( Plugin plugin )
    {
        return _dao.selectMaxOrder( plugin );
    }

    /**
     * Finds the max order for the column.
     * 
     * @param nColumn
     *            the column
     * @param plugin
     *            Plugin
     * @return the max order
     */
    public static int findMaxOrder( int nColumn, Plugin plugin )
    {
        return _dao.selectMaxOrder( nColumn, plugin );
    }

    /**
     * Finds all columns
     * 
     * @param plugin
     *            Plugin
     * @return the list of columns
     */
    public static List<Integer> findColumns( Plugin plugin )
    {
        return _dao.selectColumns( plugin );
    }
}
