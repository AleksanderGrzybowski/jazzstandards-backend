FROM mariadb

RUN apt-get update && apt-get install -y cron
 
ADD crontab /etc/cron.d/backup
RUN chmod 0644 /etc/cron.d/backup
 
RUN touch /var/log/cron.log
 
CMD cron && tail -f /var/log/cron.log
