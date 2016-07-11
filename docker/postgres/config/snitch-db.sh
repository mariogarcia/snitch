#!/bin/bash
echo "######### CREATE SNITCH-DB ##########"

# Perform all actions as user 'postgres'
export PGUSER=postgres

# Create f1db users and databases (dev/test)
psql <<EOSQL
    CREATE ROLE snitch WITH LOGIN PASSWORD 'snitch';
    CREATE DATABASE snitch OWNER snitch;
EOSQL

# psql -U snitch snitch < /db/snitch.psql

echo ""
echo "######### SNITCH CONFIGURATION  ##########"

cp /postgresql.conf /var/lib/postgresql/data/

echo ""
echo "######### SNITCHDATABASE CREATED ##########"
