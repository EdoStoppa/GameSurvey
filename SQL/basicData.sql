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
INSERT INTO Product(prodName, prodPhoto) VALUES('<-- Product name -->', LOAD_FILE('<-- images path -->'));
