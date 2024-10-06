# web-data-storage
This is a multi-page web-app that allows authenticated users to store their data in the data-storage using the following operations:deleting,saving,downloading and so on.
## Description
As I sad early this app helps you to store your data in one place. But after saving you can do somethink else. This app contains the main functions to manage your own 
data-storage.You can save your files and then delete them ,also you can download your files later if you'll need it.
Also you can just look at the main information about your files. This functional is avaliable for some user thanks to the web interface that's a frontend part of this app.
If you want to install this web-app for using in the internet then your router should to have a static ip address.

## A several words about the logging system:
I used the logger 'log4j' to log the app 'web-data-storage'.
  What about default logging system 'juli' that it is used to log the application server 'TOMCAT'.

## About the tests infrastructure:
I prefered to up in-memory DB 'H2-database'. Also I used JUNIT5 and POSTMAN.

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
