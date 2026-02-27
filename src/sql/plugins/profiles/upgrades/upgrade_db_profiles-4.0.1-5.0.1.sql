-- liquibase formatted sql
-- changeset profiles:upgrade_db_profiles-4.0.1-5.0.1.sql
-- preconditions onFail:MARK_RAN onError:WARN
UPDATE core_admin_right SET icon_url='ti ti-user-cog' WHERE  id_right='PROFILES_MANAGEMENT';
UPDATE core_admin_right SET icon_url='ti ti-user-pin' WHERE  id_right='PROFILES_VIEWS_MANAGEMENT';