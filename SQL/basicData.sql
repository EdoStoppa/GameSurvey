# Synthetic data used to test the system

# UserTable Data
INSERT INTO UserTable (username, passw, email, totPoints) VALUES ("a", "a", "a@gmail.com", 0);
INSERT INTO UserTable (username, passw, email, totPoints) VALUES ("b", "b", "b@gmail.com", 0);
INSERT INTO UserTable (username, passw, email, totPoints) VALUES ("c", "c", "c@gmail.com", 0);
INSERT INTO UserTable (username, passw, email, totPoints) VALUES ("d", "d", "d@gmail.com", 0);

INSERT INTO UserTable (username, passw, email, totPoints) VALUES ("admin", "admin", "admin@gmail.com", 0);

# AdminTable Data
INSERT INTO AdminTable (userId) VALUES (5);

# Product Data
# Every image has to be in the path written in the secure_file_priv system variable, if there's none chose. every image has to be a png
# The standard value for the secure_file_priv is '/var/lib/mysql-files/'
INSERT INTO Product(prodName, prodPhoto) VALUES("Product One", LOAD_FILE("/var/lib/mysql-files/image.png"));
INSERT INTO Product(prodName, prodPhoto) VALUES("Product Two", LOAD_FILE("/var/lib/mysql-files/image.png"));
INSERT INTO Product(prodName, prodPhoto) VALUES("Product Three", LOAD_FILE("/var/lib/mysql-files/image.png"));
INSERT INTO Product(prodName, prodPhoto) VALUES("Product Four", LOAD_FILE("/var/lib/mysql-files/image.png"));
INSERT INTO Product(prodName, prodPhoto) VALUES("Product Five", LOAD_FILE("/var/lib/mysql-files/image.png"));

# ProductOfDay Data
INSERT INTO ProdOfDay(prodID, chosenDate) VALUES (1, '20200101');
INSERT INTO ProdOfDay(prodID, chosenDate) VALUES (2, '20210101');
INSERT INTO ProdOfDay(prodID, chosenDate) VALUES (3, '20220202');
INSERT INTO ProdOfDay(prodID, chosenDate) VALUES (4, '20230202');

# Question
INSERT INTO Question (prodOfDayId, question) VALUES (1, "Question 1");
INSERT INTO Question (prodOfDayId, question) VALUES (1, "Question 2");
INSERT INTO Question (prodOfDayId, question) VALUES (2, "Question 1");
INSERT INTO Question (prodOfDayId, question) VALUES (2, "Question 2");
INSERT INTO Question (prodOfDayId, question) VALUES (3, "Question 1");
INSERT INTO Question (prodOfDayId, question) VALUES (4, "Question 2");

# AnswerLog Data
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed) VALUES (1, 1, '20200101', false);
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed, points) VALUES (1, 1, '20200101', true, 8);
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed) VALUES (2, 1, '20200101', false);
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed, points) VALUES (3, 1, '20200101', true, 8);

INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed) VALUES (1, 2, '20210101', false);
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed) VALUES (3, 2, '20210101', false);
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed, points) VALUES (1, 2, '20210101', true, 2);
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed, points) VALUES (2, 2, '20210101', true, 2);
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed, points) VALUES (3, 2, '20210101', true, 2);

# Stat Data
INSERT INTO Stat (logId, sex, age, expertise) VALUES (2, "Female", 18, "Low");
INSERT INTO Stat (logId, sex, age, expertise) VALUES (4, "Male", 18, "Low");

# FullAnswer Data
INSERT INTO FullAnswer (logId, questId, answer) VALUES (2, 1, "Answer 1 a");
INSERT INTO FullAnswer (logId, questId, answer) VALUES (2, 2, "Answer 2 a");
INSERT INTO FullAnswer (logId, questId, answer) VALUES (4, 1, "Answer 1 b");
INSERT INTO FullAnswer (logId, questId, answer) VALUES (4, 2, "Answer 2 b");

INSERT INTO FullAnswer (logId, questId, answer) VALUES (7, 3, "Answer 1 c");
INSERT INTO FullAnswer (logId, questId, answer) VALUES (7, 4, "Answer 2 c");
INSERT INTO FullAnswer (logId, questId, answer) VALUES (8, 3, "Answer 1 d");
INSERT INTO FullAnswer (logId, questId, answer) VALUES (8, 4, "Answer 2 d");
INSERT INTO FullAnswer (logId, questId, answer) VALUES (9, 3, "Answer 1 e");
INSERT INTO FullAnswer (logId, questId, answer) VALUES (9, 4, "Answer 2 e");

# Review
INSERT INTO Review (userId, prodId, reviewText) VALUES (1, 1, "Wow!");
INSERT INTO Review (userId, prodId, reviewText) VALUES (2, 1, "This is super!!!");
INSERT INTO Review (userId, prodId, reviewText) VALUES (3, 1, "Magnificient!");
INSERT INTO Review (userId, prodId, reviewText) VALUES (4, 1, "Awful experience");

INSERT INTO Review (userId, prodId, reviewText) VALUES (1, 2, "That was ok");
INSERT INTO Review (userId, prodId, reviewText) VALUES (2, 2, "Very good!");
INSERT INTO Review (userId, prodId, reviewText) VALUES (3, 2, "Too expensive!");

# Offensive words
INSERT INTO OffensiveWord(word) VALUES ("very");
INSERT INTO OffensiveWord(word) VALUES ("offensive");
INSERT INTO OffensiveWord(word) VALUES ("word");

# If all data is inserted from here, the Leaderboard table must be cleaned
DELETE FROM Leaderboard;