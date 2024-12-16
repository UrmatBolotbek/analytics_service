ALTER TABLE analytics_event
DROP COLUMN IF EXISTS follower_Id,
DROP COLUMN IF EXISTS followee_Id;