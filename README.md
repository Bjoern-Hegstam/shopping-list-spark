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
