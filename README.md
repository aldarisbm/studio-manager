# studio-manager
Manager for studios(dance, yoga, pilates) or any rented space.
### Overview
Studio Manager-

Studio Owners will be able to keep organized different instructors and students, schedule private, group classes and be
able to create new users with a login and password with different user levels

### Features
User Login: Users will be able to create accounts and login to the application

User levels: Superusers will be able to create both instructors and students, Instructors will be able to create students
and students will be able to schedule privates with instructors.

Oauth2: Letting people login with third party accounts

### Technologies
Java

SpringMVC

MySQL

thymeleaf

### Project Tracker:
Pivotal Tracker
https://www.pivotaltracker.com/n/projects/2185453

### Run it
Use Java 10

After you clone from github repo, you should build with gradle -Usually have auto-import checked and use gradle 'wrapper' configuration.

After it boots up, in MAMP or whichever MYSQL db you're using to connect you need to add the following two commands(can be added both at once) from the root folder of the DB


##### INSERT INTO `role` VALUES (1,'ADMIN');
##### INSERT INTO `role` VALUES (2,'USER');
