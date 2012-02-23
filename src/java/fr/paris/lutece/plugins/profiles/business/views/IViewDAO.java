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
package fr.paris.lutece.plugins.profiles.business.views;

import fr.paris.lutece.plugins.profiles.business.Profile;
import fr.paris.lutece.portal.business.dashboard.DashboardFilter;
import fr.paris.lutece.portal.service.dashboard.IDashboardComponent;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;


/**
 *
 * IViewDAO
 *
 */
public interface IViewDAO
{
    /**
     * Delete a view from the table
     * @param strViewKey The view key
     * @param plugin Plugin
     */
    void delete( String strViewKey, Plugin plugin );

    /**
     * Insert a new record in the table.
     * @param view The view object
     * @param plugin Plugin
     */
    void insert( View view, Plugin plugin );

    /**
     * Load the data of view from the table
     * @param strViewKey  The view key
     * @param plugin Plugin
     * @return the instance of the view
     */
    View load( String strViewKey, Plugin plugin );

    /**
     * Load the list of views
     * @param plugin Plugin
     * @return The List of the views
     */
    List<View> selectViewsList( Plugin plugin );

    /**
     * Update the record identified by the given view key with the given view in the table
     * @param view The reference of view to be the new one
     * @param plugin Plugin
     */
    void store( View view, Plugin plugin );

    /**
     * Find view by filter
     * @param vFilter the Filter
     * @param plugin Plugin
     * @return List of views
     */
    List<View> selectViewsByFilter( ViewFilter vFilter, Plugin plugin );

    /**
    * Check if a view already exists or not
    * @param strKey The view key
    * @param plugin Plugin
    * @return true if it already exists, false otherwise
    */
    boolean checkExistView( String strKey, Plugin plugin );

    /**
    * Get the list of views
    * @param plugin Plugin
    * @return the list of views
    */
    ReferenceList getViewsList( Plugin plugin );

    /* PROFILES */

    /**
     * Get the list of profiles associated to the view
     * @param strViewKey The view Key
     * @param plugin Plugin
     * @return The list of users
     */
    List<Profile> selectProfilesListForView( String strViewKey, Plugin plugin );

    /**
    * Get the view from a profile
    * @param strProfileKey the profile key
    * @param plugin Plugin
    * @return the view associated to the profile
    */
    View selectViewForProfile( String strProfileKey, Plugin plugin );

    /**
    * Check if the given profile has a view or not
    * @param strProfileKey the profile key
    * @param plugin Plugin
    * @return true if the profile has the view, false otherwise
    */
    boolean hasView( String strProfileKey, Plugin plugin );

    /**
     * Add a profile for a view
     * @param strViewKey The view Key
     * @param strProfileKey The profile Key
     * @param plugin Plugin
     */
    void insertProfileForView( String strViewKey, String strProfileKey, Plugin plugin );

    /**
     * Remove a profile from a view
     * @param strViewKey The view Key
     * @param plugin Plugin
     */
    void deleteProfiles( String strViewKey, Plugin plugin );

    /**
     * Remove profile from a view
     * @param strViewKey the view key
     * @param strProfileKey the profile key
     * @param plugin Plugin
     */
    void deleteProfileFromView( String strViewKey, String strProfileKey, Plugin plugin );

    /* DASHBOARDS */

    /**
     * Load the list of dashboards from a given view key
     * @param strViewKey the view key
     * @param plugin Plugin
     * @return a list of {@link IDashboardComponent}
     */
    List<IDashboardComponent> selectDashboards( String strViewKey, Plugin plugin );

    /**
     * Load the dashboard
     * @param strDashboardName the dashboard name
     * @param strViewKey the view key
     * @param plugin Plugin
     * @return the dashboard
     */
    IDashboardComponent selectDashboard( String strDashboardName, String strViewKey, Plugin plugin );

    /**
     * Insert a dashboard for a view
     * @param strViewKey the view key
     * @param dashboard the dashboard
     * @param plugin Plugin
     */
    void insertDashboard( String strViewKey, IDashboardComponent dashboard, Plugin plugin );

    /**
     * Delete all dashboards of a view
     * @param strViewKey the view key
     * @param plugin Plugin
     */
    void deleteDashboards( String strViewKey, Plugin plugin );

    /**
     * Delete a dashboard of a view
     * @param strViewKey the view key
     * @param strDashboardName the dashboard name
     * @param plugin Plugin
     */
    void deleteDashboard( String strViewKey, String strDashboardName, Plugin plugin );

    /**
     * Update a dashboard
     * @param strViewKey the view key
     * @param dashboard the dashboard
     * @param plugin Plugin
     */
    void storeDashboard( String strViewKey, IDashboardComponent dashboard, Plugin plugin );

    /**
     * Returns the max order value, for all columns
     * @param plugin Plugin
     * @return the max order
     */
    int selectMaxOrder( Plugin plugin );

    /**
     * Returns the max order value, for the given column
     * @param nColumn the column
     * @param plugin Plugin
     * @return the max order
     */
    int selectMaxOrder( int nColumn, Plugin plugin );

    /**
     * Returns the columns list
     * @param plugin Plugin
     * @return the columns list
     */
    List<Integer> selectColumns( Plugin plugin );

    /**
     * Finds all dashboard components matching filter
     * @param filter the filter
     * @param strViewKey the view key
     * @param plugin Plugin
     * @return all dashboard components matching filter
     */
    List<IDashboardComponent> selectDashboardsByFilter( DashboardFilter filter, String strViewKey, Plugin plugin );
}
