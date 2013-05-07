/*
 * Copyright (c) 2002-2013, Mairie de Paris
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

import fr.paris.lutece.plugins.profiles.business.Profile;
import fr.paris.lutece.plugins.profiles.business.views.View;
import fr.paris.lutece.plugins.profiles.business.views.ViewAction;
import fr.paris.lutece.plugins.profiles.business.views.ViewFilter;
import fr.paris.lutece.portal.business.dashboard.DashboardFilter;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.IDashboardComponent;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.ItemNavigator;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 *
 * IViewsService
 *
 */
public interface IViewsService
{
    /**
    * Get the item navigator
    * @param vFilter the view filter
    * @param view the view
    * @param url the url
    * @return the item navigator
    */
    ItemNavigator getItemNavigator( ViewFilter vFilter, View view, UrlItem url );

    /**
    * Get the list of actions
    * @param user the current user
    * @param view the view
    * @param strPermission the permission name
    * @param locale Locale
    * @param plugin Plugin
    * @return the list of actions
    */
    List<ViewAction> getListActions( AdminUser user, View view, String strPermission, Locale locale, Plugin plugin );

    /**
     * Get the list of dashboards of the given view
     * @param strViewKey the view key
     * @param user the current user
     * @param plugin Plugin
     * @return the list of dashboards
     */
    Map<String, List<IDashboardComponent>> getAllSetDashboards( String strViewKey, AdminUser user, Plugin plugin );

    /**
     * Get the list of dashboards that are not set
     * @param strViewKey the view key
     * @param user the admin user
     * @param plugin Plugin
     * @return the list of dashboards
     */
    List<IDashboardComponent> getNotSetDashboards( String strViewKey, AdminUser user, Plugin plugin );

    /**
     * Builds all refList order for all columns
     * @param plugin Plugin
     * @return the map with column id as key
     */
    Map<String, ReferenceList> getMapAvailableOrders( Plugin plugin );

    /**
     * Orders reference list for the given column
     * @param nColumn column
     * @param plugin Plugin
     * @return the refList
     */
    ReferenceList getListAvailableOrders( int nColumn, Plugin plugin );

    /**
     * Returns list with available column
     * @return all available columns
     */
    ReferenceList getListAvailableColumns(  );

    /**
     * Moves the dashboard.
     * @param dashboard to move, with new values
     * @param nOldColumn previous column id
     * @param nOldOrder previous order
     * @param bCreate <code>true</code> if this is a new dashboard, <code>false</code> otherwise.
     * @param strViewKey the view key
     * @param plugin the plugin
     */
    void doMoveDashboard( IDashboardComponent dashboard, int nOldColumn, int nOldOrder, boolean bCreate,
        String strViewKey, Plugin plugin );

    /**
     * Get the dashboard component
     * @param strViewKey the view key
     * @param nColumn the column id
     * @param plugin the plugins
     * @return all dashboards for this column
     */
    List<IDashboardComponent> getDashboardComponents( String strViewKey, int nColumn, Plugin plugin );

    /**
     * Reorders column's dashboard
     * @param strViewKey the view key
     * @param nColumn the column to reorder
     * @param plugin the plugin
     */
    void doReorderColumn( String strViewKey, int nColumn, Plugin plugin );

    /**
     * Builds the map to with column id as key, and <code>true</code> as value if column is well ordered, <code>false</code> otherwise.
     * @param strViewKey the view key
     * @param plugin the plugin
     * @return the map
     */
    Map<String, Boolean> getOrderedColumnsStatus( String strViewKey, Plugin plugin );

    /**
     * Creation of an instance of View
     * @param view The instance of the View which contains the informations to store
     * @param plugin Plugin
     * @return The instance of View which has been created with its primary key.
     */
    View create( View view, Plugin plugin );

    /**
     * Update of the view which is specified in parameter
     * @param view The instance of the view which contains the new data to store
     * @param plugin Plugin
     * @return The instance of the view which has been updated
     */
    View update( View view, Plugin plugin );

    /**
     * Remove the View whose identifier is specified in parameter
     * @param strViewKey The View object to remove
     * @param plugin Plugin
     */
    void remove( String strViewKey, Plugin plugin );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a profile whose identifier is specified in parameter
     * @param strViewKey The key of the View
     * @param plugin Plugin
     * @return An instance of View
     */
    View findByPrimaryKey( String strViewKey, Plugin plugin );

