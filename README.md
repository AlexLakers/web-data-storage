# web-data-storage
This is a multi-page web-app that allows authenticated users to store their data in the data-storage using the following operations:deleting,saving,downloading and so on.
## Description
As I sad early this app helps you to store your data in one place. But after saving you can do somethink else. This app contains the main functions to manage your own 
data-storage.You can save your files and then delete them ,also you can download your files later if you'll need it.
Also you can just look at the main information about your files. This functional is avaliable for some user thanks to the web interface that's a frontend part of this app.
If you want to install this web-app for using in the internet then your router should to have a static ip address.

## How does it works?
For example you decided to use this web-app as a user then you have to use some web-browser to open the main page were you can see the app description.
Then you can go to other pages using a navigation menu. The pages can be private or public, if you aren't authenicated user that you can go only the fallowing pages:
'/registration','/login','/'.The private pages are '/files','/files/upload' and other specific pages. You should fill all the required fields by the registration form,however you need to know that some 
fields have limitations, for example you can't set a password if it is too short or it is not used digits and so on. If the registration process is successful then user goes to the login page automatically.
And then you as a authenticated user need to enter your credentials to go to the main page '/files'. On this page you can see all the files in your own file-storage.

  Also you can apply dynamic filter that allows to filter search result on the page '/files'. 
And here you can to store a new file from your PC to web-storage,I want to informative you that data content is stored into the HD or SSD by some server in the base dir. And what about other file information it is stored in the database table 'files'.

  By the way I forgot to mention two more database tables.
Account information is saved in the database too but in the another table 'account'.The last table is 'role' that contains available roles in this app.
Ok, let's continue... You can click the button for specific action(delete,ddownload,upload) diring you are located on the main page '/files' by authenticated account.
In this app I use web-filters to handle all the input request.As a database is choosed PostgreSQL.  

## About the project stucture and building:
This application is developed using MVC pattern.
  The servlets as a controller layer. 
  As a view level I used classic HTML + JSP pages.
  In this app I use web-filters to pre-handling all the input requests from user.
  Also I use DTO to interaction between different app layers and filter entity's search.
  As a database is choosed 'PostgreSQL' V 14.13.
  As a building system I use 'Maven' V 3.6.3.
  As an application server I installed 'TOMCAT' V 10.1.25.
  java 17.0.11 2024-04-16 LTS.

## A several words about the logging system:
I used the logger 'log4j' to log the app 'web-data-storage'.
  What about default logging system 'juli' that it is used to log the application server 'TOMCAT'.

## About the tests infrastructure:
I prefered to up in-memory DB 'H2-database'. Also I used JUNIT5 and POSTMAN.

## How does it install?

Firstly you should to install all the necassary programms:Postgres,Tomcat and Maven.

You need to execute the script 'query.sql':

```
CREATE DATABASE files_repository;

CREATE SCHEMA files_storage IF NOT EXISTS;

CREATE TABLE role IF NOT EXISTS(
    id BIGSERIAL PRIMARY KEY ,
    name VARCHAR(128) NOT NULL UNIQUE
);

INSERT INTO role(name) 
    VALUES ('MODERATOR'),('USER'),('ADMIN');


CREATE TABLE files_storage.account IF NOT EXISTS(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(128) NOT NULL,
    last_name VARCHAR(128) NOT NULL,
    birth_date DATE NOT NULL ,
    login VARCHAR(128) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    role_id BIGINT NOT NULL REFERENCES role(id) ON UPDATE CASCADE ON DELETE CASCADE ,
    folder VARCHAR(128) NOT NULL UNIQUE
);


CREATE TABLE files_storage.file_info IF NOT EXISTS(
    id BIGSERIAL PRIMARY KEY ,
    name VARCHAR(128) NOT NULL,
    upload_date DATE NOT NULL,
    size BIGINT NOT NULL ,
    account_id BIGINT NOT NULL REFERENCES account(id) ON UPDATE CASCADE ON DELETE CASCADE
);

```




Then you need to correct some properties in the 'application.properties' if it is necessary:

The application.properties ***for prod:***

```properties
connection.url=jdbc:postgresql://localhost:5432/files_repository
connection.username=postgres
connection.password=postgres
connection.pool.size=5
connection.driver=org.postgresql.Driver
storage.base.dir=/home/alexlakers/data-storage
```

The application.properties ***for tests:***
```
connection.url=jdbc:h2:mem:default;lock_mode=0;DB_CLOSE_DELAY=-1
connection.username=sa
connection.password=
connection.pool.size=5
connection.driver=org.h2.Driver
storage.base.dir=/home/alexlakers/file-storage
```

You should control that the env var '$CATALINA_HOME' is seted.
It is a part of path to logging files.I used the next path:

```
export CATALINA_HOME=/opt/apache-tomcat-10.1.25.
```


Then you can build this app using maven.
```
mvm clean package
```

After it you need to deploy WAR file to the application server.


***Bellow you can see the refs to all the neccessary  lib:***

> [!IMPORTANT]
> - "Jakarta Servlet 6.0.0" https://mvnrepository.com/artifact/jakarta.servlet/jakarta.servlet-api/6.0.0
> - "PostgreSQL JDBC Driver » 42.7.3" https://mvnrepository.com/artifact/org.postgresql/postgresql/42.7.3
> - "Project Lombok 1.18.34" https://mvnrepository.com/artifact/org.projectlombok/lombok/1.18.34
> - ***The additional libs for tests and logging:***
> - "H2 Database Engine 2.2.224" https://mvnrepository.com/artifact/com.h2database/h2/2.2.224
> - "Apache Log4j  1.2.17" https://mvnrepository.com/artifact/log4j/log4j/1.2.17
> - "JUnit Jupiter API 5.11.0" https://mvnrepository.com/artifact/org.mockito/mockito-junit-jupiter
> - "Mockito JUnit Jupiter 5.11.0" https://mvnrepository.com/artifact/org.mockito/mockito-junit-jupiter
> - "JUnit Jupiter Engine 5.11.0" https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine
> - "JUnit Jupiter Params 5.11.0" https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-params
> - "Mockito Core 5.11.0" https://mvnrepository.com/artifact/org.mockito/mockito-core
> - ***And some libs for using jstl:***
> - "Jakarta Standard Tag Library Implementation 2.0.0" https://mvnrepository.com/artifact/org.mockito/mockito-core
> - "Jakarta Standard Tag Library API » 3.0.0" https://mvnrepository.com/artifact/jakarta.servlet.jsp.jstl/jakarta.servlet.jsp.jstl-api/3.0.0
