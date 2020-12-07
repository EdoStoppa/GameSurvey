# This is to make sure that the event scheduler is active (otherwise the event won't run)
SET GLOBAL event_scheduler = ON;


# EVENTS

# This event will clean the leaderboard every day at midnight, starting from the day after it's executed
CREATE EVENT deleteLeaderboardDaily
    ON SCHEDULE
      EVERY 1 DAY STARTS DATE_ADD(CURDATE(), INTERVAL '1 0' DAY_HOUR)
    DO
      DELETE FROM leaderboard;
      
      
# TRIGGERS

CREATE TRIGGER IncreaseTotPoints
AFTER INSERT ON AnswerLog
FOR EACH ROW
UPDATE UserTable
	SET totPoints = totPoints + NEW.points
    WHERE userId = NEW.userId;
    
CREATE TRIGGER DecreaseTotPoints
	BEFORE DELETE ON AnswerLog FOR EACH ROW
UPDATE UserTable
	SET totPoints = totPoints - OLD.points
    WHERE userId = OLD.userId;
