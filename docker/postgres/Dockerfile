FROM postgres:9.4.4

# All sh files found under docker-entry... will be executed by
# postgres initdb
COPY ["config/snitch-db.sh", "/docker-entrypoint-initdb.d/"]

# Enable pg_stats_statements and set VSL requirements
COPY config/postgresql.conf /postgresql.conf

EXPOSE 5432
