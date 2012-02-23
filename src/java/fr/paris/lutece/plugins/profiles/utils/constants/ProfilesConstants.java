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
package fr.paris.lutece.plugins.profiles.utils.constants;


/**
 *
 * ProfilesConstants
 *
 */
public final class ProfilesConstants
{
    // Constants
    public static final String EQUAL = "=";
    public static final String AMPERSAND = "&";
    public static final String PERCENT = "%";
    public static final String SPACE = " ";
    public static final String OPEN_BRACKET = "(";
    public static final String CLOSED_BRACKET = ")";
    public static final String UNDERSCORE = "_";
    public static final String INTERROGATION_MARK = "?";
    public static final String SHARP = "#";
    public static final String LAST_NAME = "last_name";
    public static final String ALL = "ALL";
    public static final int CONSTANTE_FIRST_ORDER = 1;

    // BEANS
    public static final String BEAN_PROFILES_SERVICE = "profiles.profilesService";
    public static final String BEAN_PROFILE_ACTION_SERVICE = "profiles.profileActionService";
    public static final String BEAN_VIEWS_SERVICE = "profiles.viewsService";
    public static final String BEAN_VIEW_ACTION_SERVICE = "profiles.viewActionService";

    // PROPERTIES
    public static final String PROPERTY_ENCODING_URL = "lutece.encoding.url";
    public static final String PROPERTY_MANAGE_PROFILES_PAGETITLE = "profiles.manage_profiles.pageTitle";
    public static final String PROPERTY_CREATE_PROFILE_PAGETITLE = "profiles.create_profile.pageTitle";
    public static final String PROPERTY_MODIFY_PROFILE_PAGETITLE = "profiles.modify_profile.pageTitle";
    public static final String PROPERTY_ITEM_PER_PAGE = "profiles.itemPerPage";
    public static final String PROPERTY_ASSIGN_RIGHTS_PROFILE_PAGETITLE = "profiles.assign_rights_profile.pageTitle";
    public static final String PROPERTY_ASSIGN_WORKGROUPS_PROFILE_PAGETITLE = "profiles.assign_workgroups_profile.pageTitle";
    public static final String PROPERTY_ASSIGN_ROLES_PROFILE_PAGETITLE = "profiles.assign_roles_profile.pageTitle";
    public static final String PROPERTY_ASSIGN_USERS_PROFILE_PAGETITLE = "profiles.assign_users_profile.pageTitle";
    public static final String PROPERTY_ASSIGN_RIGHTS_PROFILE_LABEL_LEVEL = "profiles.assign_rights_profile.labelLevel";
    public static final String PROPERTY_PROFILE_ATTRIBUTED = "profiles.message.profileAttributed";
    public static final String PROPERTY_NO_MULTIPLE_PROFILES = "profiles.message.noMultipleProfiles";
    public static final String PROPERTY_MANAGE_VIEWS_PAGETITLE = "profiles.manage_views.pageTitle";
    public static final String PROPERTY_CREATE_VIEW_PAGETITLE = "profiles.create_view.pageTitle";
    public static final String PROPERTY_MODIFY_VIEW_PAGETITLE = "profiles.modify_view.pageTitle";
    public static final String PROPERTY_VIEW_ATTRIBUTED = "profiles.message.viewAttributed";
    public static final String PROPERTY_NO_MULTIPLE_VIEWS = "profiles.message.noMultipleViews";
    public static final String PROPERTY_ASSIGN_PROFILES_VIEW_PAGETITLE = "profiles.assign_profiles_view.pageTitle";
    public static final String PROPERTY_ASSIGN_VIEW_PROFILE_PAGETITLE = "profiles.assign_view_profile.pageTitle";

