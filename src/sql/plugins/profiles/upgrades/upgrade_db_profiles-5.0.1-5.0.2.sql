-- liquibase formatted sql
-- changeset profiles:upgrade_db_profiles-5.0.1-5.0.2.sql
-- preconditions onFail:MARK_RAN onError:WARN
UPDATE profile_action SET action_permission = 'DELETE_PROFILE' WHERE action_permission = 'DELETE_DELETE';
