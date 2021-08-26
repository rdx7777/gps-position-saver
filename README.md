# Gps position saver

is an application which receives and saves in the database information about 
positions (geolocation) from mobile devices, e.g. phone / GPS locator.

Application is licensed by [MIT](https://opensource.org/licenses/mit-license.php)

## Tech/frameworks used ##

<img src="https://whirly.pl/wp-content/uploads/2017/05/spring.png" width="200"><img src="http://yaqzi.pl/wp-content/uploads/2016/12/apache_maven.png" width="200"><img src="https://upload.wikimedia.org/wikipedia/commons/2/2c/Mockito_Logo.png" width="200"><img src="https://www.admfactory.com/images/logos/junit-400.jpg" width="200"><img src="https://jules-grospeiller.fr/media/logo_competences/lang/json.png" width="200">

## Instalation ##

* JDK 1.8
* Apache Maven 3.x

## Build and Run ##
```
mvn clean verify
java -jar target/gps-position-saver-0.0.1-SNAPSHOT.jar
```

## Dependencies ##

All dependencies are included in `pom.xml` file.

## API ##

Application is available on localhost:8080.

## Swagger ##

The documentation of all endpoints offered by this application is available 
after starting the application at http://localhost:8080/swagger-ui.html