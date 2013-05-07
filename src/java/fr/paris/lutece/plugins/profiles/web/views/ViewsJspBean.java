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
package fr.paris.lutece.plugins.profiles.web.views;

import fr.paris.lutece.plugins.profiles.business.Profile;
import fr.paris.lutece.plugins.profiles.business.ProfileFilter;
import fr.paris.lutece.plugins.profiles.business.views.View;
import fr.paris.lutece.plugins.profiles.business.views.ViewAction;
import fr.paris.lutece.plugins.profiles.business.views.ViewFilter;
import fr.paris.lutece.plugins.profiles.service.IProfilesService;
import fr.paris.lutece.plugins.profiles.service.action.IViewActionService;
import fr.paris.lutece.plugins.profiles.service.views.IViewsService;
import fr.paris.lutece.plugins.profiles.service.views.ViewsResourceIdService;
import fr.paris.lutece.plugins.profiles.utils.constants.ProfilesConstants;
import fr.paris.lutece.portal.business.dashboard.DashboardFactory;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.dashboard.DashboardService;
import fr.paris.lutece.portal.service.dashboard.IDashboardComponent;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.ItemNavigator;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.sort.AttributeComparator;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * class ViewsJspBean
 *
 */
public class ViewsJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_MANAGE_VIEWS = "PROFILES_VIEWS_MANAGEMENT";

    // TEMPLATES
    private static final String TEMPLATE_MANAGE_VIEWS = "admin/plugins/profiles/views/manage_views.html";
    private static final String TEMPLATE_CREATE_VIEW = "admin/plugins/profiles/views/create_view.html";
    private static final String TEMPLATE_MODIFY_VIEW = "admin/plugins/profiles/views/modify_view.html";
    private static final String TEMPLATE_ASSIGN_PROFILES_VIEW = "admin/plugins/profiles/views/assign_profiles_view.html";
    private static final String TEMPLATE_MANAGE_DASHBOARDS = "admin/plugins/profiles/views/manage_dashboards.html";

    // JSP
    private static final String JSP_MANAGE_VIEWS = "ManageViews.jsp";
    private static final String JSP_URL_DO_REMOVE_VIEW = "jsp/admin/plugins/profiles/DoRemoveView.jsp";
    private static final String JSP_URL_MODIFY_VIEW = "jsp/admin/plugins/profiles/ModifyView.jsp";
    private static final String JSP_URL_ASSIGN_PROFILES_VIEW = "jsp/admin/plugins/profiles/AssignProfilesView.jsp";
    private static final String JSP_ASSIGN_PROFILES_VIEW = "AssignProfilesView.jsp";
    private static final String JSP_URL_MANAGE_DASHBOARDS = "jsp/admin/plugins/profiles/ManageDashboards.jsp";
    private static final String JSP_MANAGE_DASHBOARDS = "ManageDashboards.jsp";

    // VARIABLES
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;
    private Map<String, ItemNavigator> _itemNavigators = new HashMap<String, ItemNavigator>(  );
    private IViewsService _viewsService = (IViewsService) SpringContextService.getBean( ProfilesConstants.BEAN_VIEWS_SERVICE );
    private IViewActionService _viewActionService = (IViewActionService) SpringContextService.getBean( ProfilesConstants.BEAN_VIEW_ACTION_SERVICE );
    private IProfilesService _profilesService = (IProfilesService) SpringContextService.getBean( ProfilesConstants.BEAN_PROFILES_SERVICE );
    private ViewFilter _vFilter;

    /**
     * Return views management
     * @param request The Http request
     * @return Html views management page
     */
    public String getManageViews( HttpServletRequest request )
    {
        setPageTitleProperty( ProfilesConstants.PROPERTY_MANAGE_VIEWS_PAGETITLE );

        // Reinit session
        reinitItemNavigators(  );

        // FILTER
        _vFilter = new ViewFilter(  );

        boolean bIsSearch = _vFilter.setFilter( request );

        List<View> filteredViews = (List<View>) _viewsService.findViewsByFilter( _vFilter, getPlugin(  ) );

        // SORT
        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = null;

        if ( strSortedAttributeName != null )
        {
            strAscSort = request.getParameter( Parameters.SORTED_ASC );

            boolean bIsAscSort = Boolean.parseBoolean( strAscSort );

            Collections.sort( filteredViews, new AttributeComparator( strSortedAttributeName, bIsAscSort ) );
        }

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( ProfilesConstants.PROPERTY_ITEM_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        String strURL = getHomeUrl( request );
        UrlItem url = new UrlItem( strURL );

        if ( strSortedAttributeName != null )
        {
            url.addParameter( Parameters.SORTED_ATTRIBUTE_NAME, strSortedAttributeName );
        }

        if ( strAscSort != null )
        {
            url.addParameter( Parameters.SORTED_ASC, strAscSort );
        }

        String strSortSearchAttribute = StringUtils.EMPTY;

        if ( bIsSearch )
        {
            _vFilter.setUrlAttributes( url );
            strSortSearchAttribute = ProfilesConstants.AMPERSAND + _vFilter.getUrlAttributes(  );
        }

        // PAGINATOR
        LocalizedPaginator<View> paginator = new LocalizedPaginator<View>( filteredViews, _nItemsPerPage,
                url.getUrl(  ), Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale(  ) );

        // PERMISSIONS
        for ( View view : filteredViews )
        {
            List<ViewAction> listActions = _viewActionService.selectActionsList( getLocale(  ), getPlugin(  ) );
            listActions = (List<ViewAction>) RBACService.getAuthorizedActionsCollection( listActions, view, getUser(  ) );
            view.setActions( listActions );
        }

        boolean bPermission = RBACService.isAuthorized( View.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                ViewsResourceIdService.PERMISSION_CREATE_VIEW, getUser(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( ProfilesConstants.MARK_NB_ITEMS_PER_PAGE, StringUtils.EMPTY + _nItemsPerPage );
        model.put( ProfilesConstants.MARK_PAGINATOR, paginator );
        model.put( ProfilesConstants.MARK_LIST_VIEWS, paginator.getPageItems(  ) );
        model.put( ProfilesConstants.MARK_SEARCH_FILTER, _vFilter );
        model.put( ProfilesConstants.MARK_SEARCH_IS_SEARCH, bIsSearch );
        model.put( ProfilesConstants.MARK_SORT_SEARCH_ATTRIBUTE, strSortSearchAttribute );
        model.put( ProfilesConstants.MARK_PERMISSION, bPermission );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_VIEWS, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /* CREATE VIEW */

    /**
     * Returns the view creation form
     *
     * @param request The Http request
     * @return Html creation form
     */
    public String getCreateView( HttpServletRequest request )
    {
        setPageTitleProperty( ProfilesConstants.PROPERTY_CREATE_VIEW_PAGETITLE );

        Map<String, Object> model = new HashMap<String, Object>(  );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_VIEW, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the data capture form of a new view
     *
     * @param request The HTTP Request
     * @return The Jsp URL of the process result
     */
    public String doCreateView( HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( View.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    ViewsResourceIdService.PERMISSION_CREATE_VIEW, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        String strKey = request.getParameter( ProfilesConstants.PARAMETER_VIEW_KEY );
        String strDescription = request.getParameter( ProfilesConstants.PARAMETER_VIEW_DESCRIPTION );

        if ( StringUtils.isBlank( strKey ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( StringUtils.isBlank( strDescription ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Check if the view already exists
        if ( _viewsService.checkExistView( strKey, getPlugin(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, ProfilesConstants.MESSAGE_VIEW_ALREADY_EXISTS,
                AdminMessage.TYPE_STOP );
        }

        // Check if strKey contains accentuated characters
        if ( !StringUtil.checkCodeKey( strKey ) )
        {
            return AdminMessageService.getMessageUrl( request, ProfilesConstants.MESSAGE_ACCENTUATED_CHARACTER,
                AdminMessage.TYPE_STOP );
        }

        View view = new View(  );
        view.setKey( strKey.trim(  ) );
        view.setDescription( strDescription );
        _viewsService.create( view, getPlugin(  ) );

        return JSP_MANAGE_VIEWS;
    }

    /* REMOVE VIEW */

    /**
     * Returns the confirmation to remove the view
     *
     * @param request The Http request
     * @return the confirmation page
     */
    public String getConfirmRemoveView( HttpServletRequest request )
    {
        String strViewKey = request.getParameter( ProfilesConstants.PARAMETER_VIEW_KEY );

        UrlItem url = new UrlItem( JSP_URL_DO_REMOVE_VIEW );
        url.addParameter( ProfilesConstants.PARAMETER_VIEW_KEY, strViewKey );

        return AdminMessageService.getMessageUrl( request, ProfilesConstants.MESSAGE_CONFIRM_REMOVE_VIEW,
            url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Remove a view
     *
     * @param request The Http request
     * @return Html form
     */
    public String doRemoveView( HttpServletRequest request )
    {
        String strViewKey = request.getParameter( ProfilesConstants.PARAMETER_VIEW_KEY );

        if ( !RBACService.isAuthorized( View.RESOURCE_TYPE, strViewKey, ViewsResourceIdService.PERMISSION_DELETE_VIEW,
                    getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        _viewsService.removeProfiles( strViewKey, getPlugin(  ) );
        _viewsService.removeDashboards( strViewKey, getPlugin(  ) );
        _viewsService.remove( strViewKey, getPlugin(  ) );

        return JSP_MANAGE_VIEWS;
    }

    /* MODIFY VIEW */

    /**
     * Returns the form for view modification
     *
     * @param request The Http request
     * @return Html form
     */
    public String getModifyView( HttpServletRequest request )
    {
        setPageTitleProperty( ProfilesConstants.PROPERTY_MODIFY_PROFILE_PAGETITLE );

        String strViewKey = request.getParameter( ProfilesConstants.PARAMETER_VIEW_KEY );
        View view = _viewsService.findByPrimaryKey( strViewKey, getPlugin(  ) );

        String strPermission = ViewsResourceIdService.PERMISSION_MODIFY_VIEW;
        boolean bPermission = RBACService.isAuthorized( View.RESOURCE_TYPE, strViewKey, strPermission, getUser(  ) );
        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_MODIFY_VIEW;
        UrlItem url = new UrlItem( strBaseUrl );

        // ITEM NAVIGATION
        setItemNavigator( ProfilesConstants.PARAMETER_MODIFY_VIEW, view, url );

        // PERMISSIONS
        List<ViewAction> listActions = _viewsService.getListActions( getUser(  ), view, strPermission, getLocale(  ),
                getPlugin(  ) );
        view.setActions( listActions );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( ProfilesConstants.MARK_VIEW, view );
        model.put( ProfilesConstants.MARK_ITEM_NAVIGATOR, _itemNavigators.get( ProfilesConstants.PARAMETER_MODIFY_VIEW ) );
        model.put( ProfilesConstants.MARK_PERMISSION, bPermission );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_VIEW, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Update a view
     *
     * @param request The Http request
     * @return Html form
     */
    public String doModifyView( HttpServletRequest request )
    {
        String strViewKey = request.getParameter( ProfilesConstants.PARAMETER_VIEW_KEY );

        if ( !RBACService.isAuthorized( View.RESOURCE_TYPE, strViewKey, ViewsResourceIdService.PERMISSION_MODIFY_VIEW,
                    getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        String strDescription = request.getParameter( ProfilesConstants.PARAMETER_VIEW_DESCRIPTION );

        if ( StringUtils.isBlank( strDescription ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        View view = new View(  );
        view.setKey( strViewKey.trim(  ) );
        view.setDescription( strDescription );
        _viewsService.update( view, getPlugin(  ) );

        return JSP_MANAGE_VIEWS;
    }

    /* ASSIGN PROFILES */

    /**
     * Returns the user assignation form
     *
     * @param request The Http request
     * @return the html code for display the modes list
     */
    public String getAssignProfilesView( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        setPageTitleProperty( ProfilesConstants.PROPERTY_ASSIGN_PROFILES_VIEW_PAGETITLE );

        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_ASSIGN_PROFILES_VIEW;
        UrlItem url = new UrlItem( strBaseUrl );

        // VIEW
        String strViewKey = request.getParameter( ProfilesConstants.PARAMETER_VIEW_KEY );
        View view = _viewsService.findByPrimaryKey( strViewKey, getPlugin(  ) );

        // ASSIGNED PROFILES
        List<Profile> listAssignedProfiles = new ArrayList<Profile>(  );

        for ( Profile profile : _viewsService.getProfilesListForView( strViewKey, getPlugin(  ) ) )
        {
            profile = _profilesService.findByPrimaryKey( profile.getKey(  ), getPlugin(  ) );
            listAssignedProfiles.add( profile );
        }

        // FILTERED PROFILES
        ProfileFilter pFilter = new ProfileFilter(  );
        List<Profile> listFilteredProfiles = new ArrayList<Profile>(  );
        boolean bIsSearch = pFilter.setFilter( request );

        for ( Profile filteredProfile : _profilesService.findProfilesByFilter( pFilter, getPlugin(  ) ) )
        {
            for ( Profile profile : listAssignedProfiles )
            {
                if ( filteredProfile.getKey(  ).equals( profile.getKey(  ) ) )
                {
                    listFilteredProfiles.add( profile );
                }
            }
        }

        String strSortSearchAttribute = StringUtils.EMPTY;

        if ( bIsSearch )
        {
            pFilter.setUrlAttributes( url );
            strSortSearchAttribute = ProfilesConstants.AMPERSAND + pFilter.getUrlAttributes(  );
        }

        // AVAILABLE PROFILES
        ReferenceList listAvailableProfiles = new ReferenceList(  );
        ReferenceItem itemProfile = null;
        boolean bAssigned;

        for ( Profile profile : _profilesService.findAll( getPlugin(  ) ) )
        {
            itemProfile = new ReferenceItem(  );
            itemProfile.setCode( profile.getKey(  ) );
            itemProfile.setName( profile.getKey(  ) );
            bAssigned = false;

            for ( Profile assignedProfile : listAssignedProfiles )
            {
                if ( assignedProfile.getKey(  ).equals( itemProfile.getCode(  ) ) )
                {
                    bAssigned = true;

                    break;
                }
            }

            if ( !bAssigned )
            {
                listAvailableProfiles.add( itemProfile );
            }
        }

        // SORT
        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = null;

        if ( strSortedAttributeName != null )
        {
            strAscSort = request.getParameter( Parameters.SORTED_ASC );

            boolean bIsAscSort = Boolean.parseBoolean( strAscSort );

            Collections.sort( listFilteredProfiles, new AttributeComparator( strSortedAttributeName, bIsAscSort ) );
        }

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( ProfilesConstants.PROPERTY_ITEM_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        if ( strSortedAttributeName != null )
        {
            url.addParameter( Parameters.SORTED_ATTRIBUTE_NAME, strSortedAttributeName );
        }

        if ( strAscSort != null )
        {
            url.addParameter( Parameters.SORTED_ASC, strAscSort );
        }

        String strPermission = ViewsResourceIdService.PERMISSION_MANAGE_PROFILES_ASSIGNMENT;
        boolean bPermission = RBACService.isAuthorized( View.RESOURCE_TYPE, strViewKey, strPermission, getUser(  ) );

        // ITEM NAVIGATION
        setItemNavigator( ProfilesConstants.PARAMETER_ASSIGN_PROFILE, view, url );

        // PAGINATOR
        url.addParameter( ProfilesConstants.PARAMETER_PROFILE_KEY, view.getKey(  ) );

        LocalizedPaginator<Profile> paginator = new LocalizedPaginator<Profile>( listFilteredProfiles, _nItemsPerPage,
                url.getUrl(  ), Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale(  ) );

        // PERMISSIONS
        List<ViewAction> listActions = _viewsService.getListActions( getUser(  ), view, strPermission, getLocale(  ),
                getPlugin(  ) );
        view.setActions( listActions );

        model.put( ProfilesConstants.MARK_VIEW, view );
        model.put( ProfilesConstants.MARK_AVAILABLE_LIST, listAvailableProfiles );
        model.put( ProfilesConstants.MARK_ASSIGNED_LIST, paginator.getPageItems(  ) );
        model.put( ProfilesConstants.MARK_ASSIGNED_NUMBER, listAssignedProfiles.size(  ) );
        model.put( ProfilesConstants.MARK_ITEM_NAVIGATOR,
            _itemNavigators.get( ProfilesConstants.PARAMETER_ASSIGN_PROFILE ) );
        model.put( ProfilesConstants.MARK_NB_ITEMS_PER_PAGE, StringUtils.EMPTY + _nItemsPerPage );
        model.put( ProfilesConstants.MARK_PAGINATOR, paginator );
        model.put( ProfilesConstants.MARK_PERMISSION, bPermission );
        model.put( ProfilesConstants.MARK_SEARCH_FILTER, pFilter );
        model.put( ProfilesConstants.MARK_SEARCH_IS_SEARCH, bIsSearch );
        model.put( ProfilesConstants.MARK_LOCALE, getUser(  ).getLocale(  ) );
        model.put( ProfilesConstants.MARK_SORT_SEARCH_ATTRIBUTE, strSortSearchAttribute );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ASSIGN_PROFILES_VIEW, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the data capture form for assign users to a profile
     *
     * @param request The HTTP Request
     * @return The Jsp URL of the process result
     */
    public String doAssignProfilesView( HttpServletRequest request )
    {
        String strReturn;

        String strActionCancel = request.getParameter( ProfilesConstants.PARAMETER_CANCEL );

        if ( strActionCancel != null )
        {
            strReturn = JSP_MANAGE_VIEWS;
        }
        else
        {
            String strViewKey = request.getParameter( ProfilesConstants.PARAMETER_VIEW_KEY );

            if ( !RBACService.isAuthorized( View.RESOURCE_TYPE, strViewKey,
                        ViewsResourceIdService.PERMISSION_MANAGE_PROFILES_ASSIGNMENT, getUser(  ) ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
            }

            //retrieve the selected portlets ids
            String[] arrayProfileKeys = request.getParameterValues( ProfilesConstants.PARAMETER_PROFILES_LIST );

            if ( arrayProfileKeys != null )
            {
                for ( int i = 0; i < arrayProfileKeys.length; i++ )
                {
                    String strProfileKey = arrayProfileKeys[i];
                    Profile profile = _profilesService.findByPrimaryKey( strProfileKey, getPlugin(  ) );

                    if ( !_viewsService.hasView( strProfileKey, getPlugin(  ) ) )
                    {
                        _viewsService.addProfileForView( strViewKey, strProfileKey, getPlugin(  ) );
                    }
                    else
                    {
                        Object[] args = { profile.getKey(  ) };

                        return AdminMessageService.getMessageUrl( request,
                            ProfilesConstants.PROPERTY_NO_MULTIPLE_VIEWS, args, AdminMessage.TYPE_STOP );
                    }
                }
            }

            strReturn = JSP_ASSIGN_PROFILES_VIEW + ProfilesConstants.INTERROGATION_MARK +
                ProfilesConstants.PARAMETER_VIEW_KEY + ProfilesConstants.EQUAL + strViewKey;
        }

        return strReturn;
    }

    /**
     * unassigns users from profile
     * @param request The HttpRequest
     * @return the HTML code of list assignations
     */
    public String doUnassignProfileView( HttpServletRequest request )
    {
        String strViewKey = request.getParameter( ProfilesConstants.PARAMETER_VIEW_KEY );

        if ( !RBACService.isAuthorized( View.RESOURCE_TYPE, strViewKey,
                    ViewsResourceIdService.PERMISSION_MANAGE_PROFILES_ASSIGNMENT, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
        String strAnchor = request.getParameter( ProfilesConstants.PARAMETER_ANCHOR );

        // Remove profile
        _viewsService.removeProfileFromView( strViewKey, strProfileKey, getPlugin(  ) );

        return JSP_ASSIGN_PROFILES_VIEW + ProfilesConstants.INTERROGATION_MARK + ProfilesConstants.PARAMETER_VIEW_KEY +
        ProfilesConstants.EQUAL + strViewKey + ProfilesConstants.SHARP + strAnchor;
    }

    /* DASHBOARD POSITIONS */

    /**
     * Get the dashboard positions management interface
     * @param request HttpServletRequest
     * @return the html form
     */
    public String getManageDashboards( HttpServletRequest request )
    {
        String strViewKey = request.getParameter( ProfilesConstants.PARAMETER_VIEW_KEY );
        View view = _viewsService.findByPrimaryKey( strViewKey, getPlugin(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );

        String strPermission = ViewsResourceIdService.PERMISSION_MANAGE_DASHBOARDS;
        boolean bPermission = RBACService.isAuthorized( View.RESOURCE_TYPE, strViewKey, strPermission, getUser(  ) );

        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_DASHBOARDS;
        UrlItem url = new UrlItem( strBaseUrl );

        // ITEM NAVIGATION
        setItemNavigator( ProfilesConstants.PARAMETER_ASSIGN_DASHBOARD, view, url );

        // PERMISSIONS
        List<ViewAction> listActions = _viewsService.getListActions( getUser(  ), view, strPermission, getLocale(  ),
                getPlugin(  ) );
        view.setActions( listActions );

        Map<String, List<IDashboardComponent>> mapDashboards = _viewsService.getAllSetDashboards( strViewKey,
                getUser(  ), getPlugin(  ) );
        model.put( ProfilesConstants.MARK_MAP_DASHBOARDS, mapDashboards );

        List<IDashboardComponent> listNotSetDashboards = _viewsService.getNotSetDashboards( strViewKey, getUser(  ),
                getPlugin(  ) );
        model.put( ProfilesConstants.MARK_NOT_SET_DASHBOARDS, listNotSetDashboards );

        model.put( ProfilesConstants.MARK_COLUMN_COUNT, DashboardService.getInstance(  ).getColumnCount(  ) );
        model.put( ProfilesConstants.MARK_MAP_AVAILABLE_ORDERS, _viewsService.getMapAvailableOrders( getPlugin(  ) ) );
        model.put( ProfilesConstants.MARK_LIST_AVAILABLE_COLUMNS, _viewsService.getListAvailableColumns(  ) );
        model.put( ProfilesConstants.MARK_MAP_COLUMN_ORDER_STATUS,
            _viewsService.getOrderedColumnsStatus( strViewKey, getPlugin(  ) ) );

        model.put( ProfilesConstants.MARK_VIEW, view );
        model.put( ProfilesConstants.MARK_PERMISSION, bPermission );
        model.put( ProfilesConstants.MARK_ITEM_NAVIGATOR,
            _itemNavigators.get( ProfilesConstants.PARAMETER_ASSIGN_DASHBOARD ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_DASHBOARDS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Reorders columns
     * @param request the request
     * @return url
     */
    public String doReorderColumn( HttpServletRequest request )
    {
        String strViewKey = request.getParameter( ProfilesConstants.PARAMETER_VIEW_KEY );

        if ( !RBACService.isAuthorized( View.RESOURCE_TYPE, strViewKey,
                    ViewsResourceIdService.PERMISSION_MANAGE_DASHBOARDS, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        String strColumnName = request.getParameter( ProfilesConstants.PARAMETER_COLUMN );

        if ( StringUtils.isBlank( strColumnName ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nColumn = 0;

        try
        {
            nColumn = Integer.parseInt( strColumnName );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( "ViewJspBean.doReorderColumn : " + nfe.getMessage(  ), nfe );

            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        _viewsService.doReorderColumn( strViewKey, nColumn, getPlugin(  ) );

        return JSP_MANAGE_DASHBOARDS + ProfilesConstants.INTERROGATION_MARK + ProfilesConstants.PARAMETER_VIEW_KEY +
        ProfilesConstants.EQUAL + strViewKey;
    }

    /**
     * Moves the dashboard
     * @param request the request
     * @return url
     */
    public String doMoveDashboard( HttpServletRequest request )
    {
        String strViewKey = request.getParameter( ProfilesConstants.PARAMETER_VIEW_KEY );

        if ( !RBACService.isAuthorized( View.RESOURCE_TYPE, strViewKey,
                    ViewsResourceIdService.PERMISSION_MANAGE_DASHBOARDS, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        String strDashboardName = request.getParameter( ProfilesConstants.PARAMETER_DASHBOARD_NAME );

        if ( StringUtils.isBlank( strDashboardName ) )
        {
            return AdminMessageService.getMessageUrl( request, ProfilesConstants.MESSAGE_DASHBOARD_NOT_FOUND,
                AdminMessage.TYPE_STOP );
        }

        // retrieve dashboard from database. If not found, will use Spring.
        IDashboardComponent dashboard = _viewsService.findDashboard( strDashboardName, strViewKey, getPlugin(  ) );
        int nOldOrder = 0;
        int nOldColumn = 0;
        boolean bCreate = false;

        if ( dashboard == null )
        {
            bCreate = true;

            if ( AppLogService.isDebugEnabled(  ) )
            {
                AppLogService.debug( "Dashboard " + strDashboardName +
                    " has no property set. Retrieving from SpringContext" );
            }

            dashboard = DashboardFactory.getDashboardComponent( strDashboardName );

            if ( dashboard == null )
            {
                return AdminMessageService.getMessageUrl( request, ProfilesConstants.MESSAGE_DASHBOARD_NOT_FOUND,
                    AdminMessage.TYPE_STOP );
            }
        }
        else
        {
            nOldOrder = dashboard.getOrder(  );
            nOldColumn = dashboard.getZone(  );
        }

        // set order and column
        String strOrder = request.getParameter( ProfilesConstants.PARAMETER_DASHBOARD_ORDER );
        String strColumn = request.getParameter( ProfilesConstants.PARAMETER_DASHBOARD_COLUMN );

        if ( StringUtils.isBlank( strOrder ) && StringUtils.isBlank( strColumn ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nOrder = StringUtil.getIntValue( strOrder, -1 );
        int nColumn = StringUtil.getIntValue( strColumn, -1 );

        if ( ( nOrder == -1 ) || ( nColumn == -1 ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        dashboard.setOrder( nOrder );
        dashboard.setZone( nColumn );

        _viewsService.doMoveDashboard( dashboard, nOldColumn, nOldOrder, bCreate, strViewKey, getPlugin(  ) );

        return JSP_MANAGE_DASHBOARDS + ProfilesConstants.INTERROGATION_MARK + ProfilesConstants.PARAMETER_VIEW_KEY +
        ProfilesConstants.EQUAL + strViewKey;
    }

    /**
     * Unset the column
     * @param request the request
     * @return url
     */
    public String doUnsetColumn( HttpServletRequest request )
    {
        String strViewKey = request.getParameter( ProfilesConstants.PARAMETER_VIEW_KEY );

        if ( !RBACService.isAuthorized( View.RESOURCE_TYPE, strViewKey, ViewsResourceIdService.PERMISSION_DELETE_VIEW,
                    getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        String strDashboardName = request.getParameter( ProfilesConstants.PARAMETER_DASHBOARD_NAME );

        _viewsService.removeDashboard( strViewKey, strDashboardName, getPlugin(  ) );

        return JSP_MANAGE_DASHBOARDS + ProfilesConstants.INTERROGATION_MARK + ProfilesConstants.PARAMETER_VIEW_KEY +
        ProfilesConstants.EQUAL + strViewKey;
    }

    /**
    * Get the item navigator
    * @param strItemNavigatorKey the item navigator key
    * @param view the view
    * @param url the url
    */
    private void setItemNavigator( String strItemNavigatorKey, View view, UrlItem url )
    {
        ItemNavigator itemNavigator = _itemNavigators.get( strItemNavigatorKey );

        if ( itemNavigator == null )
        {
            if ( _vFilter == null )
            {
                _vFilter = new ViewFilter(  );
            }

            itemNavigator = _viewsService.getItemNavigator( _vFilter, view, url );
        }
        else
        {
            itemNavigator.setCurrentItemId( view.getKey(  ) );
        }

        _itemNavigators.put( strItemNavigatorKey, itemNavigator );
    }

    /**
     * Reinit the item navigator
     */
    private void reinitItemNavigators(  )
    {
        _itemNavigators = new HashMap<String, ItemNavigator>(  );
    }
}