    // MARKS 
    public static final String MARK_LIST_PROFILES = "list_profiles";
    public static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    public static final String MARK_PAGINATOR = "paginator";
    public static final String MARK_SEARCH_FILTER = "search_filter";
    public static final String MARK_SEARCH_IS_SEARCH = "search_is_search";
    public static final String MARK_SORT_SEARCH_ATTRIBUTE = "sort_search_attribute";
    public static final String MARK_PROFILE = "profile";
    public static final String MARK_PERMISSION = "permission";
    public static final String MARK_ITEM_NAVIGATOR = "item_navigator";
    public static final String MARK_USER_LEVELS = "user_levels";
    public static final String MARK_AVAILABLE_LIST = "available_list";
    public static final String MARK_ASSIGNED_LIST = "assigned_list";
    public static final String MARK_ASSIGNED_NUMBER = "assigned_number";
    public static final String MARK_ATTRIBUTE = "attribute";
    public static final String MARK_ATTRIBUTE_FIELD = "attribute_field";
    public static final String MARK_LIST_VIEWS = "list_views";
    public static final String MARK_VIEW = "view";
    public static final String MARK_LOCALE = "locale";
    public static final String MARK_ASSIGNED_VIEW = "assigned_view";
    public static final String MARK_MAP_DASHBOARDS = "map_dashboards";
    public static final String MARK_NOT_SET_DASHBOARDS = "not_set_dashboards";
    public static final String MARK_COLUMN_COUNT = "column_count";
    public static final String MARK_LIST_AVAILABLE_COLUMNS = "list_available_columns";
    public static final String MARK_MAP_AVAILABLE_ORDERS = "map_available_orders";
    public static final String MARK_MAP_COLUMN_ORDER_STATUS = "map_column_order_status";

    // PARAMETERS
    public static final String PARAMETER_SEARCH_IS_SEARCH = "search_is_search";
    public static final String PARAMETER_SEARCH_KEY = "search_key";
    public static final String PARAMETER_SEARCH_DESCRIPTION = "search_description";
    public static final String PARAMETER_PROFILE_KEY = "profile_key";
    public static final String PARAMETER_PROFILE_DESCRIPTION = "profile_description";
    public static final String PARAMETER_CANCEL = "cancel";
    public static final String PARAMETER_ANCHOR = "anchor";
    public static final String PARAMETER_RIGHTS_LIST = "rights_list";
    public static final String PARAMETER_ID_RIGHT = "id_right";
    public static final String PARAMETER_WORKGROUPS_LIST = "workgroups_list";
    public static final String PARAMETER_WORKGROUP_KEY = "workgroup_key";
    public static final String PARAMETER_ROLES_LIST = "roles_list";
    public static final String PARAMETER_ROLE_KEY = "role_key";
    public static final String PARAMETER_USERS_LIST = "users_list";
    public static final String PARAMETER_ID_USER = "id_user";
    public static final String PARAMETER_ATTRIBUTE = "attribute";
    public static final String PARAMETER_VIEW_KEY = "view_key";
    public static final String PARAMETER_VIEW_DESCRIPTION = "view_description";
    public static final String PARAMETER_PROFILES_LIST = "profiles_list";
    public static final String PARAMETER_DASHBOARD_NAME = "dashboard_name";
    public static final String PARAMETER_DASHBOARD_COLUMN = "dashboard_column";
    public static final String PARAMETER_DASHBOARD_ORDER = "dashboard_order";
    public static final String PARAMETER_COLUMN = "column";
    public static final String PARAMETER_MODIFY_PROFILE = "modify_profile";
    public static final String PARAMETER_ASSIGN_RIGHT = "assign_right";
    public static final String PARAMETER_ASSIGN_WORKGROUP = "assign_workgroup";
    public static final String PARAMETER_ASSIGN_ROLE = "assign_role";
    public static final String PARAMETER_ASSIGN_VIEW = "assign_view";
    public static final String PARAMETER_ASSIGN_USER = "assign_user";
    public static final String PARAMETER_MODIFY_VIEW = "modify_view";
    public static final String PARAMETER_ASSIGN_PROFILE = "assign_profile";
    public static final String PARAMETER_ASSIGN_DASHBOARD = "assign_dashboard";

    // MESSAGES
    public static final String MESSAGE_PROFILE_ALREADY_EXISTS = "profiles.message.profileAlreadyExists";
    public static final String MESSAGE_ACCENTUATED_CHARACTER = "profiles.message.accentuatedCharacter";
    public static final String MESSAGE_CONFIRM_REMOVE_PROFILE = "profiles.message.confirmRemoveProfile";
    public static final String MESSAGE_VIEW_ALREADY_EXISTS = "profiles.message.viewAlreadyExists";
    public static final String MESSAGE_CONFIRM_REMOVE_VIEW = "profiles.message.confirmRemoveView";
    public static final String MESSAGE_DASHBOARD_NOT_FOUND = "portal.dashboard.message.dashboardNotFound";

    /**
     * Private constructor
     */
    private ProfilesConstants(  )
    {
    }
}
