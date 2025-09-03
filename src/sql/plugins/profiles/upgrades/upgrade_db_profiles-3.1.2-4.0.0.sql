-- liquibase formatted sql
-- changeset profiles:upgrade_db_profiles-3.1.2-4.0.0.sql
-- preconditions onFail:MARK_RAN onError:WARN
UPDATE profile_action SET icon_url="users-group" WHERE id_action=6;