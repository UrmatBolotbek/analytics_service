ALTER TABLE analytics_event
DROP COLUMN if exists followeeid,
ADD COLUMN if not exists follower_Id bigint NOT NULL,
ADD COLUMN if not exists followee_Id bigint NOT NULL;