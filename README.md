# studio-manager
Manager for studios(dance, yoga, pilates) or any rented space.
### Overview
Studio Manager-

Studio Owners will be able to keep organized different instructors and students, schedule private, group classes and be
able to create new users with a login and password with different user levels

### Run it:
Use Java 8

After you clone from github repo, you should build with gradle -Usually have auto-import checked and use gradle 'wrapper' configuration.

After it boots up, in MAMP or whichever MYSQL db you're using to connect you need to add the following two commands(can be added both at once) from the root folder of the DB


##### INSERT INTO `role` VALUES (1,'ADMIN');
##### INSERT INTO `role` VALUES (2,'USER');
