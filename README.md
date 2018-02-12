## Prepare workspace
1. Install PyYAML: `pip install pyyaml`
2. Install [Lombok integration](https://projectlombok.org/) for your IDE of choice

## When there are new migration scripts
1. Run `migrate_and_generate_sources.py`

## Configuration
Environment variables:
* PORT
* JDBC_DATABASE_URL
* JDBC_DATABASE_USER
* JDBC_DATABASE_PASSWORD

## To deploy new version to Heroku
1. Migrate production database
   1. `export JDBC_DATABASE_URL="<JDBC_URL>?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory" JDBC_DATABASE_USERNAME=<USERNAME> JDBC_DATABASE_PASSWORD=<PASSWORD>`
   2. `mvn flyway:migrate`
2. Manually deploy corresponding code version

## Special files
* system.properties
  * Specifies jdk version for Heroku
* .mvn/jvm.config
  * Adds java 9 modules required when running the jooq-codegen-maven plugin
* Procfile
  * Heroku configuration