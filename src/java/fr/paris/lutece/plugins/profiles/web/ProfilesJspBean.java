/*
 * Copyright (c) 2002-2010, Mairie de Paris
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
package fr.paris.lutece.plugins.profiles.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.profiles.business.Profile;
import fr.paris.lutece.plugins.profiles.business.ProfileAction;
import fr.paris.lutece.plugins.profiles.business.ProfileActionHome;
import fr.paris.lutece.plugins.profiles.business.ProfileFilter;
import fr.paris.lutece.plugins.profiles.business.ProfileHome;
import fr.paris.lutece.plugins.profiles.business.views.View;
import fr.paris.lutece.plugins.profiles.business.views.ViewHome;
import fr.paris.lutece.plugins.profiles.service.ProfilesPlugin;
import fr.paris.lutece.plugins.profiles.service.ProfilesResourceIdService;
import fr.paris.lutece.plugins.profiles.service.ProfilesService;
import fr.paris.lutece.plugins.profiles.web.constants.ProfilesConstants;
import fr.paris.lutece.portal.business.rbac.AdminRole;
import fr.paris.lutece.portal.business.rbac.AdminRoleHome;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.right.Level;
import fr.paris.lutece.portal.business.right.LevelHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.attribute.AdminUserField;
import fr.paris.lutece.portal.business.user.attribute.AdminUserFieldHome;
import fr.paris.lutece.portal.business.user.attribute.AttributeField;
import fr.paris.lutece.portal.business.user.attribute.AttributeFieldHome;
import fr.paris.lutece.portal.business.user.attribute.AttributeHome;
import fr.paris.lutece.portal.business.user.attribute.IAttribute;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroup;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupFilter;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.user.attribute.AdminUserFieldListenerService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupResource;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
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

/**
 * 
 * ProfilesJspBean
 * 
 */
