## Environment

### Versions
* JDK: 10
* node js: 12

### Specified in
* system.properties: jdk version for Heroku
* package.json
  * engines.node: node js version for Heroku
* .travis.yml: jdk and node js version specified in CI configuration 

## Other configuration
* Procfile: Heroku configuration

## Keeping the project up-to-date

### Backend
1. `mvn versions:update-properties -DgenerateBackupPoms=false`
2. `mvn versions:use-latest-releases -DgenerateBackupPoms=false`

## Frontend
1. Run `npm update`
2. Run `npm outdated` and manually go through the list
