# This is to make sure that the event scheduler is active (otherwise the event won't run)
SET GLOBAL event_scheduler = ON;

# Commands needed to sanitize eveything if this isn't a new DB
DROP EVENT IF EXISTS deleteLeaderboardDaily;

DROP TRIGGER IF EXISTS db2Project.IncreaseTotPoints;
DROP TRIGGER IF EXISTS db2Project.DecreaseTotPoints;
DROP TRIGGER IF EXISTS db2Project.AddToLeaderBoard;
DROP TRIGGER IF EXISTS db2Project.DeleteAnswerOfSurvey;



# Set the delimiter for events
DELIMITER $$

# EVENTS

# This event will clean the leaderboard every day at midnight, starting from the day after it's executed

CREATE EVENT deleteLeaderboardDaily
ON SCHEDULE EVERY 1 DAY STARTS DATE_ADD(CURDATE(), INTERVAL '1 0' DAY_HOUR) DO
BEGIN
	DELETE FROM db2Project.Leaderboard;
END $$
      
      
# TRIGGERS

CREATE TRIGGER IncreaseTotPoints
AFTER INSERT ON db2Project.AnswerLog FOR EACH ROW
BEGIN
	UPDATE db2Project.UserTable
		SET db2Project.UserTable.totPoints = db2Project.UserTable.totPoints + NEW.points
		WHERE db2Project.UserTable.userId = NEW.userId;
END $$

CREATE TRIGGER DecreaseTotPoints
BEFORE DELETE ON AnswerLog FOR EACH ROW
BEGIN
	UPDATE db2Project.UserTable
		SET db2Project.UserTable.totPoints = db2Project.UserTable.totPoints - OLD.points
		WHERE db2Project.UserTable.userId = OLD.userId;
END $$

CREATE TRIGGER AddToLeaderBoard 
AFTER INSERT ON db2Project.AnswerLog FOR EACH ROW  
BEGIN
  IF(NEW.confirmed = TRUE) THEN
	INSERT INTO db2Project.Leaderboard(userId) VALUE (NEW.userId);
  END IF;
END $$

# This trigger is needed because using ON DELETE CASCADE on prodOfDayId doesn't execute all the triggers on AnswerLog
CREATE TRIGGER DeleteAnswerOfSurvey 
BEFORE DELETE ON db2Project.ProdOfDay FOR EACH ROW  
BEGIN
	DELETE FROM db2Project.AnswerLog
    WHERE 
		OLD.prodOfDayId = db2Project.AnswerLog.prodOfDayId;
END$$

# Reset the Delimiter
DELIMITER ;