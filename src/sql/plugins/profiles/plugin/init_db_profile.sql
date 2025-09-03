-- liquibase formatted sql
-- changeset profiles:init_db_profile.sql
-- preconditions onFail:MARK_RAN onError:WARN
--
-- Init  table profile_action
--
INSERT INTO profile_action (id_action, name_key, description_key, action_url, icon_url, action_permission) VALUES (1,'profiles.action.modify_profile.name','profiles.action.modify_profile.description','jsp/admin/plugins/profiles/ModifyProfile.jsp','edit','MODIFY_PROFILE');
INSERT INTO profile_action (id_action, name_key, description_key, action_url, icon_url, action_permission) VALUES (2,'profiles.action.delete_profile.name','profiles.action.delete_profile.description','jsp/admin/plugins/profiles/RemoveProfile.jsp','trash','DELETE_DELETE');
INSERT INTO profile_action (id_action, name_key, description_key, action_url, icon_url, action_permission) VALUES (3,'profiles.action.manage_users_assignment.name','profiles.action.manage_users_assignment.description','jsp/admin/plugins/profiles/AssignUsersProfile.jsp','user','MANAGE_USERS_ASSIGNMENT');
INSERT INTO profile_action (id_action, name_key, description_key, action_url, icon_url, action_permission) VALUES (4,'profiles.action.manage_rights_assignment.name','profiles.action.manage_rights_assignment.description','jsp/admin/plugins/profiles/AssignRightsProfile.jsp','lock','MANAGE_RIGHTS_ASSIGNMENT');
INSERT INTO profile_action (id_action, name_key, description_key, action_url, icon_url, action_permission) VALUES (5,'profiles.action.manage_roles_assignment.name','profiles.action.manage_roles_assignment.description','jsp/admin/plugins/profiles/AssignRolesProfile.jsp','th-list','MANAGE_ROLES_ASSIGNMENT');
INSERT INTO profile_action (id_action, name_key, description_key, action_url, icon_url, action_permission) VALUES (6,'profiles.action.manage_workgroups_assignment.name','profiles.action.manage_workgroups_assignment.description','jsp/admin/plugins/profiles/AssignWorkgroupsProfile.jsp','users-group','MANAGE_WORKGROUPS_ASSIGNMENT');
INSERT INTO profile_action (id_action, name_key, description_key, action_url, icon_url, action_permission) VALUES (7,'profiles.action.manage_view_assignment.name','profiles.action.manage_view_assignment.description','jsp/admin/plugins/profiles/AssignViewProfile.jsp','eye','MANAGE_VIEW_ASSIGNMENT');
	
	
--
-- Init  table profile_view_action
--	
INSERT INTO profile_view_action (id_action, name_key, description_key, action_url, icon_url, action_permission) VALUES (1, 'profiles.action.modify_view.name', 'profiles.action.modify_view.description', 'jsp/admin/plugins/profiles/ModifyView.jsp', 'edit', 'MODIFY_VIEW');
INSERT INTO profile_view_action (id_action, name_key, description_key, action_url, icon_url, action_permission) VALUES (2, 'profiles.action.delete_view.name', 'profiles.action.delete_view.description', 'jsp/admin/plugins/profiles/RemoveView.jsp', 'trash', 'DELETE_VIEW');
INSERT INTO profile_view_action (id_action, name_key, description_key, action_url, icon_url, action_permission) VALUES (3, 'profiles.action.manage_views_assignment.name', 'profiles.action.manage_views_assignment.description', 'jsp/admin/plugins/profiles/AssignProfilesView.jsp', 'user-scan', 'MANAGE_PROFILES_ASSIGNMENT');
INSERT INTO profile_view_action (id_action, name_key, description_key, action_url, icon_url, action_permission) VALUES (4, 'profiles.action.manage_dashboards.name', 'profiles.action.manage_dashboards.description', 'jsp/admin/plugins/profiles/ManageDashboards.jsp', 'wrench', 'MANAGE_DASHBOARDS');
