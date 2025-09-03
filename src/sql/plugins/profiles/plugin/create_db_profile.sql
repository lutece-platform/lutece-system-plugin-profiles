-- liquibase formatted sql
-- changeset profiles:create_db_profile.sql
-- preconditions onFail:MARK_RAN onError:WARN
DROP TABLE IF EXISTS profile_profile;
DROP TABLE IF EXISTS profile_user;
DROP TABLE IF EXISTS profile_right;
DROP TABLE IF EXISTS profile_role;
DROP TABLE IF EXISTS profile_workgroup;
DROP TABLE IF EXISTS profile_action;
DROP TABLE IF EXISTS profile_view;
DROP TABLE IF EXISTS profile_view_profile;
DROP TABLE IF EXISTS profile_view_action;
DROP TABLE IF EXISTS profile_view_dashboard;

--
-- Table structure for table profile_profile
--
CREATE TABLE profile_profile (
  profile_key VARCHAR(50) DEFAULT '' NOT NULL,
  profile_description VARCHAR(255),
  PRIMARY KEY (profile_key)
);

--
-- Table structure for table profile_user
--
CREATE TABLE profile_user (
  profile_key VARCHAR(50) DEFAULT '' NOT NULL,
  id_user INT DEFAULT 0 NOT NULL,
  PRIMARY KEY (profile_key, id_user)
);

--
-- Table structure for table profile_right
--
CREATE TABLE profile_right (
  profile_key VARCHAR(50) DEFAULT '' NOT NULL,
  id_right VARCHAR(50) DEFAULT '' NOT NULL,
  PRIMARY KEY(profile_key, id_right)
);
  
--
-- Table structure for table profile_role
--
CREATE TABLE profile_role (
  profile_key VARCHAR(50) DEFAULT '' NOT NULL,
  role_key VARCHAR(50) DEFAULT '' NOT NULL,
  PRIMARY KEY (profile_key, role_key)
);

--
-- Table structure for table profile_workgroup
--
CREATE TABLE profile_workgroup (
  profile_key VARCHAR(50) DEFAULT '' NOT NULL,
  workgroup_key VARCHAR(50) DEFAULT '' NOT NULL,
  PRIMARY KEY (profile_key, workgroup_key)
);

--
-- Table structure for table profile_action
--
CREATE TABLE profile_action (
  id_action INT DEFAULT 0 NOT NULL,
  name_key VARCHAR(100) DEFAULT NULL ,
  description_key VARCHAR(100) DEFAULT NULL  ,
  action_url VARCHAR(255) DEFAULT NULL ,
  icon_url VARCHAR(255) DEFAULT NULL,
  action_permission VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id_action)
);

--
-- Table structure for table profile_view
--
CREATE TABLE profile_view (
  view_key VARCHAR(50) DEFAULT '' NOT NULL,
  view_description VARCHAR(255),
  PRIMARY KEY (view_key)
);

--
-- Table structure for table profile_view_profile
--
CREATE TABLE profile_view_profile (
  view_key VARCHAR(50) DEFAULT '' NOT NULL,
  profile_key VARCHAR(50) DEFAULT '' NOT NULL,
  PRIMARY KEY(view_key, profile_key)
);

--
-- Table structure for table profile_view_action
--
CREATE TABLE profile_view_action (
  id_action INT DEFAULT 0 NOT NULL,
  name_key VARCHAR(100) DEFAULT NULL ,
  description_key VARCHAR(100) DEFAULT NULL  ,
  action_url VARCHAR(255) DEFAULT NULL ,
  icon_url VARCHAR(255) DEFAULT NULL,
  action_permission VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id_action)
);

--
-- Table structure for table profile_view_dashboard
--
CREATE TABLE profile_view_dashboard (
	view_key VARCHAR(50) DEFAULT '' NOT NULL,
	dashboard_name varchar(100) NOT NULL,
	dashboard_column int NOT NULL,
	dashboard_order int NOT NULL,
	PRIMARY KEY (view_key, dashboard_name)
);