    /**
     * Returns a List of Views objects
     * @param plugin Plugin
     * @return A List of Views
     */
    List<View> findAll( Plugin plugin );

    /**
     * Find Views by filter
     * @param vFilter the Filter
     * @param plugin Plugin
     * @return List of Views
     */
    List<View> findViewsByFilter( ViewFilter vFilter, Plugin plugin );

    /**
     * Check if a view already exists or not
     * @param strViewKey The view key
     * @param plugin Plugin
     * @return true if it already exists, false otherwise
     */
    boolean checkExistView( String strViewKey, Plugin plugin );

    /**
     * Get the list of Views
     * @param plugin Plugin
     * @return the list of Views
     */
    ReferenceList getViewsList( Plugin plugin );

    /* PROFILES */

    /**
     * Get the list of profiles associated to the view
     * @param strViewKey The view Key
     * @param plugin Plugin
     * @return The list of profile
     */
    List<Profile> getProfilesListForView( String strViewKey, Plugin plugin );

    /**
     * Get the view from a profile
     * @param strProfileKey the profile key
     * @param plugin Plugin
     * @return the view associated to the profile
     */
    View findViewForProfile( String strProfileKey, Plugin plugin );

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
    void addProfileForView( String strViewKey, String strProfileKey, Plugin plugin );

    /**
     * Remove a profile from a view
     * @param strViewKey The view Key
     * @param plugin Plugin
     */
    void removeProfiles( String strViewKey, Plugin plugin );

    /**
     * Remove a view from a profile
     * @param strViewKey the view key
     * @param strProfileKey the profile key
     * @param plugin Plugin
     */
    void removeProfileFromView( String strViewKey, String strProfileKey, Plugin plugin );

    /* DASHBOARDS */

    /**
     * Load the list of dashboards from a given view key
     * @param strViewKey the view key
     * @param plugin Plugin
     * @return a list of {@link IDashboardComponent}
     */
    List<IDashboardComponent> findDashboards( String strViewKey, Plugin plugin );

    /**
     * Load the dashboard
     * @param strDashboardName the dashboard name
     * @param strViewKey the view key
     * @param plugin Plugin
     * @return the dashboard
     */
    IDashboardComponent findDashboard( String strDashboardName, String strViewKey, Plugin plugin );

    /**
     * Insert a dashboard for a view
     * @param strViewKey the view key
     * @param dashboard the dashboard
     * @param plugin Plugin
     */
    void createDashboard( String strViewKey, IDashboardComponent dashboard, Plugin plugin );

    /**
     * Delete all dashboards of a view
     * @param strViewKey the view key
     * @param plugin Plugin
     */
    void removeDashboards( String strViewKey, Plugin plugin );

    /**
     * Delete a dashboard of a view
     * @param strViewKey the view key
     * @param strDashboardName the dashboard name
     * @param plugin Plugin
     */
    void removeDashboard( String strViewKey, String strDashboardName, Plugin plugin );

    /**
     * Update a dashboard
     * @param strViewKey the view key
     * @param dashboard the dashboard
     * @param plugin Plugin
     */
    void updateDashboard( String strViewKey, IDashboardComponent dashboard, Plugin plugin );

    /**
     * Loads the data of all the IDashboardComponent
     * @param filter the filter
     * @param strViewKey the view key
     * @param plugin Plugin
     * @return the list which contains the data of all the IDashboardComponent
     */
    List<IDashboardComponent> findDashboardsByFilter( DashboardFilter filter, String strViewKey, Plugin plugin );

    /**
     * Finds the max order for all columns.
     * @param plugin Plugin
     * @return the max order
     */
    int findMaxOrder( Plugin plugin );

    /**
     * Finds the max order for the column.
     * @param nColumn the column
     * @param plugin Plugin
     * @return the max order
     */
    int findMaxOrder( int nColumn, Plugin plugin );

    /**
     * Finds all columns
     * @param plugin Plugin
     * @return the list of columns
     */
    List<Integer> findColumns( Plugin plugin );
}
