## Prepare workspace
1. Install [Lombok integration](https://projectlombok.org/) for your IDE of choice

## Special files
* system.properties
  * Specifies jdk version for Heroku
* Procfile
  * Heroku configuration

## Keeping the project up-to-date

### Backend
1. `mvn versions:update-properties -DgenerateBackupPoms=false`
2. `mvn versions:use-latest-releases -DgenerateBackupPoms=false`

## Frontend
1. Run `npm update`
2. Run `npm outdated` and manually go through the list

### Known dependency issues
* Upgrading axios to `0.19.0` breaks application. Calls to backend return 406. 
