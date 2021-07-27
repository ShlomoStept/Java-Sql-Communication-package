# Java Sql Communication Package
This project is a java class/package that greatly simplifies the communication between any Java program and a SQL Database. It was built with MySql in mind but does work with other databases

## JavaSqlCommunication 
JavaSqlCommunication is a class of functions that simplifies several commands 
1. Connection to the database: Rather than having to type in a 
    (1) URL 
    (2) Username, 
    (3) Database Name , and 
    (4) Table Name, 
each time you carry out a create, select, insert, update, or delete SQL command, this class allows the user to --> save/connect that information to an instance of this class. 
   >This simplifies programs because, if a user has several queries to make in sql with one server, one JavaSqlCommunication object instance will work for all actions, since the information is saved to data fields, when the JavaSqlCommunication instance is constructed.

2. The communication Java has with databases is rather finicky, this library reduces the need to fiddle around with Java->SQL translation by providing all the basic SQL queries.  
  A. Create (tables and columns) (delete coming soon), 
  B. Select, insert, and update for 9 data types 
    1. text, 
    2. integer, 
    3. float, 
    4. real, 
    5. date, 
    6. datetime,
    7. image, 
    8. boolean, 
    9. blob 
    10. Json --> _is coming soon_
  
3. TO DO: In the future I do plan on adding functions for, 
  - delete 
  - merge, 
  - and may other functions, 
  - as well as expanding datatypes to include less common ones


# Installation
**Note**: *This package was build and tested on MySql (so even though it does work with other database software platforms as well) the installation will be tailored for mySql.*

### A. You will need to download the three pieces of this puzzle
1.  MySQL: the database software --> [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)
2.  A MySQL workbench: to interact with the database --> [MySQL Workbench](https://dev.mysql.com/downloads/workbench/)
3.  A Oracle created Java-SQL communication package: to allow java to talk to MySql [Connector/J](https://dev.mysql.com/downloads/connector/j/)
4.  You need to add the ""mysql-connector-java... .jar file"" to the workspce by:
> File -> project structure -> modules -> dependancies (instead of sources or paths) -> Click the plus sign ->  locate the mysql-connector-java... .jar file -> press apply/ok 
> Then add it to your Java workspace class path 

> a good video that walks though this process (steps 1 & 2) is [MySQL Tutorial for Beginners](https://www.youtube.com/watch?v=7S_tz1z_5bA&t=290s)

### B. Set-Up
 1. In order to be able to use this Java Sql Communication Package, You must place into your project folder 
  - (1) the **JavaSqlCommunication.java** Package/file and 
  - (2) the .jar file (*my version is called **mysql-connector-java-8.0.25.jar***) that can be found in the Connector/J file you downloaded from the oracle site. 
 2. You must obtain the **proper** **URL** for your database.  
 > It will be in the form of **jdbc:mysql://127.0.0.1:3306/?user=*username*** 

> For MySQL this can usually found by right clicking the database icon (button) underneath the **MySQL Connections(+)** line in my SQL and selecting **copy JDBC connection string to clipboard**
 3. You must obtain the proper **User Name** for your database. 
> For MySQL this is next to the person icon by the database icon (button) underneath the **MySQL Connections(+)** 
 4. You must obtain the proper **Password** for your database. 
 - **IMPORTANT::Make sure to write it down when setting up your DataBase** 

## Final Note: Occasionally MySQL is prone to resist attempts to connect to the connector/j driver -(which is a prerequisite for my JavaSqlCommunication package). 
- The complaint will typically be *"access denied"*
- the most popular solution to this issue is to open up the workspace and to run the following query all at once 
> ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password';
> FLUSH PRIVILEGES;
> SELECT user,authentication_string,plugin,host FROM mysql.user;


