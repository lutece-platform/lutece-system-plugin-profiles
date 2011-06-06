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
package fr.paris.lutece.plugins.profiles.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.paris.lutece.plugins.profiles.business.Profile;
import fr.paris.lutece.plugins.profiles.business.ProfileAction;
import fr.paris.lutece.plugins.profiles.business.ProfileActionHome;
import fr.paris.lutece.plugins.profiles.business.ProfileHome;
import fr.paris.lutece.plugins.profiles.business.views.View;
import fr.paris.lutece.plugins.profiles.business.views.ViewAction;
import fr.paris.lutece.plugins.profiles.business.views.ViewActionHome;
import fr.paris.lutece.plugins.profiles.business.views.ViewHome;
import fr.paris.lutece.plugins.profiles.utils.constants.ProfilesConstants;
import fr.paris.lutece.portal.business.dashboard.DashboardFilter;
import fr.paris.lutece.portal.business.dashboard.DashboardHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.DashboardService;
import fr.paris.lutece.portal.service.dashboard.IDashboardComponent;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.ItemNavigator;
import fr.paris.lutece.util.url.UrlItem;

/**
 * 
 * ProfilesService
 *
 */
public class ProfilesService 
{
	private static ProfilesService _singleton;
	
	/**
     * Initialize the profiles service
     *
     */
    public void init(  )
    {
        
    }
    
