# This code will generate every standard table in the database

DROP TABLE IF EXISTS UserTable;
CREATE TABLE UserTable(
userId INT NOT NULL AUTO_INCREMENT, 
username varchar(45) NOT NULL,
passw varchar(45) NOT NULL,
email varchar(45) NOT NULL,
blocked BOOLEAN DEFAULT FALSE,
totPoints INT DEFAULT 0,
PRIMARY KEY(userId)
);

DROP TABLE IF EXISTS AdminTable;
CREATE TABLE AdminTable(
accountId INT NOT NULL AUTO_INCREMENT,
userId INT NOT NULL,
PRIMARY KEY(accountId)
);

DROP TABLE IF EXISTS Product;
CREATE TABLE Product(
prodId INT NOT NULL AUTO_INCREMENT, 
prodName varchar(45) NOT NULL,
prodPhoto BLOB NOT NULL,
PRIMARY KEY(prodId)
);

DROP TABLE IF EXISTS ProdOfDay;
CREATE TABLE ProdOfDay(
prodOfDayId INT NOT NULL AUTO_INCREMENT, 
prodID INT NOT NULL REFERENCES Product(prodId) ON UPDATE CASCADE ON DELETE NO ACTION,
chosenDate DATE NOT NULL,
PRIMARY KEY(prodOfDayId)
);

DROP TABLE IF EXISTS Question;
CREATE TABLE Question(
questId INT NOT NULL AUTO_INCREMENT, 
prodOfDayID INT NOT NULL REFERENCES ProdOfDay(prodOfDayId) ON UPDATE CASCADE ON DELETE NO ACTION,
question varchar(255) NOT NULL,
PRIMARY KEY(questId)
);

DROP TABLE IF EXISTS AnswerLog;
CREATE TABLE AnswerLog(
logId INT NOT NULL AUTO_INCREMENT,
userId INT NOT NULL REFERENCES UserTable(userId) ON UPDATE CASCADE ON DELETE NO ACTION, 
prodOfDayID INT NOT NULL REFERENCES ProdOfDay(prodOfDayId) ON UPDATE CASCADE ON DELETE NO ACTION,
logTime TIMESTAMP NOT NULL,
confirmed BOOLEAN NOT NULL,
points INT NOT NULL DEFAULT 0,
PRIMARY KEY(logId)
);

DROP TABLE IF EXISTS FullAnswer;
CREATE TABLE FullAnswer(
logId INT NOT NULL REFERENCES AnswerLog(logId) ON UPDATE CASCADE ON DELETE NO ACTION, 
questId INT NOT NULL REFERENCES Question(questId) ON UPDATE CASCADE ON DELETE NO ACTION,
answer varchar(255) NOT NULL,
PRIMARY KEY(logId, questId)
);

DROP TABLE IF EXISTS Stat;
CREATE TABLE Stat(
statId INT NOT NULL AUTO_INCREMENT,
logId INT NOT NULL REFERENCES AnswerLog(logId) ON UPDATE CASCADE ON DELETE NO ACTION, 
sex ENUM('Male', 'Female', 'Other') DEFAULT NULL,
age TINYINT UNSIGNED DEFAULT NULL,
expertise ENUM('Low', 'Medium', 'High') DEFAULT NULL,
PRIMARY KEY(statId)
);

DROP TABLE IF EXISTS Leaderboard;
CREATE TABLE Leaderboard(
entryId INT NOT NULL AUTO_INCREMENT,
userId INT NOT NULL REFERENCES UserTable(userId) ON UPDATE CASCADE ON DELETE NO ACTION, 
PRIMARY KEY(entryId)
);