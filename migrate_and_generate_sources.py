import os
import subprocess


def main():
    db_url = os.getenv('JDBC_DATABASE_URL')
    db_user = os.getenv('JDBC_DATABASE_USERNAME')
    db_password = os.getenv('JDBC_DATABASE_PASSWORD')

    # Run database migrations
    subprocess.call([
        'mvn',
        'flyway:migrate',
        '-Ddb.url={}'.format(db_url),
        '-Ddb.user={}'.format(db_user),
        '-Ddb.password={}'.format(db_password),
    ], shell=True)

    # Generate sources from database
    subprocess.call([
        'mvn',
        'generate-sources',
        '-Ddb.url={}'.format(db_url),
        '-Ddb.user={}'.format(db_user),
        '-Ddb.password={}'.format(db_password)
    ], shell=True)


if __name__ == '__main__':
    main()
