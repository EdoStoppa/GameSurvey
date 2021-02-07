# Basic User data insertion

# This bit of code is added to "clean" the needed tables
# DELETE FROM UserTable;

# UserTable Data
INSERT INTO UserTable (username, passw, email, totPoints) VALUES ("admin", "admin", "admin@gmail.com", 5);
INSERT INTO UserTable (username, passw, email, totPoints) VALUES ("a", "a", "a@gmail.com", 2);
INSERT INTO UserTable (username, passw, email, totPoints) VALUES ("user1", "user1pass", "user1@gmail.com", 5);
INSERT INTO UserTable (username, passw, email, totPoints) VALUES ("user2", "user2pass", "user2@gmail.com", 10);
INSERT INTO UserTable (username, passw, email, totPoints) VALUES ("user3", "user3pass", "user3@gmail.com", 4);

# AdminTable Data
INSERT INTO AdminTable (userId) VALUES (1);

# Product Data
# Every image has to be in the path written in the secure_file_priv system variable, if there's none chose. every image has to be a png
# The standard value for the secure_file_priv is '/var/lib/mysql-files/'
INSERT INTO Product(prodName, prodPhoto) VALUES("Product One", LOAD_FILE("/var/lib/mysql-files/image.png"));
INSERT INTO Product(prodName, prodPhoto) VALUES("Product Two", LOAD_FILE("/var/lib/mysql-files/image.png"));
INSERT INTO Product(prodName, prodPhoto) VALUES("Product Three", LOAD_FILE("/var/lib/mysql-files/image.png"));
INSERT INTO Product(prodName, prodPhoto) VALUES("Product Four", LOAD_FILE("/var/lib/mysql-files/image.png"));
INSERT INTO Product(prodName, prodPhoto) VALUES("Product Five", LOAD_FILE("/var/lib/mysql-files/image.png"));

# Question
INSERT INTO Question (prodOfDayId, question) VALUES (1, "Domanda 1");
INSERT INTO Question (prodOfDayId, question) VALUES (1, "Domanda 2");
INSERT INTO Question (prodOfDayId, question) VALUES (1, "Domanda 3");
INSERT INTO Question (prodOfDayId, question) VALUES (1, "Domanda 4");
INSERT INTO Question (prodOfDayId, question) VALUES (1, "Domanda 5");
INSERT INTO Question (prodOfDayId, question) VALUES (1, "Domanda 6");

# ProductOfDay Data
INSERT INTO ProdOfDay(prodID, chosenDate) VALUES (1, curdate());
INSERT INTO ProdOfDay(prodID, chosenDate) VALUES (2, '19710101');
INSERT INTO ProdOfDay(prodID, chosenDate) VALUES (3, '19710103');
INSERT INTO ProdOfDay(prodID, chosenDate) VALUES (4, '19710104');
INSERT INTO ProdOfDay(prodID, chosenDate) VALUES (5, '19710105');
INSERT INTO ProdOfDay(prodID, chosenDate) VALUES (5, '20220202');
INSERT INTO ProdOfDay(prodID, chosenDate) VALUES (5, '20230202');

# AnswerLog Data
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed) VALUES (1, 1, '19710101', true);
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed) VALUES (2, 2, '19710101', false);
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed) VALUES (3, 3, '19710101', true);
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed) VALUES (4, 1, '19710101', false);
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed) VALUES (5, 1, '19710101', true);

# FullAnswer Data
INSERT INTO FullAnswer (logId, questId, answer) VALUES (1, 1, "Ciao come va 1");
INSERT INTO FullAnswer (logId, questId, answer) VALUES (1, 2, "Ciao come va 2");
INSERT INTO FullAnswer (logId, questId, answer) VALUES (1, 3, "Ciao come va 3");
INSERT INTO FullAnswer (logId, questId, answer) VALUES (1, 4, "Ciao come va 4");
INSERT INTO FullAnswer (logId, questId, answer) VALUES (1, 5, "Ciao come va 5");
INSERT INTO FullAnswer (logId, questId, answer) VALUES (1, 6, "Ciao come va 6");

# Leaderboard
INSERT INTO Leaderboard (userId) VALUE (3);
INSERT INTO Leaderboard (userId) VALUE (5);

# Review
INSERT INTO Review (userId, prodOfDayId, reviewText) VALUES (1, 1, "Wow");
INSERT INTO Review (userId, prodOfDayId, reviewText) VALUES (2, 1, "Super");
INSERT INTO Review (userId, prodOfDayId, reviewText) VALUES (3, 1, "Magnificient");
INSERT INTO Review (userId, prodOfDayId, reviewText) VALUES (4, 1, "Awful");

# Offensive words
INSERT INTO OffensiveWord(word) VALUES ("very");
INSERT INTO OffensiveWord(word) VALUES ("offensive");
INSERT INTO OffensiveWord(word) VALUES ("word");
