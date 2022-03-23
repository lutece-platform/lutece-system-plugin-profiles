/*
 * Copyright (c) 2002-2021, City of Paris
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
import fr.paris.lutece.plugins.profiles.utils.constants.ProfilesConstants;
import fr.paris.lutece.portal.business.dashboard.DashboardFactory;
import fr.paris.lutece.portal.business.dashboard.DashboardFilter;
import fr.paris.lutece.portal.service.dashboard.IDashboardComponent;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ViewDAO
 * 
 */
public class ViewDAO implements IViewDAO
{
    private static final String SQL_QUERY_SELECT = " SELECT view_key, view_description FROM profile_view WHERE view_key = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO profile_view (view_key, view_description) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM profile_view WHERE view_key = ? ";
    private static final String SQL_QUERY_UPDATE = " UPDATE profile_view SET view_description = ? WHERE view_key = ? ";
    private static final String SQL_QUERY_SELECTALL = " SELECT view_key, view_description FROM profile_view ORDER BY view_key ";
    private static final String SQL_QUERY_SELECT_VIEWS_FROM_SEARCH = " SELECT view_key, view_description FROM profile_view "
            + " WHERE view_key LIKE ? AND view_description LIKE ? ORDER BY view_key ";
    private static final String SQL_QUERY_SELECT_VIEW_FROM_PROFILE_KEY = " SELECT pvp.view_key, pvp.view_description "
            + " FROM profile_view pvp INNER JOIN profile_view_profile vp ON pvp.view_key = vp.view_key WHERE vp.profile_key = ? ";
    private static final String SQL_QUERY_SELECT_PROFILES_LIST_FOR_VIEW = " SELECT profile_key FROM profile_view_profile WHERE view_key = ? ORDER BY profile_key ASC ";
    private static final String SQL_QUERY_SELECT_VIEW_PROFILE_FROM_VIEW_KEY = " SELECT view_key, profile_key FROM profile_view_profile WHERE view_key = ? LIMIT 1 ";
    private static final String SQL_QUERY_INSERT_VIEW_PROFILE = " INSERT INTO profile_view_profile (view_key, profile_key) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE_PROFILES = " DELETE FROM profile_view_profile WHERE view_key = ? ";
    private static final String SQL_QUERY_DELETE_PROFILE_FROM_VIEW = " DELETE FROM profile_view_profile WHERE view_key = ? AND profile_key = ? ";
    private static final String SQL_QUERY_SELECT_DASHBOARDS = " SELECT dashboard_name, dashboard_column, dashboard_order FROM profile_view_dashboard ";
    private static final String SQL_QUERY_SELECT_DASHBOARD = " SELECT dashboard_column, dashboard_order "
            + " FROM profile_view_dashboard WHERE view_key = ? AND dashboard_name = ? ";
    private static final String SQL_QUERY_SELECT_DASHBOARDS_FROM_VIEW = " SELECT dashboard_name, dashboard_column, dashboard_order "
            + " FROM profile_view_dashboard WHERE view_key = ? ORDER BY dashboard_column, dashboard_order ";
    private static final String SQL_QUERY_DELETE_DASHBOARD = " DELETE FROM profile_view_dashboard WHERE view_key = ? AND dashboard_name = ? ";
    private static final String SQL_QUERY_DELETE_DASHBOARDS = " DELETE FROM profile_view_dashboard WHERE view_key = ? ";
    private static final String SQL_QUERY_INSERT_DASHBOARD = " INSERT INTO profile_view_dashboard (view_key, dashboard_name, dashboard_column, dashboard_order) VALUES (?,?,?,?) ";
    private static final String SQL_QUERY_STORE_DASHBOARD = " UPDATE profile_view_dashboard SET dashboard_column = ?, dashboard_order = ? "
            + " WHERE view_key = ? AND dashboard_name = ? ";
    private static final String SQL_QUERY_SELECT_COLUMNS = " SELECT dashboard_column FROM profile_view_dashboard GROUP BY dashboard_column ";
    private static final String SQL_QUERY_MAX_ORDER = " SELECT max(dashboard_order) FROM profile_view_dashboard ";
    private static final String SQL_QUERY_MAX_ORDER_COLUMN = SQL_QUERY_MAX_ORDER + " WHERE dashboard_column = ? ";
    private static final String SQL_QUERY_ORDER_BY_COLUMN_AND_ORDER = " ORDER BY dashboard_column, dashboard_order";
    private static final String SQL_QUERY_FILTER_COLUMN = " dashboard_column = ? ";
    private static final String SQL_QUERY_FILTER_ORDER = " dashboard_order = ? ";
    private static final String SQL_QUERY_KEYWORD_WHERE = "  WHERE ";
    private static final String SQL_QUERY_KEYWORD_AND = " AND ";

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( View view, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {
            daoUtil.setString( 1, view.getKey( ) );
            daoUtil.setString( 2, view.getDescription( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View load( String strViewKey, Plugin plugin )
    {
        View view = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setString( 1, strViewKey );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                view = new View( );
                view.setKey( daoUtil.getString( 1 ) );
                view.setDescription( daoUtil.getString( 2 ) );
            }
        }
        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( String strViewKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setString( 1, strViewKey );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( View view, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            daoUtil.setString( 1, view.getDescription( ) );
            daoUtil.setString( 2, view.getKey( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<View> selectViewsList( Plugin plugin )
    {
        List<View> listViews = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                View view = new View( );
                view.setKey( daoUtil.getString( 1 ) );
                view.setDescription( daoUtil.getString( 2 ) );

                listViews.add( view );
            }
        }
        return listViews;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<View> selectViewsByFilter( ViewFilter vFilter, Plugin plugin )
    {
        List<View> listFilteredViews = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_VIEWS_FROM_SEARCH, plugin ) )
        {
            daoUtil.setString( 1, ProfilesConstants.PERCENT + vFilter.getKey( ) + ProfilesConstants.PERCENT );
            daoUtil.setString( 2, ProfilesConstants.PERCENT + vFilter.getDescription( ) + ProfilesConstants.PERCENT );

            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                View view = new View( );
                view.setKey( daoUtil.getString( 1 ) );
                view.setDescription( daoUtil.getString( 2 ) );

                listFilteredViews.add( view );
            }
        }
        return listFilteredViews;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkExistView( String strViewKey, Plugin plugin )
    {
        boolean bResult = false;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setString( 1, strViewKey );
            daoUtil.executeQuery( );

            bResult = daoUtil.next( );
        }
        return bResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getViewsList( Plugin plugin )
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

    /* PROFILES */

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Profile> selectProfilesListForView( String strViewKey, Plugin plugin )
    {
        List<Profile> listProfiles = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PROFILES_LIST_FOR_VIEW, plugin ) )
        {
            daoUtil.setString( 1, strViewKey );

            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                Profile profile = new Profile( );
                profile.setKey( daoUtil.getString( 1 ) );

                listProfiles.add( profile );
            }
        }
        return listProfiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View selectViewForProfile( String strProfileKey, Plugin plugin )
    {
        View view = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_VIEW_FROM_PROFILE_KEY, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                view = new View( );
                view.setKey( daoUtil.getString( 1 ) );
                view.setDescription( daoUtil.getString( 2 ) );
            }
        }
        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasView( String strProfileKey, Plugin plugin )
    {
        boolean bHasView = false;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_VIEW_PROFILE_FROM_VIEW_KEY, plugin ) )
        {
            daoUtil.setString( 1, strProfileKey );
            daoUtil.executeQuery( );

            bHasView = daoUtil.next( );
        }
        return bHasView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertProfileForView( String strViewKey, String strProfileKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_VIEW_PROFILE, plugin ) )
        {
            daoUtil.setString( 1, strViewKey );
            daoUtil.setString( 2, strProfileKey );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteProfiles( String strViewKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_PROFILES, plugin ) )
        {
            daoUtil.setString( 1, strViewKey );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteProfileFromView( String strViewKey, String strProfileKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_PROFILE_FROM_VIEW, plugin ) )
        {
            daoUtil.setString( 1, strViewKey );
            daoUtil.setString( 2, strProfileKey );

            daoUtil.executeUpdate( );
        }
    }

    /* DASHBOARDS */

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IDashboardComponent> selectDashboards( String strViewKey, Plugin plugin )
    {
        List<IDashboardComponent> listDashboards = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_DASHBOARDS_FROM_VIEW, plugin ) )
        {
            daoUtil.setString( 1, strViewKey );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                IDashboardComponent dashboardComponent = null;

                String strBeanName = daoUtil.getString( 1 );

                dashboardComponent = DashboardFactory.getDashboardComponent( strBeanName );
                if ( dashboardComponent != null )
                {
                    dashboardComponent.setName( daoUtil.getString( 1 ) );
                    dashboardComponent.setZone( daoUtil.getInt( 2 ) );
                    dashboardComponent.setOrder( daoUtil.getInt( 3 ) );
                    listDashboards.add( dashboardComponent );
                }
                else
                {
                    AppLogService.error( "Dashboard named " + strBeanName + " not found" );
                }
            }
        }
        return listDashboards;
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
    @Override
    public IDashboardComponent selectDashboard( String strDashboardName, String strViewKey, Plugin plugin )
    {
        IDashboardComponent dashboardComponent = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_DASHBOARD, plugin ) )
        {
            daoUtil.setString( 1, strViewKey );
            daoUtil.setString( 2, strDashboardName );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                dashboardComponent = DashboardFactory.getDashboardComponent( strDashboardName );

                if ( dashboardComponent != null )
                {
                    dashboardComponent.setName( strDashboardName );
                    dashboardComponent.setZone( daoUtil.getInt( 1 ) );
                    dashboardComponent.setOrder( daoUtil.getInt( 2 ) );
                }
                else
                {
                    AppLogService.error( "Dashboard named " + strDashboardName + " not found" );
                }
            }
        }
        return dashboardComponent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDashboard( String strViewKey, String strDashboardName, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_DASHBOARD, plugin ) )
        {
            daoUtil.setString( 1, strViewKey );
            daoUtil.setString( 2, strDashboardName );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDashboards( String strViewKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_DASHBOARDS, plugin ) )
        {
            daoUtil.setString( 1, strViewKey );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertDashboard( String strViewKey, IDashboardComponent dashboard, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_DASHBOARD, plugin ) )
        {
            daoUtil.setString( 1, strViewKey );
            daoUtil.setString( 2, dashboard.getName( ) );
            daoUtil.setInt( 3, dashboard.getZone( ) );
            daoUtil.setInt( 4, dashboard.getOrder( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void storeDashboard( String strViewKey, IDashboardComponent dashboard, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_STORE_DASHBOARD, plugin ) )
        {
            daoUtil.setInt( 1, dashboard.getZone( ) );
            daoUtil.setInt( 2, dashboard.getOrder( ) );

            daoUtil.setString( 3, strViewKey );
            daoUtil.setString( 4, dashboard.getName( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int selectMaxOrder( Plugin plugin )
    {
        int nMaxOrder = 0;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_MAX_ORDER, plugin ) )
        {
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                nMaxOrder = daoUtil.getInt( 1 );
            }
        }
        return nMaxOrder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int selectMaxOrder( int nColumn, Plugin plugin )
    {
        int nMaxOrder = 0;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_MAX_ORDER_COLUMN, plugin ) )
        {
            daoUtil.setInt( 1, nColumn );

            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                nMaxOrder = daoUtil.getInt( 1 );
            }
        }
        return nMaxOrder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> selectColumns( Plugin plugin )
    {
        List<Integer> listColumns = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_COLUMNS, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                listColumns.add( daoUtil.getInt( 1 ) );
            }
        }
        return listColumns;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IDashboardComponent> selectDashboardsByFilter( DashboardFilter filter, String strViewKey, Plugin plugin )
    {
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_DASHBOARDS );
        buildSQLFilter( sbSQL, filter );
        sbSQL.append( SQL_QUERY_KEYWORD_AND + ProfilesConstants.SPACE + ProfilesConstants.PARAMETER_VIEW_KEY + ProfilesConstants.EQUAL
                + ProfilesConstants.INTERROGATION_MARK );
        sbSQL.append( SQL_QUERY_ORDER_BY_COLUMN_AND_ORDER );

        List<IDashboardComponent> listDashboards = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin ) )
        {
            int nIndex = applySQLFilter( daoUtil, 1, filter );
            daoUtil.setString( nIndex, strViewKey );

            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                IDashboardComponent dashboardComponent = null;

                String strBeanName = daoUtil.getString( 1 );

                dashboardComponent = DashboardFactory.getDashboardComponent( strBeanName );

                if ( dashboardComponent != null )
                {
                    load( dashboardComponent, daoUtil );
                    listDashboards.add( dashboardComponent );
                }
                else
                {
                    AppLogService.error( "dashboard named " + strBeanName + " not found" );
                }
            }
        }
        return listDashboards;
    }

    /**
     * Builds sql filter
     * 
     * @param sbSQL
     *            the buffer
     * @param filter
     *            the filter
     */
    private void buildSQLFilter( StringBuilder sbSQL, DashboardFilter filter )
    {
        List<String> listFilters = new ArrayList<>( );

        if ( filter.containsFilterOrder( ) )
        {
            listFilters.add( SQL_QUERY_FILTER_ORDER );
        }

        if ( filter.containsFilterColumn( ) )
        {
            listFilters.add( SQL_QUERY_FILTER_COLUMN );
        }

        if ( !listFilters.isEmpty( ) )
        {
            sbSQL.append( SQL_QUERY_KEYWORD_WHERE );

            boolean bFirstFilter = true;

            for ( String strFilter : listFilters )
            {
                sbSQL.append( strFilter );

                if ( !bFirstFilter )
                {
                    sbSQL.append( SQL_QUERY_KEYWORD_AND );
                }
                else
                {
                    bFirstFilter = false;
                }
            }
        }
    }

    /**
     * Add daoUtil parameters
     * 
     * @param daoUtil
     *            daoUtil
     * @param nStartIndex
     *            start index
     * @param filter
     *            the filter to apply
     * @return end index
     */
    private int applySQLFilter( DAOUtil daoUtil, int nStartIndex, DashboardFilter filter )
    {
        int nIndex = nStartIndex;

        if ( filter.containsFilterOrder( ) )
        {
            daoUtil.setInt( nIndex++, filter.getFilterOrder( ) );
        }

        if ( filter.containsFilterColumn( ) )
        {
            daoUtil.setInt( nIndex++, filter.getFilterColumn( ) );
        }

        return nIndex;
    }

    /**
     * Loads compenent data from daoUtil
     * 
     * @param component
     *            the component
     * @param daoUtil
     *            the daoutil
     */
    private void load( IDashboardComponent component, DAOUtil daoUtil )
    {
        int nIndex = 0;
        component.setName( daoUtil.getString( ++nIndex ) );
        component.setZone( daoUtil.getInt( ++nIndex ) );
        component.setOrder( daoUtil.getInt( ++nIndex ) );
    }
}
