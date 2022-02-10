# bank-holiday-api

An instance of redit is needed you can run
> docker run --name redis-poc -p 6379:6379 -d --rm redis

MySql is running on port 13306
> docker run --name mysql-poc -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -p 13306:3306 --rm mysql

Upcoming additions add

- Persist MySql data
- Persist redis cache
- Setup job for getting data
  - Wired in needs to check existing data before saving
- Fix tests
  - package fails because the conf is looking for mysql and redis
- Look up Canadian Holidays
- Look up European Holidays
- Store the env settings better
     