    /**
     * Returns the instance of the singleton
     *
     * @return The instance of the singleton
     */
    public static ProfilesService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new ProfilesService(  );
        }

        return _singleton;
    }
    
    /**
     * Get the item navigator
     * @param profile the profile
     * @param url the url
     * @param plugin Plugin
     * @return the item navigator
     */
    public ItemNavigator getItemNavigator( Profile profile, UrlItem url, Plugin plugin )
    {
    	Map<Integer, String> listItem = new HashMap<Integer, String>(  );
        Collection<Profile> listAllProfiles = ProfileHome.findAll( plugin );
        int nMapKey = 1;
        int nCurrentItemId = 1;
        for( Profile allProfile : listAllProfiles )
        {
    		listItem.put( nMapKey, allProfile.getKey(  ) );
        	if( allProfile.getKey(  ).equals( profile.getKey(  ) ) )
        	{
        		nCurrentItemId = nMapKey;
        	}
        	nMapKey++;
        }
        return new ItemNavigator( listItem, nCurrentItemId, url.getUrl(  ), ProfilesConstants.PARAMETER_PROFILE_KEY );
    }
    
    /**
     * Get the list of actions
     * @param user the current user
     * @param profile the profile
     * @param strPermission the permission name
     * @param locale Locale
     * @param plugin Plugin
     * @return the list of actions
     */
    public List<ProfileAction> getListActions( AdminUser user, Profile profile, String strPermission, Locale locale, Plugin plugin )
    {
    	List<ProfileAction> listActions = new ArrayList<ProfileAction>(  );
        for( ProfileAction action : ProfileActionHome.selectActionsList( locale, plugin ) )
        {
        	if( !action.getPermission(  ).equals( strPermission ) )
        	{
        		listActions.add( action );
        	}
        }
        listActions = ( List<ProfileAction> ) RBACService.getAuthorizedActionsCollection( listActions, profile, user );
        
        return listActions;
    }
    
    /**
     * Get the item navigator
     * @param view the view
     * @param url the url
     * @param plugin Plugin
     * @return the item navigator
     */
    public ItemNavigator getItemNavigator( View view, UrlItem url, Plugin plugin )
    {
    	Map<Integer, String> listItem = new HashMap<Integer, String>(  );
        Collection<View> listAllViews = ViewHome.findAll( plugin );
        int nMapKey = 1;
        int nCurrentItemId = 1;
        for( View allView : listAllViews )
        {
    		listItem.put( nMapKey, allView .getKey(  ) );
        	if( allView .getKey(  ).equals( view.getKey(  ) ) )
        	{
        		nCurrentItemId = nMapKey;
        	}
        	nMapKey++;
        }
        return new ItemNavigator( listItem, nCurrentItemId, url.getUrl(  ), ProfilesConstants.PARAMETER_VIEW_KEY );
    }
    
    /**
     * Get the list of actions
     * @param user the current user
     * @param view the view
     * @param strPermission the permission name
     * @param locale Locale
     * @param plugin Plugin
     * @return the list of actions
     */
    public List<ViewAction> getListActions( AdminUser user, View view, String strPermission, Locale locale, Plugin plugin )
    {
    	List<ViewAction> listActions = new ArrayList<ViewAction>(  );
        for( ViewAction action : ViewActionHome.selectActionsList( locale, plugin ) )
        {
        	if( !action.getPermission(  ).equals( strPermission ) )
        	{
        		listActions.add( action );
        	}
        }
        listActions = ( List<ViewAction> ) RBACService.getAuthorizedActionsCollection( listActions, view, user );
        
        return listActions;
    }

    /**
     * Get the list of dashboards of the given view
     * @param strViewKey the view key
     * @param user the current user
     * @param plugin Plugin
     * @return the list of dashboards
     */
    public Map<String, List<IDashboardComponent>> getAllSetDashboards( String strViewKey, AdminUser user, Plugin plugin )
    {	
    	Map<String, List<IDashboardComponent>> mapDashboardComponents = new HashMap<String, List<IDashboardComponent>>(  );
    	
    	// Personnalized dashboard positions
    	List<IDashboardComponent> listDashboards = ViewHome.findDashboards( strViewKey, plugin );

		for ( IDashboardComponent dashboard : listDashboards )
		{
			int nColumn = dashboard.getZone(  );
			boolean bRight = user.checkRight( dashboard.getRight(  ) ) || dashboard.getRight(  ).equalsIgnoreCase( ProfilesConstants.ALL );
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
     * Get the list of dashboards that are not set
     * @param strViewKey the view key
     * @param plugin Plugin
     * @return the list of dashboards
     */
    public List<IDashboardComponent> getNotSetDashboards( String strViewKey, AdminUser user, Plugin plugin )
    {
    	List<IDashboardComponent> listDashboards = DashboardHome.findAll(  ); 
    	List<IDashboardComponent> listPersonnalizedDashboards = ViewHome.findDashboards( strViewKey, plugin );
    	List<IDashboardComponent> listNotSetDashboards = new ArrayList<IDashboardComponent>(  );
    	
    	for ( IDashboardComponent dashboard : listDashboards )
    	{
    		boolean bRight = user.checkRight( dashboard.getRight(  ) ) || dashboard.getRight(  ).equalsIgnoreCase( ProfilesConstants.ALL );
    		if ( !listPersonnalizedDashboards.contains( dashboard ) && bRight && 
    				dashboard.getZone(  ) <= DashboardService.getInstance(  ).getColumnCount(  ) )
    		{
    			listNotSetDashboards.add( dashboard );
    		}
    	}
    	
    	return listNotSetDashboards;
    }
    
    /**
	 * Builds all refList order for all columns
	 * @param plugin Plugin
	 * @return the map with column id as key
	 */
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
	 * Orders reference list for the given column
	 * @param nColumn column
	 * @param plugin Plugin
	 * @return the refList
	 */
	public ReferenceList getListAvailableOrders( int nColumn, Plugin plugin )
	{
		ReferenceList refList = new ReferenceList(  );

		// add empty item
		refList.addItem( ProfilesConstants.EMPTY_STRING, ProfilesConstants.EMPTY_STRING );

		int nMaxOrder = ViewHome.findMaxOrder( nColumn, plugin );

		for ( int nOrder = 1; nOrder <= nMaxOrder; nOrder++ )
		{
			refList.addItem( nOrder, Integer.toString( nOrder ) );
		}

		return refList;
	}
	
	/**
	 * Returns list with available column
	 * @return all available columns
	 */
	public ReferenceList getListAvailableColumns(  )
	{
		ReferenceList refList = new ReferenceList(  );

		// add empty item
		refList.addItem( ProfilesConstants.EMPTY_STRING, ProfilesConstants.EMPTY_STRING );

		for ( int nColumnIndex = 1; nColumnIndex <= DashboardService.getInstance(  ).getColumnCount(  ); nColumnIndex++ )
		{
			refList.addItem( nColumnIndex, Integer.toString( nColumnIndex ) );
		}

		return refList;
	}
	
	/**
	 * Moves the dashboard.
	 * @param dashboard to move, with new values
	 * @param nOldColumn previous column id
	 * @param nOldOrder previous order
	 * @param bCreate <code>true</code> if this is a new dashboard, <code>false</code> otherwise.
	 */
	public void doMoveDashboard( IDashboardComponent dashboard, int nOldColumn, int nOldOrder, 
			boolean bCreate, String strViewKey, Plugin plugin )
	{
		int nColumn = dashboard.getZone(  );
		int nOrder = dashboard.getOrder(  );

		// find the dashboard already with this order and column
		DashboardFilter filter = new DashboardFilter(  );
		filter.setFilterColumn( nColumn );

		List<IDashboardComponent> listColumnDashboards = ViewHome.findDashboardsByFilter( filter, strViewKey, plugin );

		if ( listColumnDashboards != null && !listColumnDashboards.isEmpty(  ) )
		{
			if ( AppLogService.isDebugEnabled(  ) )
			{
				AppLogService.debug( "Reordering  dashboard column " + dashboard.getZone(  ) );
			}

			// sort by order
			Collections.sort( listColumnDashboards );
			int nMaxOrder = listColumnDashboards.get( listColumnDashboards.size(  ) - 1 ).getOrder(  );

			if ( nOldColumn == 0 || nOldColumn != nColumn )
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
							if ( nCurrentOrder >= nOrder && nCurrentOrder < nOldOrder )
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
							if ( nCurrentOrder <= nOrder && nCurrentOrder > nOldOrder )
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
	 * 
	 * @param nColumn the column id
	 * @return all dashboards for this column
	 */
	public List<IDashboardComponent> getDashboardComponents( String strViewKey, int nColumn, Plugin plugin )
	{
		DashboardFilter filter = new DashboardFilter(  );
		filter.setFilterColumn( nColumn );
		List<IDashboardComponent> dashboardComponents = ViewHome.findDashboardsByFilter( filter, strViewKey, plugin );

		return dashboardComponents;
	}
	
	/**
	 * Reorders column's dashboard
	 * @param nColumn the column to reorder
	 */
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
	 * Builds the map to with column id as key, and <code>true</code> as value if column is well ordered, <code>false</code> otherwise.
	 * @return the map
	 */
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
	 * Determines if the column is well ordered
	 * @param nColumn the column id
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