public class ProfilesJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_MANAGE_PROFILES = "PROFILES_MANAGEMENT";

    private static final String BEAN_PROFILES_ADMIN_USER_FIELD_LISTENER_SERVICE = "profiles.profilesAdminUserFieldListenerService";
    
    // TEMPLATES
    private static final String TEMPLATE_MANAGE_PROFILES = "admin/plugins/profiles/manage_profiles.html";
    private static final String TEMPLATE_CREATE_PROFILE = "admin/plugins/profiles/create_profile.html";
    private static final String TEMPLATE_MODIFY_PROFILE = "admin/plugins/profiles/modify_profile.html";
    private static final String TEMPLATE_ASSIGN_RIGHTS_PROFILE = "admin/plugins/profiles/assign_rights_profile.html";
    private static final String TEMPLATE_ASSIGN_WORKGROUPS_PROFILE = "admin/plugins/profiles/assign_workgroups_profile.html";
    private static final String TEMPLATE_ASSIGN_ROLES_PROFILE = "admin/plugins/profiles/assign_roles_profile.html";
    private static final String TEMPLATE_ASSIGN_USERS_PROFILE = "admin/plugins/profiles/assign_users_profile.html";
    private static final String TEMPLATE_ASSIGN_VIEW_PROFILE = "admin/plugins/profiles/assign_view_profile.html";
    
    // JSP
    private static final String JSP_MANAGE_PROFILES = "ManageProfiles.jsp";
    private static final String JSP_URL_DO_REMOVE_PROFILE = "jsp/admin/plugins/profiles/DoRemoveProfile.jsp";
    private static final String JSP_URL_MODIFY_PROFILE = "jsp/admin/plugins/profiles/ModifyProfile.jsp";
    private static final String JSP_URL_ASSIGN_RIGHTS_PROFILE = "jsp/admin/plugins/profiles/AssignRightsProfile.jsp";
    private static final String JSP_ASSIGN_RIGHTS_PROFILE = "AssignRightsProfile.jsp";
    private static final String JSP_URL_ASSIGN_WORKGROUPS_PROFILE = "jsp/admin/plugins/profiles/AssignWorkgroupsProfile.jsp";
    private static final String JSP_ASSIGN_WORKGROUPS_PROFILE = "AssignWorkgroupsProfile.jsp";
    private static final String JSP_URL_ASSIGN_ROLES_PROFILE = "jsp/admin/plugins/profiles/AssignRolesProfile.jsp";
    private static final String JSP_ASSIGN_ROLES_PROFILE = "AssignRolesProfile.jsp";
    private static final String JSP_URL_ASSIGN_USERS_PROFILE = "jsp/admin/plugins/profiles/AssignUsersProfile.jsp";
    private static final String JSP_ASSIGN_USERS_PROFILE = "AssignUsersProfile.jsp";
    private static final String JSP_URL_ASSIGN_VIEW_PROFILE = "jsp/admin/plugins/profiles/AssignViewProfile.jsp";
    private static final String JSP_ASSIGN_VIEW_PROFILE = "AssignViewProfile.jsp";
    
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;

    /**
     * Return management page of plugin profiles
     * @param request The Http request
     * @return Html management page of plugin profiles
     */
	public String getManageProfiles( HttpServletRequest request )
    {
        setPageTitleProperty( ProfilesConstants.PROPERTY_MANAGE_PROFILES_PAGETITLE );
        
        // FILTER
        ProfileFilter pFilter = new ProfileFilter(  );
        boolean bIsSearch = pFilter.setFilter( request );
        
        List<Profile> filteredProfiles = ( List<Profile> ) ProfileHome.findProfilesByFilter( pFilter, getPlugin(  ) );
                
        // SORT
        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = null;

        if ( strSortedAttributeName != null )
        {
            strAscSort = request.getParameter( Parameters.SORTED_ASC );

            boolean bIsAscSort = Boolean.parseBoolean( strAscSort );

            Collections.sort( filteredProfiles, new AttributeComparator( strSortedAttributeName, bIsAscSort ) );
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
        
        String strSortSearchAttribute = ProfilesConstants.EMPTY_STRING;
        if( bIsSearch )
        {
        	pFilter.setUrlAttributes( url );
        	strSortSearchAttribute = ProfilesConstants.AMPERSAND + pFilter.getUrlAttributes(  );
        }

        // PAGINATOR
        LocalizedPaginator paginator = new LocalizedPaginator( filteredProfiles, _nItemsPerPage, url.getUrl(  ), Paginator.PARAMETER_PAGE_INDEX,
                _strCurrentPageIndex, getLocale(  ) );
        
        // PERMISSIONS
        for( Profile profile : filteredProfiles )
        {
        	List<ProfileAction> listActions = ProfileActionHome.selectActionsList( getLocale(  ), getPlugin(  ) );
            listActions = ( List<ProfileAction> ) RBACService.getAuthorizedActionsCollection( listActions, profile,
                    getUser(  ) );
            profile.setActions( listActions );
        }
        boolean bPermission = RBACService.isAuthorized( Profile.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                ProfilesResourceIdService.PERMISSION_CREATE_PROFILE, getUser(  ) );
        
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( ProfilesConstants.MARK_NB_ITEMS_PER_PAGE, ProfilesConstants.EMPTY_STRING + _nItemsPerPage );
        model.put( ProfilesConstants.MARK_PAGINATOR, paginator );
        model.put( ProfilesConstants.MARK_LIST_PROFILES, paginator.getPageItems(  ) );
        model.put( ProfilesConstants.MARK_SEARCH_FILTER, pFilter );
        model.put( ProfilesConstants.MARK_SEARCH_IS_SEARCH, bIsSearch );
        model.put( ProfilesConstants.MARK_SORT_SEARCH_ATTRIBUTE, strSortSearchAttribute );
        model.put( ProfilesConstants.MARK_PERMISSION, bPermission );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_PROFILES, getLocale(  ),
                model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /* CREATE PROFILE */
    
    /**
     * Returns the profile creation form
     *
     * @param request The Http request
     * @return Html creation form
     */
    public String getCreateProfile( HttpServletRequest request )
    {
    	setPageTitleProperty( ProfilesConstants.PROPERTY_CREATE_PROFILE_PAGETITLE );

        Map<String, Object> model = new HashMap<String, Object>(  );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_PROFILE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the data capture form of a new profile
     *
     * @param request The HTTP Request
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return The Jsp URL of the process result
     */
    public String doCreateProfile( HttpServletRequest request )
    	throws AccessDeniedException
    {
    	if ( !RBACService.isAuthorized( Profile.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    ProfilesResourceIdService.PERMISSION_CREATE_PROFILE, getUser(  ) ) )
        {
            throw new AccessDeniedException(  );
        }
    	
        String strKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
        String strDescription = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_DESCRIPTION );

        if ( ( strKey == null ) || ( strKey.equals( ProfilesConstants.EMPTY_STRING ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( ( strDescription == null ) || ( strDescription.equals( ProfilesConstants.EMPTY_STRING ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Check if profile already exist
        if ( ProfileHome.checkExistProfile( strKey, getPlugin(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, ProfilesConstants.MESSAGE_PROFILE_ALREADY_EXISTS, AdminMessage.TYPE_STOP );
        }

        // Check if strKey contains accentuated characters
        if ( !StringUtil.checkCodeKey( strKey ) )
        {
            return AdminMessageService.getMessageUrl( request, ProfilesConstants.MESSAGE_ACCENTUATED_CHARACTER,
                AdminMessage.TYPE_STOP );
        }

        Profile profile = new Profile(  );
        profile.setKey( strKey.trim(  ) );
        profile.setDescription( strDescription );
        ProfileHome.create( profile, getPlugin(  ) );
        
        // Create user field
        List<IAttribute> listAttributes = AttributeHome.findPluginAttributes( ProfilesPlugin.PLUGIN_NAME, getLocale(  ) );
        AttributeField attributeField = new AttributeField(  );
        attributeField.setTitle( profile.getKey(  ) );
        attributeField.setValue( profile.getDescription(  ) );
        attributeField.setDefaultValue( false );
        attributeField.setAttribute( listAttributes.get( 0 ) );
        AttributeFieldHome.create( attributeField );

        return JSP_MANAGE_PROFILES;
    }

    /* REMOVE PROFILE */
    
    /**
     * Returns the confirmation to remove the profile
     *
     * @param request The Http request
     * @return the confirmation page
     */
    public String getConfirmRemoveProfile( HttpServletRequest request )
    {
    	String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
    	
        UrlItem url = new UrlItem( JSP_URL_DO_REMOVE_PROFILE );
        url.addParameter( ProfilesConstants.PARAMETER_PROFILE_KEY, strProfileKey );

        return AdminMessageService.getMessageUrl( request, ProfilesConstants.MESSAGE_CONFIRM_REMOVE_PROFILE, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }
    
    /**
     * Remove a profile
     *
     * @param request The Http request
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return Html form
     */
    public String doRemoveProfile( HttpServletRequest request )
    	throws AccessDeniedException
    {
    	String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
    	
    	if ( !RBACService.isAuthorized( Profile.RESOURCE_TYPE, strProfileKey,
                    ProfilesResourceIdService.PERMISSION_DELETE_PROFILE, getUser(  ) ) )
        {
            throw new AccessDeniedException(  );
        }
    	
    	// check that no user has this profile 
        if ( ProfileHome.checkProfileAttributed( strProfileKey, getPlugin(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, ProfilesConstants.PROPERTY_PROFILE_ATTRIBUTED, AdminMessage.TYPE_STOP );
        }

        ProfileHome.removeRights( strProfileKey, getPlugin(  ) );
        ProfileHome.removeWorkgroups( strProfileKey, getPlugin(  ) );
        ProfileHome.removeRoles( strProfileKey, getPlugin(  ) );
        ProfileHome.removeView( strProfileKey, getPlugin(  ) );
        ProfileHome.remove( strProfileKey, getPlugin(  ) );
        
        // Remove user field
        List<IAttribute> listAttributes = AttributeHome.findPluginAttributes( ProfilesPlugin.PLUGIN_NAME, getLocale(  ) );
        IAttribute attribute = listAttributes.get( 0 );
        List<AttributeField> listAttributeFields = AttributeFieldHome.selectAttributeFieldsByIdAttribute( attribute.getIdAttribute(  ) );
        for ( AttributeField attributeField : listAttributeFields )
        {
        	if ( attributeField.getTitle(  ) != null && attributeField.getTitle(  ).equals( strProfileKey ) )
        	{
        		AttributeFieldHome.remove( attributeField.getIdField(  ) );
        		break;
        	}
        }

        return JSP_MANAGE_PROFILES;
    }

    /* MODIFY PROFILE */
    
    /**
     * Returns the form for profile modification
     *
     * @param request The Http request
     * @return Html form
     */
    public String getModifyProfile( HttpServletRequest request )
    {
    	setPageTitleProperty( ProfilesConstants.PROPERTY_MODIFY_PROFILE_PAGETITLE );
    	
    	String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
        Profile profile = ProfileHome.findByPrimaryKey( strProfileKey, getPlugin(  ) );

        String strPermission = ProfilesResourceIdService.PERMISSION_MODIFY_PROFILE;
        boolean bPermission = RBACService.isAuthorized( Profile.RESOURCE_TYPE, strProfileKey, strPermission, getUser(  ) );
        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_MODIFY_PROFILE;
        UrlItem url = new UrlItem( strBaseUrl );
        
        // ITEM NAVIGATION
        ItemNavigator itemNavigator = ProfilesService.getInstance(  ).getItemNavigator( profile, url, getPlugin(  ) );

        // PERMISSIONS
    	List<ProfileAction> listActions = ProfilesService.getInstance(  ).getListActions( getUser(  ), profile, 
    			strPermission, getLocale(  ), getPlugin(  ) );
        profile.setActions( listActions );
        
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( ProfilesConstants.MARK_PROFILE, profile );
        model.put( ProfilesConstants.MARK_ITEM_NAVIGATOR, itemNavigator );
        model.put( ProfilesConstants.MARK_PERMISSION, bPermission );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_PROFILE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Update a profile
     * @param request The Http request
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return Html form
     */
    public String doModifyProfile( HttpServletRequest request )
    	throws AccessDeniedException
    {
    	String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
    	
    	if ( !RBACService.isAuthorized( Profile.RESOURCE_TYPE, strProfileKey,
                    ProfilesResourceIdService.PERMISSION_MODIFY_PROFILE, getUser(  ) ) )
        {
            throw new AccessDeniedException(  );
        }
    	
        String strDescription = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_DESCRIPTION );

        if ( ( strDescription == null ) || ( strDescription.equals( ProfilesConstants.EMPTY_STRING ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        Profile profile = new Profile(  );
        profile.setKey( strProfileKey.trim(  ) );
        profile.setDescription( strDescription );
        ProfileHome.update( profile, getPlugin(  ) );
        
        // Modify user field
        List<IAttribute> listAttributes = AttributeHome.findPluginAttributes( ProfilesPlugin.PLUGIN_NAME, getLocale(  ) );
        IAttribute attribute = listAttributes.get( 0 );
        List<AttributeField> listAttributeFields = AttributeFieldHome.selectAttributeFieldsByIdAttribute( attribute.getIdAttribute(  ) );
        for ( AttributeField attributeField : listAttributeFields )
        {
        	if ( attributeField.getTitle(  ) != null && attributeField.getTitle(  ).equals( strProfileKey ) )
        	{
        		attributeField.setValue( profile.getDescription(  ) );
        		AttributeFieldHome.update( attributeField );
        		break;
        	}
        }

        return JSP_MANAGE_PROFILES;
    }
    
    /* ASSIGN RIGHTS */

    /**
     * Returns the right assignation form
     *
     * @param request The Http request
     * @return the html code for display the modes list
     */
    public String getAssignRightsProfile( HttpServletRequest request )
    {    	
        Map<String, Object> model = new HashMap<String, Object>(  );
        setPageTitleProperty( ProfilesConstants.PROPERTY_ASSIGN_RIGHTS_PROFILE_PAGETITLE );

        // PROFILE
        String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
        Profile profile = ProfileHome.findByPrimaryKey( strProfileKey, getPlugin(  ) );
        
        // ASSIGNED RIGHTS
        List<Right> listAssignedRights = new ArrayList<Right>(  );
        for ( Right right : ProfileHome.getRightsListForProfile( strProfileKey, getPlugin(  ) ) )
        {
        	right = RightHome.findByPrimaryKey( right.getId(  ) );
            //Add right with higher level then connected user or add all rights if connected user is administrator
            if ( ( right.getLevel(  ) > getUser(  ).getUserLevel(  ) ) || ( getUser(  ).isAdmin(  ) ) )
            {
            	right.setLocale( getLocale(  ) );
            	listAssignedRights.add( right );
            }
        }

        // AVAILABLE RIGHTS
        ReferenceList listAvailableRights = new ReferenceList(  );
        ReferenceItem itemRight = null;
        boolean bAssigned;

        for ( Right right : RightHome.getRightsList(  ) )
        {
        	right.setLocale( getLocale(  ) );
        	itemRight = new ReferenceItem(  );
        	itemRight.setCode( right.getId(  ) );
        	itemRight.setName( right.getName(  ) + ProfilesConstants.SPACE + ProfilesConstants.OPEN_BRACKET + 
        			I18nService.getLocalizedString ( ProfilesConstants.PROPERTY_ASSIGN_RIGHTS_PROFILE_LABEL_LEVEL , getLocale(  ) ) + 
        			ProfilesConstants.SPACE + right.getLevel(  ) + ProfilesConstants.CLOSED_BRACKET );
            bAssigned = false;

            for ( Right assignedRight : listAssignedRights )
            {
                if ( assignedRight.getId(  ).equals( itemRight.getCode(  ) ) )
                {
                    bAssigned = true;
                    break;
                }
            }

            //Add right with higher level then connected user or add all users if connected user is administrator
            if ( !bAssigned &&
                    ( ( right.getLevel(  ) > getUser(  ).getUserLevel(  ) ) || ( getUser(  ).isAdmin(  ) ) ) )
            {
            	listAvailableRights.add( itemRight );
            }
        }
        
        String strPermission = ProfilesResourceIdService.PERMISSION_MANAGE_RIGHTS_ASSIGNMENT;
        boolean bPermission = RBACService.isAuthorized( Profile.RESOURCE_TYPE, strProfileKey, strPermission, getUser(  ) );
        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_ASSIGN_RIGHTS_PROFILE;
        UrlItem url = new UrlItem( strBaseUrl );
        
        // ITEM NAVIGATION
        ItemNavigator itemNavigator = ProfilesService.getInstance(  ).getItemNavigator( profile, url, getPlugin(  ) );

        // PERMISSIONS
    	List<ProfileAction> listActions = ProfilesService.getInstance(  ).getListActions( getUser(  ), profile, 
    			strPermission, getLocale(  ), getPlugin(  ) );
        profile.setActions( listActions );
        
        model.put( ProfilesConstants.MARK_PROFILE, profile );
        model.put( ProfilesConstants.MARK_AVAILABLE_LIST, listAvailableRights );
        model.put( ProfilesConstants.MARK_ASSIGNED_LIST, listAssignedRights );
        model.put( ProfilesConstants.MARK_ASSIGNED_NUMBER, listAssignedRights.size(  ) );
        model.put( ProfilesConstants.MARK_ITEM_NAVIGATOR, itemNavigator );
        model.put( ProfilesConstants.MARK_PERMISSION, bPermission );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ASSIGN_RIGHTS_PROFILE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the data capture form for assign rights to a profile
     *
     * @param request The HTTP Request
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return The Jsp URL of the process result
     */
    public String doAssignRightsProfile( HttpServletRequest request )
    	throws AccessDeniedException
    {
        String strReturn;

        String strActionCancel = request.getParameter( ProfilesConstants.PARAMETER_CANCEL );

        if ( strActionCancel != null )
        {
            strReturn = JSP_MANAGE_PROFILES;
        }
        else
        {
        	String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
        	
        	if ( !RBACService.isAuthorized( Profile.RESOURCE_TYPE, strProfileKey,
                        ProfilesResourceIdService.PERMISSION_MANAGE_RIGHTS_ASSIGNMENT, getUser(  ) ) )
            {
                throw new AccessDeniedException(  );
            }
        	
            //retrieve the selected portlets ids
            String[] arrayRightsIds = request.getParameterValues( ProfilesConstants.PARAMETER_RIGHTS_LIST );

            if ( ( arrayRightsIds != null ) )
            {
                for ( int i = 0; i < arrayRightsIds.length; i++ )
                {
                    if ( !ProfileHome.hasRight( strProfileKey, arrayRightsIds[i], getPlugin(  ) ) )
                    {
                    	ProfileHome.addRightForProfile( strProfileKey, arrayRightsIds[i], getPlugin(  ) );
                    	
                    	// Update users rights
                    	Right right = RightHome.findByPrimaryKey( arrayRightsIds[i] );
                    	for ( AdminUser user : ProfileHome.getUsersListForProfile( strProfileKey, getPlugin(  ) ) )
                    	{
                    		if ( !AdminUserHome.hasRight( user, right.getId(  ) ) && 
                    				user.getUserLevel(  ) <= right.getLevel(  ) )
                            {
                    			AdminUserHome.createRightForUser( user.getUserId(  ), right.getId(  ) );
                            }
                    	}
                    }
                }
            }
            
            strReturn = JSP_ASSIGN_RIGHTS_PROFILE + ProfilesConstants.INTERROGATION_MARK + 
            		ProfilesConstants.PARAMETER_PROFILE_KEY + ProfilesConstants.EQUAL + strProfileKey;
        }

        return strReturn;
    }

    /**
     * unassigns right from profile
     * @param request The HttpRequest
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return the HTML code of list assignations
     */
    public String doUnassignRightProfile( HttpServletRequest request )
    	throws AccessDeniedException
    {
    	String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
    	
    	if ( !RBACService.isAuthorized( Profile.RESOURCE_TYPE, strProfileKey,
                    ProfilesResourceIdService.PERMISSION_MANAGE_RIGHTS_ASSIGNMENT, getUser(  ) ) )
        {
            throw new AccessDeniedException(  );
        }
    	
        String strIdRight = request.getParameter( ProfilesConstants.PARAMETER_ID_RIGHT );
        String strAnchor = request.getParameter( ProfilesConstants.PARAMETER_ANCHOR );

        ProfileHome.removeRightFromProfile( strProfileKey, strIdRight, getPlugin(  ) );
        
        // Update users rights
        Right right = RightHome.findByPrimaryKey( strIdRight );
        for ( AdminUser user : ProfileHome.getUsersListForProfile( strProfileKey, getPlugin(  ) ) )
        {
        	if ( AdminUserHome.hasRight( user, right.getId(  ) ) && 
    				( user.getUserLevel(  ) > getUser(  ).getUserLevel(  ) || getUser(  ).isAdmin(  ) ) )
            {
    			AdminUserHome.removeRightForUser( user.getUserId(  ), right.getId(  ) );
            }
        }

        return JSP_ASSIGN_RIGHTS_PROFILE + ProfilesConstants.INTERROGATION_MARK + ProfilesConstants.PARAMETER_PROFILE_KEY + 
        		ProfilesConstants.EQUAL + strProfileKey	+ ProfilesConstants.SHARP + strAnchor;
    }

    /* ASSIGN WORKGROUPS */
    
    /**
     * Returns the workgroup assignation form
     *
     * @param request The Http request
     * @return the html code for display the modes list
     */
    public String getAssignWorkgroupsProfile( HttpServletRequest request )
    {    	
        Map<String, Object> model = new HashMap<String, Object>(  );
        setPageTitleProperty( ProfilesConstants.PROPERTY_ASSIGN_WORKGROUPS_PROFILE_PAGETITLE );

        // PROFILE
        String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
        Profile profile = ProfileHome.findByPrimaryKey( strProfileKey, getPlugin(  ) );
        
        // ASSIGNED WORKGROUPS
        List<AdminWorkgroup> listAssignedWorkgroups = new ArrayList<AdminWorkgroup>(  );
        for ( AdminWorkgroup workgroup : ProfileHome.getWorkgroupsListForProfile( strProfileKey, getPlugin(  ) ) )
        {
        	workgroup = AdminWorkgroupHome.findByPrimaryKey( workgroup.getKey(  ) );
            listAssignedWorkgroups.add( workgroup );
        }
        
        // FILTER
        AdminWorkgroupFilter awFilter = new AdminWorkgroupFilter(  );
        List<AdminWorkgroup> listFilteredWorkgroups = new ArrayList<AdminWorkgroup>(  );
        boolean bIsSearch = awFilter.setAdminWorkgroupFilter( request );
        boolean bIsFiltered;
        
        for ( AdminWorkgroup filteredWorkgroup : AdminWorkgroupHome.findByFilter( awFilter ) )
        {
        	bIsFiltered = false;
        	
        	for( AdminWorkgroup assignedWorkgroup : listAssignedWorkgroups )
        	{
        		if ( assignedWorkgroup.getKey(  ).equals( filteredWorkgroup.getKey(  ) ) )
                {
            		bIsFiltered = true;
            		break;
                }
        	}
        	
        	if ( bIsFiltered )
            {
        		listFilteredWorkgroups.add( filteredWorkgroup );
            }
        }
        
        if ( !getUser(  ).isAdmin(  ) )
        {
        	listFilteredWorkgroups = ( List<AdminWorkgroup> ) AdminWorkgroupService.getAuthorizedCollection( 
        			(Collection<?extends AdminWorkgroupResource>) listFilteredWorkgroups, getUser(  ) );
        }

        // AVAILABLE WORKGROUPS
        ReferenceList listAvailableWorkgroups = new ReferenceList(  );
        ReferenceItem itemWorkgroup = null;
        boolean bAssigned;

        for ( AdminWorkgroup workgroup : AdminWorkgroupHome.findAll(  ) )
        {
        	itemWorkgroup = new ReferenceItem(  );
        	itemWorkgroup.setCode( workgroup.getKey(  ) );
        	itemWorkgroup.setName( workgroup.getKey(  ) );
            bAssigned = false;

            for ( AdminWorkgroup assignedWorkgroup : listAssignedWorkgroups )
            {
                if ( assignedWorkgroup.getKey(  ).equals( itemWorkgroup.getCode(  ) ) )
                {
                    bAssigned = true;
                }
            }
            
            if ( !bAssigned )
            {
            	listAvailableWorkgroups.add( itemWorkgroup );
            }
        }
        
        // SORT
        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = null;

        if ( strSortedAttributeName != null )
        {
            strAscSort = request.getParameter( Parameters.SORTED_ASC );

            boolean bIsAscSort = Boolean.parseBoolean( strAscSort );

            Collections.sort( listFilteredWorkgroups, new AttributeComparator( strSortedAttributeName, bIsAscSort ) );
        }

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( ProfilesConstants.PROPERTY_ITEM_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_ASSIGN_WORKGROUPS_PROFILE;
        UrlItem url = new UrlItem( strBaseUrl );
        
        if ( strSortedAttributeName != null )
        {
        	url.addParameter( Parameters.SORTED_ATTRIBUTE_NAME, strSortedAttributeName );
        }

        if ( strAscSort != null )
        {
        	url.addParameter( Parameters.SORTED_ASC, strAscSort );
        }
        
        String strSortSearchAttribute = ProfilesConstants.EMPTY_STRING;
        if( bIsSearch )
        {
        	awFilter.setUrlAttributes( url );
        	strSortSearchAttribute = ProfilesConstants.AMPERSAND + awFilter.getUrlAttributes(  );
        }
        
        String strPermission = ProfilesResourceIdService.PERMISSION_MANAGE_WORKGROUPS_ASSIGNMENT;
        boolean bPermission = RBACService.isAuthorized( Profile.RESOURCE_TYPE, strProfileKey, strPermission, getUser(  ) );
        
        // ITEM NAVIGATION
        ItemNavigator itemNavigator = ProfilesService.getInstance(  ).getItemNavigator( profile, url, getPlugin(  ) );
        
        // PAGINATOR
        url.addParameter( ProfilesConstants.PARAMETER_PROFILE_KEY, profile.getKey(  ) );
        LocalizedPaginator paginator = new LocalizedPaginator( listFilteredWorkgroups, _nItemsPerPage, url.getUrl(  ), Paginator.PARAMETER_PAGE_INDEX,
                    _strCurrentPageIndex, getLocale(  ) );

        // PERMISSIONS
    	List<ProfileAction> listActions = ProfilesService.getInstance(  ).getListActions( getUser(  ), profile, 
    			strPermission, getLocale(  ), getPlugin(  ) );
        profile.setActions( listActions );
        
        model.put( ProfilesConstants.MARK_PROFILE, profile );
        model.put( ProfilesConstants.MARK_AVAILABLE_LIST, listAvailableWorkgroups );
        model.put( ProfilesConstants.MARK_ASSIGNED_LIST, paginator.getPageItems(  ) );
        model.put( ProfilesConstants.MARK_ASSIGNED_NUMBER, listAssignedWorkgroups.size(  ) );
        model.put( ProfilesConstants.MARK_ITEM_NAVIGATOR, itemNavigator );
        model.put( ProfilesConstants.MARK_NB_ITEMS_PER_PAGE, ProfilesConstants.EMPTY_STRING + _nItemsPerPage );
        model.put( ProfilesConstants.MARK_PAGINATOR, paginator );
        model.put( ProfilesConstants.MARK_SEARCH_IS_SEARCH, bIsSearch );
        model.put( ProfilesConstants.MARK_SEARCH_FILTER, awFilter );
        model.put( ProfilesConstants.MARK_SORT_SEARCH_ATTRIBUTE, strSortSearchAttribute );
        model.put( ProfilesConstants.MARK_PERMISSION, bPermission );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ASSIGN_WORKGROUPS_PROFILE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the data capture form for assign workgroups to a profile
     *
     * @param request The HTTP Request
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return The Jsp URL of the process result
     */
    public String doAssignWorkgroupsProfile( HttpServletRequest request )
    	throws AccessDeniedException
    {
        String strReturn;

        String strActionCancel = request.getParameter( ProfilesConstants.PARAMETER_CANCEL );

        if ( strActionCancel != null )
        {
            strReturn = JSP_MANAGE_PROFILES;
        }
        else
        {
        	String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
        	
        	if ( !RBACService.isAuthorized( Profile.RESOURCE_TYPE, strProfileKey,
                        ProfilesResourceIdService.PERMISSION_MANAGE_WORKGROUPS_ASSIGNMENT, getUser(  ) ) )
            {
                throw new AccessDeniedException(  );
            }
        	
            //retrieve the selected portlets ids
            String[] arrayWorkgroupsIds = request.getParameterValues( ProfilesConstants.PARAMETER_WORKGROUPS_LIST );

            if ( ( arrayWorkgroupsIds != null ) )
            {
                for ( int i = 0; i < arrayWorkgroupsIds.length; i++ )
                {
                    if ( !ProfileHome.hasWorkgroup( strProfileKey, arrayWorkgroupsIds[i], getPlugin(  ) ) )
                    {
                    	ProfileHome.addWorkgroupForProfile( strProfileKey, arrayWorkgroupsIds[i], getPlugin(  ) );
                    	
                    	// Update users workgroups
                    	AdminWorkgroup workgroup = AdminWorkgroupHome.findByPrimaryKey( arrayWorkgroupsIds[i] );
                    	for ( AdminUser user : ProfileHome.getUsersListForProfile( strProfileKey, getPlugin(  ) ) )
                    	{
                    		if ( !AdminWorkgroupHome.isUserInWorkgroup( user, workgroup.getKey(  ) ) )
                            {
                    			AdminWorkgroupHome.addUserForWorkgroup( user, workgroup.getKey(  ) );
                            }
                    	}
                    }
                }
            }
            
            strReturn = JSP_ASSIGN_WORKGROUPS_PROFILE + ProfilesConstants.INTERROGATION_MARK + 
            		ProfilesConstants.PARAMETER_PROFILE_KEY + ProfilesConstants.EQUAL + strProfileKey;
        }

        return strReturn;
    }

    /**
     * unassigns workgroup from profile
     * @param request The HttpRequest
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return the HTML code of list assignations
     */
    public String doUnassignWorkgroupProfile( HttpServletRequest request )
    	throws AccessDeniedException
    {
    	String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
    	
    	if ( !RBACService.isAuthorized( Profile.RESOURCE_TYPE, strProfileKey,
                    ProfilesResourceIdService.PERMISSION_MANAGE_WORKGROUPS_ASSIGNMENT, getUser(  ) ) )
        {
            throw new AccessDeniedException(  );
        }
    	
        String strWorkgroupKey = request.getParameter( ProfilesConstants.PARAMETER_WORKGROUP_KEY );
        String strAnchor = request.getParameter( ProfilesConstants.PARAMETER_ANCHOR );

        ProfileHome.removeWorkgroupFromProfile( strProfileKey, strWorkgroupKey, getPlugin(  ) );
        
        // Update users workgroups
        AdminWorkgroup workgroup = AdminWorkgroupHome.findByPrimaryKey( strWorkgroupKey );
        for ( AdminUser user : ProfileHome.getUsersListForProfile( strProfileKey, getPlugin(  ) ) )
        {
        	if ( AdminWorkgroupHome.isUserInWorkgroup( user, workgroup.getKey(  ) ) )
            {
    			AdminWorkgroupHome.removeUserFromWorkgroup( user, workgroup.getKey(  ) );
            }
        }

        return JSP_ASSIGN_WORKGROUPS_PROFILE + ProfilesConstants.INTERROGATION_MARK + ProfilesConstants.PARAMETER_PROFILE_KEY + 
        	ProfilesConstants.EQUAL + strProfileKey + ProfilesConstants.SHARP + strAnchor;
    }

    /* ASSIGN ROLES */
    
    /**
     * Returns the role assignation form
     *
     * @param request The Http request
     * @return the html code for display the modes list
     */
    public String getAssignRolesProfile( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        setPageTitleProperty( ProfilesConstants.PROPERTY_ASSIGN_ROLES_PROFILE_PAGETITLE );

        // PROFILE
        String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
        Profile profile = ProfileHome.findByPrimaryKey( strProfileKey, getPlugin(  ) );
        
        // ASSIGNED ROLES
        List<AdminRole> listAssignedRoles = new ArrayList<AdminRole>(  );
        for ( AdminRole role : ProfileHome.getRolesListForProfile( strProfileKey, getPlugin(  ) ) )
        {
        	role = AdminRoleHome.findByPrimaryKey( role.getKey(  ) );
            listAssignedRoles.add( role );
        }

        // AVAILABLE ROLES
        ReferenceList listAvailableRoles = new ReferenceList(  );
        ReferenceItem itemRole = null;
        boolean bAssigned;

        for ( AdminRole role : AdminRoleHome.findAll(  ) )
        {
        	itemRole = new ReferenceItem(  );
        	itemRole.setCode( role.getKey(  ) );
        	itemRole.setName( role.getKey(  ) );
            bAssigned = false;

            for ( AdminRole assignedRole : listAssignedRoles )
            {
                if ( assignedRole.getKey(  ).equals( itemRole.getCode(  ) ) )
                {
                    bAssigned = true;
                    break;
                }
            }
            
            if ( !bAssigned )
            {
            	listAvailableRoles.add( itemRole );
            }
        }
        
        // SORT
        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = null;

        if ( strSortedAttributeName != null )
        {
            strAscSort = request.getParameter( Parameters.SORTED_ASC );

            boolean bIsAscSort = Boolean.parseBoolean( strAscSort );

            Collections.sort( listAssignedRoles, new AttributeComparator( strSortedAttributeName, bIsAscSort ) );
        }

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( ProfilesConstants.PROPERTY_ITEM_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_ASSIGN_ROLES_PROFILE;
        UrlItem url = new UrlItem( strBaseUrl );
        
        if ( strSortedAttributeName != null )
        {
        	url.addParameter( Parameters.SORTED_ATTRIBUTE_NAME, strSortedAttributeName );
        }

        if ( strAscSort != null )
        {
        	url.addParameter( Parameters.SORTED_ASC, strAscSort );
        }
        
        String strPermission = ProfilesResourceIdService.PERMISSION_MANAGE_ROLES_ASSIGNMENT; 
        boolean bPermission = RBACService.isAuthorized( Profile.RESOURCE_TYPE, strProfileKey, strPermission, getUser(  ) );
        
        // ITEM NAVIGATION
        ItemNavigator itemNavigator = ProfilesService.getInstance(  ).getItemNavigator( profile, url, getPlugin(  ) );
        
        // PAGINATOR
        url.addParameter( ProfilesConstants.PARAMETER_PROFILE_KEY, profile.getKey(  ) );
        LocalizedPaginator paginator = new LocalizedPaginator( listAssignedRoles, _nItemsPerPage, url.getUrl(  ), Paginator.PARAMETER_PAGE_INDEX,
                    _strCurrentPageIndex, getLocale(  ) );

        // PERMISSIONS
    	List<ProfileAction> listActions = ProfilesService.getInstance(  ).getListActions( getUser(  ), profile, 
    			strPermission, getLocale(  ), getPlugin(  ) );
        profile.setActions( listActions );
        
        
        model.put( ProfilesConstants.MARK_PROFILE, profile );
        model.put( ProfilesConstants.MARK_AVAILABLE_LIST, listAvailableRoles );
        model.put( ProfilesConstants.MARK_ASSIGNED_LIST, paginator.getPageItems(  ) );
        model.put( ProfilesConstants.MARK_ASSIGNED_NUMBER, listAssignedRoles.size(  ) );
        model.put( ProfilesConstants.MARK_ITEM_NAVIGATOR, itemNavigator );
        model.put( ProfilesConstants.MARK_NB_ITEMS_PER_PAGE, ProfilesConstants.EMPTY_STRING + _nItemsPerPage );
        model.put( ProfilesConstants.MARK_PAGINATOR, paginator );
        model.put( ProfilesConstants.MARK_PERMISSION, bPermission );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ASSIGN_ROLES_PROFILE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the data capture form for assign roles to a profile
     *
     * @param request The HTTP Request
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return The Jsp URL of the process result
     */
    public String doAssignRolesProfile( HttpServletRequest request )
    	throws AccessDeniedException
    {
        String strReturn;

        String strActionCancel = request.getParameter( ProfilesConstants.PARAMETER_CANCEL );

        if ( strActionCancel != null )
        {
            strReturn = JSP_MANAGE_PROFILES;
        }
        else
        {
        	String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
        	
        	if ( !RBACService.isAuthorized( Profile.RESOURCE_TYPE, strProfileKey,
                        ProfilesResourceIdService.PERMISSION_MANAGE_ROLES_ASSIGNMENT, getUser(  ) ) )
            {
                throw new AccessDeniedException(  );
            }
        	
            //retrieve the selected portlets ids
            String[] arrayRoleIds = request.getParameterValues( ProfilesConstants.PARAMETER_ROLES_LIST );

            if ( ( arrayRoleIds != null ) )
            {
                for ( int i = 0; i < arrayRoleIds.length; i++ )
                {
                    if ( !ProfileHome.hasRole( strProfileKey, arrayRoleIds[i], getPlugin(  ) ) )
                    {
                    	ProfileHome.addRoleForProfile( strProfileKey, arrayRoleIds[i], getPlugin(  ) );
                    	
                    	// Update users roles
                    	AdminRole role = AdminRoleHome.findByPrimaryKey( arrayRoleIds[i] );
                    	for ( AdminUser user : ProfileHome.getUsersListForProfile( strProfileKey, getPlugin(  ) ) )
                    	{
                    		if ( !AdminUserHome.hasRole( user, role.getKey(  ) ) )
                            {
                    			AdminUserHome.createRoleForUser( user.getUserId(  ), role.getKey(  ) );
                            }
                    	}
                    }
                }
            }
            
            strReturn = JSP_ASSIGN_ROLES_PROFILE + ProfilesConstants.INTERROGATION_MARK + 
            		ProfilesConstants.PARAMETER_PROFILE_KEY + ProfilesConstants.EQUAL + strProfileKey;
        }

        return strReturn;
    }

    /**
     * unassigns roles from profile
     * @param request The HttpRequest
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return the HTML code of list assignations
     */
    public String doUnassignRoleProfile( HttpServletRequest request )
    	throws AccessDeniedException
    {
    	String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
    	
    	if ( !RBACService.isAuthorized( Profile.RESOURCE_TYPE, strProfileKey,
                    ProfilesResourceIdService.PERMISSION_MANAGE_ROLES_ASSIGNMENT, getUser(  ) ) )
        {
            throw new AccessDeniedException(  );
        }
    	
        String strRoleKey = request.getParameter( ProfilesConstants.PARAMETER_ROLE_KEY );
        String strAnchor = request.getParameter( ProfilesConstants.PARAMETER_ANCHOR );

        ProfileHome.removeRoleFromProfile( strProfileKey, strRoleKey, getPlugin(  ) );
        
        // Update users roles
        AdminRole role = AdminRoleHome.findByPrimaryKey( strRoleKey );
        for ( AdminUser user : ProfileHome.getUsersListForProfile( strProfileKey, getPlugin(  ) ) )
        {
        	if ( AdminUserHome.hasRole( user, role.getKey(  ) ) )
            {
    			AdminUserHome.removeRoleForUser( user.getUserId(  ), role.getKey(  ) );
            }
        }

        return JSP_ASSIGN_ROLES_PROFILE + ProfilesConstants.INTERROGATION_MARK + ProfilesConstants.PARAMETER_PROFILE_KEY + 
        		ProfilesConstants.EQUAL + strProfileKey + ProfilesConstants.SHARP + strAnchor;
    }

    /* ASSIGN USERS */
    
    /**
     * Returns the user assignation form
     *
     * @param request The Http request
     * @return the html code for display the modes list
     */
    public String getAssignUsersProfile( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        setPageTitleProperty( ProfilesConstants.PROPERTY_ASSIGN_USERS_PROFILE_PAGETITLE );
        
        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_ASSIGN_USERS_PROFILE;
        UrlItem url = new UrlItem( strBaseUrl );

        // PROFILE
        String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
        Profile profile = ProfileHome.findByPrimaryKey( strProfileKey, getPlugin(  ) );
        
        // ASSIGNED USERS
        List<AdminUser> listAssignedUsers = new ArrayList<AdminUser>(  );
        for ( AdminUser user : ProfileHome.getUsersListForProfile( strProfileKey, getPlugin(  ) ) )
        {
        	user = AdminUserHome.findByPrimaryKey( user.getUserId(  ) );
        	 //Add users with higher level then connected user or add all users if connected user is administrator
            if ( ( user.getUserLevel(  ) > getUser(  ).getUserLevel(  ) ) || ( getUser(  ).isAdmin(  ) ) )
            {
            	listAssignedUsers.add( user );
            }
        }
        Collections.sort( listAssignedUsers, new AttributeComparator( ProfilesConstants.LAST_NAME, true ) );
        
        List<AdminUser> listFilteredUsers = AdminUserService.getFilteredUsersInterface( listAssignedUsers, request, model, url );

        // AVAILABLE USERS
        ReferenceList listAvailableUsers = new ReferenceList(  );
        ReferenceItem itemUser = null;
        boolean bAssigned;

        for ( AdminUser user : AdminUserHome.findUserList(  ) )
        {
        	itemUser = new ReferenceItem(  );
        	itemUser.setCode( Integer.toString( user.getUserId(  ) ) );
        	itemUser.setName( user.getLastName(  ) + ProfilesConstants.SPACE + user.getFirstName(  ) + 
        			ProfilesConstants.SPACE + ProfilesConstants.OPEN_BRACKET + user.getAccessCode(  ) + ProfilesConstants.CLOSED_BRACKET );
            bAssigned = false;

            for ( AdminUser assignedUser: listAssignedUsers )
            {
            	if ( Integer.toString( assignedUser.getUserId(  ) ).equals( itemUser.getCode(  ) ) )
                {
                    bAssigned = true;
                    break;
                }
            }

            //Add user with higher level then connected user or add all users if connected user is administrator
            if ( !bAssigned &&
                    ( ( user.getUserLevel(  ) > getUser(  ).getUserLevel(  ) ) || ( getUser(  ).isAdmin(  ) ) ) )
            {
            	listAvailableUsers.add( itemUser );
            }
        }
        
        // SORT
        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = null;

        if ( strSortedAttributeName != null )
        {
            strAscSort = request.getParameter( Parameters.SORTED_ASC );

            boolean bIsAscSort = Boolean.parseBoolean( strAscSort );

            Collections.sort( listFilteredUsers, new AttributeComparator( strSortedAttributeName, bIsAscSort ) );
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
        
        String strPermission = ProfilesResourceIdService.PERMISSION_MANAGE_USERS_ASSIGNMENT;
        boolean bPermission = RBACService.isAuthorized( Profile.RESOURCE_TYPE, strProfileKey, strPermission, getUser(  ) );
        
        // ITEM NAVIGATION
        ItemNavigator itemNavigator = ProfilesService.getInstance(  ).getItemNavigator( profile, url, getPlugin(  ) );
        
        // PAGINATOR
        url.addParameter( ProfilesConstants.PARAMETER_PROFILE_KEY, profile.getKey(  ) );
        LocalizedPaginator paginator = new LocalizedPaginator( listFilteredUsers, _nItemsPerPage, url.getUrl(  ), Paginator.PARAMETER_PAGE_INDEX,
                    _strCurrentPageIndex, getLocale(  ) );
        
        // USER LEVEL
        Collection<Level> filteredLevels = new ArrayList<Level>(  );

        for ( Level level : LevelHome.getLevelsList(  ) )
        {
            if ( getUser(  ).isAdmin(  ) || getUser(  ).hasRights( level.getId(  ) ) )
            {
                filteredLevels.add( level );
            }
        }

        // PERMISSIONS
    	List<ProfileAction> listActions = ProfilesService.getInstance(  ).getListActions( getUser(  ), profile, 
    			strPermission, getLocale(  ), getPlugin(  ) );
        profile.setActions( listActions );
        
        // Attribute
        List<IAttribute> listAttributes = AttributeHome.findPluginAttributes( ProfilesPlugin.PLUGIN_NAME, getLocale(  ) );
        IAttribute attribute = listAttributes.get( 0 );
        AttributeField attributeField = new AttributeField(  );
        attributeField.setTitle( profile.getKey(  ) );
        attributeField.setValue( profile.getDescription(  ) );
        attributeField.setDefaultValue( false );
        attributeField.setAttribute( listAttributes.get( 0 ) );
    	List<AttributeField> listAttributeFields = AttributeFieldHome.selectAttributeFieldsByIdAttribute( attribute.getIdAttribute(  ) );
    	for ( AttributeField aField : listAttributeFields )
    	{
    		if ( strProfileKey.equals( aField.getTitle(  ) ) )
    		{
    			attributeField.setIdField( aField.getIdField(  ) );
    			break;
    		}
    	}
        
        model.put( ProfilesConstants.MARK_USER_LEVELS, filteredLevels );
        model.put( ProfilesConstants.MARK_PROFILE, profile );
        model.put( ProfilesConstants.MARK_AVAILABLE_LIST, listAvailableUsers );
        model.put( ProfilesConstants.MARK_ASSIGNED_LIST, paginator.getPageItems(  ) );
        model.put( ProfilesConstants.MARK_ASSIGNED_NUMBER, listAssignedUsers.size(  ) );
        model.put( ProfilesConstants.MARK_ITEM_NAVIGATOR, itemNavigator );
        model.put( ProfilesConstants.MARK_NB_ITEMS_PER_PAGE, ProfilesConstants.EMPTY_STRING + _nItemsPerPage );
        model.put( ProfilesConstants.MARK_PAGINATOR, paginator );
        model.put( ProfilesConstants.MARK_PERMISSION, bPermission );
        model.put( ProfilesConstants.MARK_ATTRIBUTE, attribute );
    	model.put( ProfilesConstants.MARK_ATTRIBUTE_FIELD, attributeField );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ASSIGN_USERS_PROFILE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the data capture form for assign users to a profile
     *
     * @param request The HTTP Request
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return The Jsp URL of the process result
     */
    public String doAssignUsersProfile( HttpServletRequest request )
    	throws AccessDeniedException
    {
        String strReturn;

        String strActionCancel = request.getParameter( ProfilesConstants.PARAMETER_CANCEL );

        if ( strActionCancel != null )
        {
            strReturn = JSP_MANAGE_PROFILES;
        }
        else
        {
        	String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
        	
        	if ( !RBACService.isAuthorized( Profile.RESOURCE_TYPE, strProfileKey,
                        ProfilesResourceIdService.PERMISSION_MANAGE_USERS_ASSIGNMENT, getUser(  ) ) )
            {
                throw new AccessDeniedException(  );
            }
        	
            //retrieve the selected portlets ids
            String[] arrayUserIds = request.getParameterValues( ProfilesConstants.PARAMETER_USERS_LIST );

            if ( ( arrayUserIds != null ) )
            {
                for ( int i = 0; i < arrayUserIds.length; i++ )
                {
                	int nIdUser = Integer.parseInt( arrayUserIds[i] );
                    if ( !ProfileHome.hasProfile( nIdUser, getPlugin(  ) ) )
                    {
                    	AdminUser user = AdminUserHome.findByPrimaryKey( nIdUser );
                    	AdminUserFieldListenerService listenerService = (AdminUserFieldListenerService)
                    		SpringContextService.getBean( BEAN_PROFILES_ADMIN_USER_FIELD_LISTENER_SERVICE );
                    	listenerService.doCreateUserFields( user, request, getLocale(  ) );
                    }
                    else
                    {
                    	AdminUser user = AdminUserHome.findByPrimaryKey( nIdUser );
                    	Object[] args = { user.getLastName(  ) + ProfilesConstants.SPACE + user.getFirstName(  ) + ProfilesConstants.SPACE + 
                    			ProfilesConstants.OPEN_BRACKET + user.getAccessCode(  ) + ProfilesConstants.CLOSED_BRACKET, };
                    	return AdminMessageService.getMessageUrl( request, ProfilesConstants.PROPERTY_NO_MULTIPLE_PROFILES, args, AdminMessage.TYPE_STOP );
                    }
                }
            }
            
            strReturn = JSP_ASSIGN_USERS_PROFILE + ProfilesConstants.INTERROGATION_MARK + 
            		ProfilesConstants.PARAMETER_PROFILE_KEY + ProfilesConstants.EQUAL + strProfileKey;
        }

        return strReturn;
    }

    /**
     * unassigns users from profile
     * @param request The HttpRequest
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return the HTML code of list assignations
     */
    public String doUnassignUserProfile( HttpServletRequest request )
    	throws AccessDeniedException
    {
    	String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
    	
    	if ( !RBACService.isAuthorized( Profile.RESOURCE_TYPE, strProfileKey,
                    ProfilesResourceIdService.PERMISSION_MANAGE_USERS_ASSIGNMENT, getUser(  ) ) )
        {
            throw new AccessDeniedException(  );
        }
    	
        String strIdUser = request.getParameter( ProfilesConstants.PARAMETER_ID_USER );
        int nIdUser = Integer.parseInt( strIdUser );
        String strAnchor = request.getParameter( ProfilesConstants.PARAMETER_ANCHOR );

        AdminUser user = AdminUserHome.findByPrimaryKey( nIdUser );
        
        // Remove User Fields
        List<IAttribute> listAttributes = AttributeHome.findPluginAttributes( ProfilesPlugin.PLUGIN_NAME, getLocale(  ) );
        IAttribute attribute = listAttributes.get( 0 );
        String strValue = request.getParameter( ProfilesConstants.PARAMETER_ATTRIBUTE + ProfilesConstants.UNDERSCORE + 
        		attribute.getIdAttribute(  ) );
        int nIdField = Integer.parseInt( strValue );
        List<AdminUserField> listUserFields = AdminUserFieldHome.selectUserFieldsByIdUserIdAttribute( user.getUserId(  ), 
        		attribute.getIdAttribute(  ) );
        for ( AdminUserField userField : listUserFields )
        {
        	if ( userField.getAttributeField(  ).getIdField(  ) == nIdField )
        	{
        		AdminUserFieldHome.remove( userField );
        		break;
        	}
        }
        
        // Remove profile
        ProfileHome.removeUserFromProfile( strProfileKey, nIdUser, getPlugin(  ) );
        
        // Remove rights to the user
    	for ( Right right : ProfileHome.getRightsListForProfile( strProfileKey, getPlugin(  ) ) )
    	{
    		if ( AdminUserHome.hasRight( user, right.getId(  ) ) && 
    				( user.getUserLevel(  ) > getUser(  ).getUserLevel(  ) || getUser(  ).isAdmin(  ) ) )
            {
    			AdminUserHome.removeRightForUser( nIdUser, right.getId(  ) );
            }
    	}
    	// Remove roles to the user
    	for ( AdminRole role : ProfileHome.getRolesListForProfile( strProfileKey, getPlugin(  ) ) )
    	{
    		if ( AdminUserHome.hasRole( user, role.getKey(  ) ) )
            {
    			AdminUserHome.removeRoleForUser( nIdUser, role.getKey(  ) );
            }
    	}
    	// Remove workgroups to the user
    	for ( AdminWorkgroup workgroup : ProfileHome.getWorkgroupsListForProfile( strProfileKey, getPlugin(  ) ) )
    	{
    		if ( AdminWorkgroupHome.isUserInWorkgroup( user, workgroup.getKey(  ) ) )
            {
    			AdminWorkgroupHome.removeUserFromWorkgroup( user, workgroup.getKey(  ) );
            }
    	}

        return JSP_ASSIGN_USERS_PROFILE + ProfilesConstants.INTERROGATION_MARK + ProfilesConstants.PARAMETER_PROFILE_KEY + 
        		ProfilesConstants.EQUAL + strProfileKey + ProfilesConstants.SHARP + strAnchor;
    }

    /* ASSIGN VIEW */
    
    /**
     * Returns the view assignation form
     * 
     * @param request HttpServletRequest
     * @return the html code
     */
    public String getAssignViewProfile( HttpServletRequest request )
    {
    	Map<String, Object> model = new HashMap<String, Object>(  );
    	setPageTitleProperty( ProfilesConstants.PROPERTY_ASSIGN_VIEW_PROFILE_PAGETITLE );
        
        // PROFILE
        String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
        Profile profile = ProfileHome.findByPrimaryKey( strProfileKey, getPlugin(  ) );
        
        // ASSIGNED VIEW
        View assignedView = ProfileHome.getViewForProfile( strProfileKey, getPlugin(  ) );
        
        ReferenceList listViews = ViewHome.getViewsList( getPlugin(  ) );
        
        String strPermission = ProfilesResourceIdService.PERMISSION_MANAGE_VIEW_ASSIGNMENT;
        boolean bPermission = RBACService.isAuthorized( Profile.RESOURCE_TYPE, strProfileKey, strPermission, getUser(  ) );
        
        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_ASSIGN_VIEW_PROFILE;
        UrlItem url = new UrlItem( strBaseUrl );
        
        // ITEM NAVIGATION
        ItemNavigator itemNavigator = ProfilesService.getInstance(  ).getItemNavigator( profile, url, getPlugin(  ) );
        
        // PERMISSIONS
    	List<ProfileAction> listActions = ProfilesService.getInstance(  ).getListActions( getUser(  ), profile, 
    			strPermission, getLocale(  ), getPlugin(  ) );
        profile.setActions( listActions );
        
        model.put( ProfilesConstants.MARK_PROFILE, profile );
        model.put( ProfilesConstants.MARK_AVAILABLE_LIST, listViews );
        model.put( ProfilesConstants.MARK_ITEM_NAVIGATOR, itemNavigator );
        model.put( ProfilesConstants.MARK_PERMISSION, bPermission );
        model.put( ProfilesConstants.MARK_ASSIGNED_VIEW, assignedView );
        
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ASSIGN_VIEW_PROFILE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }
    
    /**
     * Process the data capture form for assign a view to a profile
     * 
     * @param request HttpServletRequest
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return The Jsp URL of the process result
     */
    public String doAssignViewProfile( HttpServletRequest request )
    	throws AccessDeniedException
    {
    	String strReturn;

        String strActionCancel = request.getParameter( ProfilesConstants.PARAMETER_CANCEL );

        if ( strActionCancel != null )
        {
            strReturn = JSP_MANAGE_PROFILES;
        }
        else
        {
        	String strProfileKey = request.getParameter( ProfilesConstants.PARAMETER_PROFILE_KEY );
        	
        	if ( !RBACService.isAuthorized( Profile.RESOURCE_TYPE, strProfileKey,
                        ProfilesResourceIdService.PERMISSION_MANAGE_VIEW_ASSIGNMENT, getUser(  ) ) )
            {
                throw new AccessDeniedException(  );
            }
        	String strViewKey = request.getParameter( ProfilesConstants.PARAMETER_VIEW_KEY );
        	if ( strViewKey != null )
        	{
        		if ( strViewKey.equals( ProfilesConstants.EMPTY_STRING ) )
        		{
        			ProfileHome.removeView( strProfileKey, getPlugin(  ) );
        		}
        		else
        		{
        			ProfileHome.removeView( strProfileKey, getPlugin(  ) );
        			ViewHome.addProfileForView( strViewKey, strProfileKey, getPlugin(  ) );
        		}
        	}
        	strReturn = JSP_ASSIGN_VIEW_PROFILE + ProfilesConstants.INTERROGATION_MARK + 
    				ProfilesConstants.PARAMETER_PROFILE_KEY + ProfilesConstants.EQUAL + strProfileKey;
        }
        
        return strReturn;
    }
}
