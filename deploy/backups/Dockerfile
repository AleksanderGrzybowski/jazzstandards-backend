FROM mariadb

RUN apt-get update && apt-get install -y cron
 
ADD crontab /etc/cron.d/backup
ADD backup.sh /backup.sh
RUN chmod 0644 /etc/cron.d/backup
RUN chmod 0777 /backup.sh
 
RUN touch /var/log/cron.log
 
CMD cron && tail -f /var/log/cron.log
