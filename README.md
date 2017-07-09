## Prepare workspace
1. Install PyYAML: `pip install pyyaml`
2. Install [Lombok integration](https://projectlombok.org/) for your IDE of choice

## When there are new migration scripts
1. Configure database in `conf/application.yml`
2. Run `migrate_and_generate_sources.py`