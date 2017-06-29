from yaml import load
try:
    from yaml import CLoader as Loader, CDumper as Dumper
except ImportError:
    from yaml import Loader, Dumper

import subprocess


def main():
    with open('src/main/resources/conf/application.yml', 'r') as file:
        data = load(file.read())

    db_url = data['database']['url']
    db_user = data['database']['user']
    db_password = data['database']['password']

    subprocess.call([
        'mvn',
        'flyway:migrate',
        '-Ddb.url={}'.format(db_url),
        '-Ddb.user={}'.format(db_user),
        '-Ddb.password={}'.format(db_password),
    ], shell=True)

    subprocess.call([
        'mvn',
        'generate-sources',
        '-Ddb.url={}'.format(db_url),
        '-Ddb.user={}'.format(db_user),
        '-Ddb.password={}'.format(db_password)
    ], shell=True)
    # Generate sources from database


if __name__ == '__main__':
    main()
