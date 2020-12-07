# This is to make sure that the event scheduler is active (otherwise the event won't run)
SET GLOBAL event_scheduler = ON;

# Set the delimiter for events
DELIMITER $$

# EVENTS

# This event will clean the leaderboard every day at midnight, starting from the day after it's executed
CREATE EVENT deleteLeaderboardDaily
ON SCHEDULE EVERY 1 DAY STARTS DATE_ADD(CURDATE(), INTERVAL '1 0' DAY_HOUR)
DO
	BEGIN
		DELETE FROM Leaderboard;
	END $$
      
      
# TRIGGERS

CREATE TRIGGER IncreaseTotPoints
AFTER INSERT ON AnswerLog FOR EACH ROW
BEGIN
	UPDATE UserTable
		SET totPoints = totPoints + NEW.points
		WHERE userId = NEW.userId;
END $$

CREATE TRIGGER DecreaseTotPoints
BEFORE DELETE ON AnswerLog FOR EACH ROW
BEGIN
	UPDATE UserTable
		SET totPoints = totPoints - OLD.points
		WHERE userId = OLD.userId;
END $$
    
CREATE TRIGGER AddToLeaderBoard 
AFTER INSERT ON AnswerLog FOR EACH ROW  
BEGIN
  IF(NEW.confirmed = TRUE) THEN
    INSERT INTO LeaderBoard(userId) VALUE (NEW.userId);
  END IF;
END$$

# Reset the Delimiter
DELIMITER ;