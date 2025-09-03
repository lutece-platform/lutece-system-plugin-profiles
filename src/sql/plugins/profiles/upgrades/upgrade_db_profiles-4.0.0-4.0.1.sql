-- liquibase formatted sql
-- changeset profiles:upgrade_db_profiles-4.0.0-4.0.1.sql
-- preconditions onFail:MARK_RAN onError:WARN
UPDATE profile_view_action SET icon_url='user-scan' WHERE id_action=3;