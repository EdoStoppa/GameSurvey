# Game Survey

> This is a web application created for the course of Data Bases 2 @ PoliMi in 2020-2021. 
> The main focus of the project is the Data base design part and the correct use and integration of JPA to work with the DB. The application is realized using 
> Java Enterprise Edition, MySQL, TomEE, and Java Servlets combined with templating thorugh Thymeleaf for the Web part.

This application deals with gamified consumer data collection. Each day may be available a "Questionnaire of the day" about a product in the data base 
to all registered user. The questionnaire will be divided in two sections: marketing and statistical. The first will pose multiple marketing questions about the
product of the day, the second will ask the user to provide some data about their sex, age and level of expertise about the product. The first 
section is mandatory, but the second isn't. They may decide to take the survey and if the complete it, they'll be rewarded with some points. These points will 
be added to their personal score, and displayed on the Leaderboard accesible from the hompage of the application.

Admins have access to a different application where multiple functions are available:
1. Create a new "Questionnaire of the day". The admin will choose the product, the day in which the questionnaire will be displayed and the questions to pose to
all users. A new questionnaire can be created only for the currrent or future date, not for past dates.
2. Inspect past questionnaires visualizing the list of users who submitted a specific questionnaire,the list of users who cancelled that questionnaire and the 
questionnaire answers of each user.
3. Delete past questionnaires and with them all the associated data for users (for example, their score must diminish if a completed questionnaire is deleted)
