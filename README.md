# Shopping-list-spark

This is a simple shopping list webapp designed by me for me. The backend is built in Java using Dropwizard, while the frontend is based on React.

## Building the project

Set up your environment with:
* JDK: 10
* Node.js: 12

and run `maven package`. Maven is configured to build both backend and frontend, and package them in a fat JAR. The webapp is started by running:

```
java -jar target/shopping-list-spark-1.0-SNAPSHOT.jar server config.yml
``` 

## CI/CD

Travis CI is used to compile and test the project on each push. If all tests are green the project is automatically deployed to Heroku. Heroku currently does not use the artifacts created by travis, but rather builds the project again before deploy. That should clearly be improved. Should probably replace the current Heroku setup, with use of Travis CI support for deploying to Heroku.   

## Keeping the project up-to-date

### Backend

#### JDK
Update the following files:
* `.travis.yml`
* `system.properties` (for Heroku)

#### Dependencies
1. Run `mvn versions:update-properties -DgenerateBackupPoms=false`
2. Run `mvn versions:use-latest-releases -DgenerateBackupPoms=false`
3. Run `mvn site` and look through the updates reports. Patch anything that was missed by the commands above.

## Frontend

#### Node.js
Update the following files:
* `.travis.yml` 
* `package.json -> engines.node`: (for Heroku)

#### Dependencies
1. Run `npm update`
2. Run `npm outdated` and manually go through the list
