#!/usr/bin/env bash

TIMESTAMP=$(date +%F_%R)

mysqldump --protocol tcp -h mysql -u root -proot jazzstandards | bzip2 -9 > "/storage/${TIMESTAMP}.sql.bz2"
ls -l /storage

