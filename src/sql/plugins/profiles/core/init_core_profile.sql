-- liquibase formatted sql
-- changeset profiles:init_core_profile.sql
-- preconditions onFail:MARK_RAN onError:WARN
--
-- Init  table core_admin_right
--
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url, documentation_url) VALUES ('PROFILES_MANAGEMENT','profiles.adminFeature.profiles_management.name',0,'jsp/admin/plugins/profiles/ManageProfiles.jsp','profiles.adminFeature.profiles_management.description',0,'profiles','MANAGERS','ti ti-user-cog', NULL);
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url, documentation_url) VALUES ('PROFILES_VIEWS_MANAGEMENT','profiles.adminFeature.views_management.name',0,'jsp/admin/plugins/profiles/ManageViews.jsp','profiles.adminFeature.views_management.description',0,'profiles','MANAGERS','ti ti-user-pin', NULL);

--
-- Init  table core_user_right
--
INSERT INTO core_user_right (id_right,id_user) VALUES ('PROFILES_MANAGEMENT',1);
INSERT INTO core_user_right (id_right,id_user) VALUES ('PROFILES_VIEWS_MANAGEMENT',1);

--
-- Init  table core_admin_role
--
INSERT INTO core_admin_role (role_key,role_description) VALUES ('profiles_manager', 'Profiles management');
INSERT INTO core_admin_role (role_key,role_description) VALUES ('profiles_views_manager', 'Profiles Views management');

--
-- Init  table core_admin_role_resource
--
INSERT INTO core_admin_role_resource (role_key,resource_type,resource_id,permission) VALUES ('profiles_manager','PROFILES','*','*');
INSERT INTO core_admin_role_resource (role_key,resource_type,resource_id,permission) VALUES ('profiles_views_manager','PROFILES_VIEWS','*','*');

--
-- Init  table core_user_role
--
INSERT INTO core_user_role (role_key,id_user) VALUES ('profiles_manager',1);
INSERT INTO core_user_role (role_key,id_user) VALUES ('profiles_views_manager',1);

--
-- Init  table core_attibute
--
INSERT INTO core_attribute (type_class_name, title, help_message, is_mandatory, plugin_name) VALUES ('fr.paris.lutece.portal.business.user.attribute.AttributeComboBox', 'Profil','',0,'profiles');

--
-- Init  table core_attibute_field
--
INSERT INTO core_attribute_field (id_attribute, title, DEFAULT_value, is_DEFAULT_value, height, width, max_size_enter, is_multiple, field_position) VALUES ((SELECT a.id_attribute from core_attribute a WHERE a.plugin_name = 'profiles'),NULL,NULL,0,0,0,0,1,1);
