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

    # Generate sources from database
    subprocess.call([
        'mvn',
        '-Ddb.url={}'.format(db_url),
        '-Ddb.user={}'.format(db_user),
        '-Ddb.password={}'.format(db_password),
        'generate-sources'
    ], shell=True)


if __name__ == '__main__':
    main()
