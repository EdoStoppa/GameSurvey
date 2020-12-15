# Basic User data insertion

# This bit of code is added to "clean" the needed tables
# DELETE FROM UserTable;

# UserTable Data
INSERT INTO UserTable (username, passw, email) VALUES ("admin", "admin", "admin@gmail.com");
INSERT INTO UserTable (username, passw, email) VALUES ("a", "a", "a@gmail.com");
INSERT INTO UserTable (username, passw, email) VALUES ("user1", "user1pass", "user1@gmail.com");
INSERT INTO UserTable (username, passw, email) VALUES ("user2", "user2pass", "user2@gmail.com");
INSERT INTO UserTable (username, passw, email) VALUES ("user3", "user3pass", "user3@gmail.com");

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

# ProductOfDay Data
INSERT INTO ProdOfDay(prodID, chosenDate) VALUES (1, CURDATE());
INSERT INTO ProdOfDay(prodID, chosenDate) VALUES (2, '19710102');
INSERT INTO ProdOfDay(prodID, chosenDate) VALUES (3, '19710103');
INSERT INTO ProdOfDay(prodID, chosenDate) VALUES (4, '19710104');
INSERT INTO ProdOfDay(prodID, chosenDate) VALUES (5, '19710105');

# AnswerLog Data
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed) VALUES (1, 1, '19710101', true);
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed) VALUES (2, 1, '19710101', false);
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed) VALUES (3, 1, '19710101', true);
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed) VALUES (4, 1, '19710101', false);
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed) VALUES (5, 1, '19710101', true);
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed) VALUES (1, 2, '19710102', true);
INSERT INTO AnswerLog (userId, prodOfDayID, logTime, confirmed) VALUES (1, 3, '19710103', true);
