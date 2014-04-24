/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
import fr.paris.lutece.plugins.profiles.business.views.ViewHome;
import fr.paris.lutece.plugins.profiles.service.ProfilesPlugin;
import fr.paris.lutece.plugins.profiles.service.action.IViewActionService;
import fr.paris.lutece.plugins.profiles.utils.constants.ProfilesConstants;
import fr.paris.lutece.portal.business.dashboard.DashboardFilter;
import fr.paris.lutece.portal.business.dashboard.DashboardHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.DashboardService;
import fr.paris.lutece.portal.service.dashboard.IDashboardComponent;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.ItemNavigator;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;


/**
 *
 * ViewService
 *
 */
public class ViewsService implements IViewsService
{
    @Inject
    private IViewActionService _viewActionService;

    /**
    * {@inheritDoc}
    */
    @Override
    public ItemNavigator getItemNavigator( ViewFilter vFilter, View view, UrlItem url )
    {
        Plugin plugin = PluginService.getPlugin( ProfilesPlugin.PLUGIN_NAME );
        List<String> listItem = new ArrayList<String>(  );
        Collection<View> listAllViews = ViewHome.findViewsByFilter( vFilter, plugin );
        int nIndex = 0;
        int nCurrentItemId = 0;

        for ( View allView : listAllViews )
        {
            listItem.add( allView.getKey(  ) );

            if ( allView.getKey(  ).equals( view.getKey(  ) ) )
            {
                nCurrentItemId = nIndex;
            }

            nIndex++;
        }

        return new ItemNavigator( listItem, nCurrentItemId, url.getUrl(  ), ProfilesConstants.PARAMETER_VIEW_KEY );
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public List<ViewAction> getListActions( AdminUser user, View view, String strPermission, Locale locale,
        Plugin plugin )
    {
        List<ViewAction> listActions = new ArrayList<ViewAction>(  );

        for ( ViewAction action : _viewActionService.selectActionsList( locale, plugin ) )
        {
            if ( !action.getPermission(  ).equals( strPermission ) )
            {
                listActions.add( action );
            }
        }

        listActions = (List<ViewAction>) RBACService.getAuthorizedActionsCollection( listActions, view, user );

        return listActions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<IDashboardComponent>> getAllSetDashboards( String strViewKey, AdminUser user, Plugin plugin )
    {
        Map<String, List<IDashboardComponent>> mapDashboardComponents = new HashMap<String, List<IDashboardComponent>>(  );

        // Personnalized dashboard positions
        List<IDashboardComponent> listDashboards = ViewHome.findDashboards( strViewKey, plugin );

        for ( IDashboardComponent dashboard : listDashboards )
        {
            int nColumn = dashboard.getZone(  );
            boolean bRight = user.checkRight( dashboard.getRight(  ) ) ||
                dashboard.getRight(  ).equalsIgnoreCase( ProfilesConstants.ALL );

            if ( !bRight )
            {
                continue;
            }

            String strColumn = Integer.toString( nColumn );

            // find this column list
            List<IDashboardComponent> listDashboardsColumn = mapDashboardComponents.get( strColumn );

            if ( listDashboardsColumn == null )
            {
                // the list does not exist, create it
                listDashboardsColumn = new ArrayList<IDashboardComponent>(  );
                mapDashboardComponents.put( strColumn, listDashboardsColumn );
            }

            // add dashboard to the list
            listDashboardsColumn.add( dashboard );
        }

        return mapDashboardComponents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IDashboardComponent> getNotSetDashboards( String strViewKey, AdminUser user, Plugin plugin )
    {
        List<IDashboardComponent> listDashboards = DashboardHome.findAll(  );
        List<IDashboardComponent> listPersonnalizedDashboards = ViewHome.findDashboards( strViewKey, plugin );
        List<IDashboardComponent> listNotSetDashboards = new ArrayList<IDashboardComponent>(  );

        for ( IDashboardComponent dashboard : listDashboards )
        {
            boolean bRight = user.checkRight( dashboard.getRight(  ) ) ||
                dashboard.getRight(  ).equalsIgnoreCase( ProfilesConstants.ALL );

            if ( !listPersonnalizedDashboards.contains( dashboard ) && bRight &&
                    ( dashboard.getZone(  ) <= DashboardService.getInstance(  ).getColumnCount(  ) ) )
            {
                listNotSetDashboards.add( dashboard );
            }
        }

        return listNotSetDashboards;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, ReferenceList> getMapAvailableOrders( Plugin plugin )
    {
        Map<String, ReferenceList> mapAvailableOrders = new HashMap<String, ReferenceList>(  );

        // get columns
        for ( Integer nColumn : ViewHome.findColumns( plugin ) )
        {
            // get orders
            mapAvailableOrders.put( nColumn.toString(  ), getListAvailableOrders( nColumn, plugin ) );
        }

        return mapAvailableOrders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getListAvailableOrders( int nColumn, Plugin plugin )
    {
        ReferenceList refList = new ReferenceList(  );

        // add empty item
        refList.addItem( StringUtils.EMPTY, StringUtils.EMPTY );

        int nMaxOrder = ViewHome.findMaxOrder( nColumn, plugin );

        for ( int nOrder = 1; nOrder <= nMaxOrder; nOrder++ )
        {
            refList.addItem( nOrder, Integer.toString( nOrder ) );
        }

        return refList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getListAvailableColumns(  )
    {
        ReferenceList refList = new ReferenceList(  );

        // add empty item
        refList.addItem( StringUtils.EMPTY, StringUtils.EMPTY );

        for ( int nColumnIndex = 1; nColumnIndex <= DashboardService.getInstance(  ).getColumnCount(  );
                nColumnIndex++ )
        {
            refList.addItem( nColumnIndex, Integer.toString( nColumnIndex ) );
        }

        return refList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doMoveDashboard( IDashboardComponent dashboard, int nOldColumn, int nOldOrder, boolean bCreate,
        String strViewKey, Plugin plugin )
    {
        int nColumn = dashboard.getZone(  );
        int nOrder = dashboard.getOrder(  );

        // find the dashboard already with this order and column
        DashboardFilter filter = new DashboardFilter(  );
        filter.setFilterColumn( nColumn );

        List<IDashboardComponent> listColumnDashboards = ViewHome.findDashboardsByFilter( filter, strViewKey, plugin );

        if ( ( listColumnDashboards != null ) && !listColumnDashboards.isEmpty(  ) )
        {
            if ( AppLogService.isDebugEnabled(  ) )
            {
                AppLogService.debug( "Reordering  dashboard column " + dashboard.getZone(  ) );
            }

            // sort by order
            Collections.sort( listColumnDashboards );

            int nMaxOrder = listColumnDashboards.get( listColumnDashboards.size(  ) - 1 ).getOrder(  );

            if ( ( nOldColumn == 0 ) || ( nOldColumn != nColumn ) )
            {
                // was not in this column before, put to the end
                dashboard.setOrder( nMaxOrder + 1 );
            }
            else
            {
                if ( nOrder < nOldOrder )
                {
                    for ( IDashboardComponent dc : listColumnDashboards )
                    {
                        if ( !dc.equals( dashboard ) )
                        {
                            int nCurrentOrder = dc.getOrder(  );

                            if ( ( nCurrentOrder >= nOrder ) && ( nCurrentOrder < nOldOrder ) )
                            {
                                dc.setOrder( nCurrentOrder + 1 );
                                ViewHome.updateDashboard( strViewKey, dc, plugin );
                            }
                        }
                    }
                }
                else if ( nOrder > nOldOrder )
                {
                    for ( IDashboardComponent dc : listColumnDashboards )
                    {
                        if ( !dc.equals( dashboard ) )
                        {
                            int nCurrentOrder = dc.getOrder(  );

                            if ( ( nCurrentOrder <= nOrder ) && ( nCurrentOrder > nOldOrder ) )
                            {
                                dc.setOrder( nCurrentOrder - 1 );
                                ViewHome.updateDashboard( strViewKey, dc, plugin );
                            }
                        }
                    }
                }

                // dashboard are singletons, values are modified by getting it from database
                dashboard.setOrder( nOrder );
                dashboard.setZone( nColumn );
            }
        }
        else
        {
            dashboard.setOrder( 1 );
        }

        if ( bCreate )
        {
            // create dashboard
            ViewHome.createDashboard( strViewKey, dashboard, plugin );
        }
        else
        {
            // update dashboard
            ViewHome.updateDashboard( strViewKey, dashboard, plugin );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IDashboardComponent> getDashboardComponents( String strViewKey, int nColumn, Plugin plugin )
    {
        DashboardFilter filter = new DashboardFilter(  );
        filter.setFilterColumn( nColumn );

        List<IDashboardComponent> dashboardComponents = ViewHome.findDashboardsByFilter( filter, strViewKey, plugin );

        return dashboardComponents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doReorderColumn( String strViewKey, int nColumn, Plugin plugin )
    {
        int nOrder = ProfilesConstants.CONSTANTE_FIRST_ORDER;

        for ( IDashboardComponent dc : getDashboardComponents( strViewKey, nColumn, plugin ) )
        {
            dc.setOrder( nOrder++ );
            ViewHome.updateDashboard( strViewKey, dc, plugin );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Boolean> getOrderedColumnsStatus( String strViewKey, Plugin plugin )
    {
        Map<String, Boolean> mapOrderedStatus = new HashMap<String, Boolean>(  );
        List<Integer> listColumns = ViewHome.findColumns( plugin );

        for ( Integer nIdColumn : listColumns )
        {
            mapOrderedStatus.put( nIdColumn.toString(  ), isWellOrdered( strViewKey, nIdColumn, plugin ) );
        }

        return mapOrderedStatus;
    }

    /**
     * Creation of an instance of View
     * @param view The instance of the View which contains the informations to store
     * @param plugin Plugin
     * @return The instance of View which has been created with its primary key.
     */
    @Override
    public View create( View view, Plugin plugin )
    {
        if ( view != null )
        {
            ViewHome.create( view, plugin );
        }

        return view;
    }

    /**
     * Update of the view which is specified in parameter
     * @param view The instance of the view which contains the new data to store
     * @param plugin Plugin
     * @return The instance of the view which has been updated
     */
    @Override
    public View update( View view, Plugin plugin )
    {
        if ( view != null )
        {
            ViewHome.update( view, plugin );
        }

        return view;
    }

    /**
     * Remove the View whose identifier is specified in parameter
     * @param strViewKey The View object to remove
     * @param plugin Plugin
     */
    @Override
    public void remove( String strViewKey, Plugin plugin )
    {
        ViewHome.remove( strViewKey, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a profile whose identifier is specified in parameter
     * @param strViewKey The key of the View
     * @param plugin Plugin
     * @return An instance of View
     */
    @Override
    public View findByPrimaryKey( String strViewKey, Plugin plugin )
    {
        return ViewHome.findByPrimaryKey( strViewKey, plugin );
    }

    /**
     * Returns a List of Views objects
     * @param plugin Plugin
     * @return A List of Views
     */
    @Override
    public List<View> findAll( Plugin plugin )
    {
        return ViewHome.findAll( plugin );
    }

    /**
     * Find Views by filter
     * @param vFilter the Filter
     * @param plugin Plugin
     * @return List of Views
     */
    @Override
    public List<View> findViewsByFilter( ViewFilter vFilter, Plugin plugin )
    {
        return ViewHome.findViewsByFilter( vFilter, plugin );
    }

    /**
     * Check if a view already exists or not
     * @param strViewKey The view key
     * @param plugin Plugin
     * @return true if it already exists, false otherwise
     */
    @Override
    public boolean checkExistView( String strViewKey, Plugin plugin )
    {
        return ViewHome.checkExistView( strViewKey, plugin );
    }

    /**
     * Get the list of Views
     * @param plugin Plugin
     * @return the list of Views
     */
    @Override
    public ReferenceList getViewsList( Plugin plugin )
    {
        return ViewHome.getViewsList( plugin );
    }

    /* PROFILES */

    /**
     * Get the list of profiles associated to the view
     * @param strViewKey The view Key
     * @param plugin Plugin
     * @return The list of profile
     */
    @Override
    public List<Profile> getProfilesListForView( String strViewKey, Plugin plugin )
    {
        return ViewHome.getProfilesListForView( strViewKey, plugin );
    }

    /**
     * Get the view from a profile
     * @param strProfileKey the profile key
     * @param plugin Plugin
     * @return the view associated to the profile
     */
    @Override
    public View findViewForProfile( String strProfileKey, Plugin plugin )
    {
        return ViewHome.findViewForProfile( strProfileKey, plugin );
    }

    /**
     * Check if the given profile has a view or not
     * @param strProfileKey the profile key
     * @param plugin Plugin
     * @return true if the profile has the view, false otherwise
     */
    @Override
    public boolean hasView( String strProfileKey, Plugin plugin )
    {
        return ViewHome.hasView( strProfileKey, plugin );
    }

    /**
     * Add a profile for a view
     * @param strViewKey The view Key
     * @param strProfileKey The profile Key
     * @param plugin Plugin
     */
    @Override
    public void addProfileForView( String strViewKey, String strProfileKey, Plugin plugin )
    {
        ViewHome.addProfileForView( strViewKey, strProfileKey, plugin );
    }

    /**
     * Remove a profile from a view
     * @param strViewKey The view Key
     * @param plugin Plugin
     */
    @Override
    public void removeProfiles( String strViewKey, Plugin plugin )
    {
        ViewHome.removeProfiles( strViewKey, plugin );
    }

    /**
     * Remove a view from a profile
     * @param strViewKey the view key
     * @param strProfileKey the profile key
     * @param plugin Plugin
     */
    @Override
    public void removeProfileFromView( String strViewKey, String strProfileKey, Plugin plugin )
    {
        ViewHome.removeProfileFromView( strViewKey, strProfileKey, plugin );
    }

    /* DASHBOARDS */

    /**
     * Load the list of dashboards from a given view key
     * @param strViewKey the view key
     * @param plugin Plugin
     * @return a list of {@link IDashboardComponent}
     */
    @Override
    public List<IDashboardComponent> findDashboards( String strViewKey, Plugin plugin )
    {
        return ViewHome.findDashboards( strViewKey, plugin );
    }

    /**
     * Load the dashboard
     * @param strDashboardName the dashboard name
     * @param strViewKey the view key
     * @param plugin Plugin
     * @return the dashboard
     */
    @Override
    public IDashboardComponent findDashboard( String strDashboardName, String strViewKey, Plugin plugin )
    {
        return ViewHome.findDashboard( strDashboardName, strViewKey, plugin );
    }

    /**
     * Insert a dashboard for a view
     * @param strViewKey the view key
     * @param dashboard the dashboard
     * @param plugin Plugin
     */
    @Override
    public void createDashboard( String strViewKey, IDashboardComponent dashboard, Plugin plugin )
    {
        ViewHome.createDashboard( strViewKey, dashboard, plugin );
    }

    /**
     * Delete all dashboards of a view
     * @param strViewKey the view key
     * @param plugin Plugin
     */
    @Override
    public void removeDashboards( String strViewKey, Plugin plugin )
    {
        ViewHome.removeDashboards( strViewKey, plugin );
    }

    /**
     * Delete a dashboard of a view
     * @param strViewKey the view key
     * @param strDashboardName the dashboard name
     * @param plugin Plugin
     */
    @Override
    public void removeDashboard( String strViewKey, String strDashboardName, Plugin plugin )
    {
        ViewHome.removeDashboard( strViewKey, strDashboardName, plugin );
    }

    /**
     * Update a dashboard
     * @param strViewKey the view key
     * @param dashboard the dashboard
     * @param plugin Plugin
     */
    @Override
    public void updateDashboard( String strViewKey, IDashboardComponent dashboard, Plugin plugin )
    {
        ViewHome.updateDashboard( strViewKey, dashboard, plugin );
    }

    /**
     * Loads the data of all the IDashboardComponent
     * @param filter the filter
     * @param strViewKey the view key
     * @param plugin Plugin
     * @return the list which contains the data of all the IDashboardComponent
     */
    public List<IDashboardComponent> findDashboardsByFilter( DashboardFilter filter, String strViewKey, Plugin plugin )
    {
        return ViewHome.findDashboardsByFilter( filter, strViewKey, plugin );
    }

    /**
     * Finds the max order for all columns.
     * @param plugin Plugin
     * @return the max order
     */
    @Override
    public int findMaxOrder( Plugin plugin )
    {
        return ViewHome.findMaxOrder( plugin );
    }

    /**
     * Finds the max order for the column.
     * @param nColumn the column
     * @param plugin Plugin
     * @return the max order
     */
    @Override
    public int findMaxOrder( int nColumn, Plugin plugin )
    {
        return ViewHome.findMaxOrder( nColumn, plugin );
    }

    /**
     * Finds all columns
     * @param plugin Plugin
     * @return the list of columns
     */
    @Override
    public List<Integer> findColumns( Plugin plugin )
    {
        return ViewHome.findColumns( plugin );
    }

    /**
     * Determines if the column is well ordered
     * @param strViewKey the view key
     * @param nColumn the column id
     * @param plugin the plugin
     * @return true if well ordered, <code>false</code> otherwise.
     */
    private boolean isWellOrdered( String strViewKey, int nColumn, Plugin plugin )
    {
        int nOrder = ProfilesConstants.CONSTANTE_FIRST_ORDER;

        for ( IDashboardComponent dc : getDashboardComponents( strViewKey, nColumn, plugin ) )
        {
            if ( nOrder != dc.getOrder(  ) )
            {
                return false;
            }

            nOrder++;
        }

        return true;
    }
}
