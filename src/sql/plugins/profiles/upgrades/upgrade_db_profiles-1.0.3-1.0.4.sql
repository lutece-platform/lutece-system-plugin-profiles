DROP TABLE IF EXISTS profile_view_action;
CREATE TABLE IF NOT EXISTS profile_view_action (
  id_action int(11) NOT NULL DEFAULT '0',
  name_key varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  description_key varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  action_url varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  icon_url varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  action_permission varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (id_action)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO profile_view_action (id_action, name_key, description_key, action_url, icon_url, action_permission) VALUES (1, 'profiles.action.modify_view.name', 'profiles.action.modify_view.description', 'jsp/admin/plugins/profiles/ModifyView.jsp', 'icon-edit', 'MODIFY_VIEW');
INSERT INTO profile_view_action (id_action, name_key, description_key, action_url, icon_url, action_permission) VALUES (2, 'profiles.action.delete_view.name', 'profiles.action.delete_view.description', 'jsp/admin/plugins/profiles/RemoveView.jsp', 'icon-trash', 'DELETE_VIEW');
INSERT INTO profile_view_action (id_action, name_key, description_key, action_url, icon_url, action_permission) VALUES (3, 'profiles.action.manage_views_assignment.name', 'profiles.action.manage_views_assignment.description', 'jsp/admin/plugins/profiles/AssignProfilesView.jsp', 'icon-folder-close', 'MANAGE_PROFILES_ASSIGNMENT');
INSERT INTO profile_view_action (id_action, name_key, description_key, action_url, icon_url, action_permission) VALUES (4, 'profiles.action.manage_dashboards.name', 'profiles.action.manage_dashboards.description', 'jsp/admin/plugins/profiles/ManageDashboards.jsp', 'icon-wrench', 'MANAGE_DASHBOARDS');
