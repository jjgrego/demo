#!/bin/sh

clear

set -e
set -x

mysql_install_db

# Start the MySQL daemon in the background.
/usr/sbin/mysqld &
mysql_pid=$!

until mysqladmin ping >/dev/null 2>&1; do
  echo -n "."; sleep 0.2
done

# Permit root login without password from outside container.
mysql -e "GRANT ALL ON *.* TO root@'%' IDENTIFIED BY '' WITH GRANT OPTION"

# create the default database from the ADDed file.
mysql < /tmp/epcis_schema.sql

# Tell the MySQL daemon to shutdown.
mysqladmin shutdown

# Wait for the MySQL daemon to exit.
wait $mysql_pid

# create a tar file with the database as it currently exists
tar czvf default_mysql.tar.gz /var/lib/mysql
